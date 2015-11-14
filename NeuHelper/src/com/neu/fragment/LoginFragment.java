package com.neu.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.neu.helper.R;
import com.neu.tools.MyDialog;
import com.neu.tools.NetUtils;
import com.neu.tools.ResultInfoTools;
import com.neu.tools.StreamTools;

public class LoginFragment extends Fragment {

	public static final int MSG_SUCCESS = 0;

	public static final int MSG_EXCEPTION = 1;

	public static final int MSG_FAIL = 2;

	protected static final int MSG_NULL = 3;

	private Dialog dialog;
	private EditText username;
	private EditText password;
	private RadioGroup rangeGroup;
	private RadioButton radioButton;
	private Button connect;
	private Button disconnect;
	private String range = "";
	private String operation = "";
	private SharedPreferences sp;

	@SuppressLint("HandlerLeak") Handler hander = new Handler() {
		public void handleMessage(Message msg) {

			dialog.dismiss();

			switch (msg.what) {
			case MSG_SUCCESS:
				Bundle bundle = new Bundle();
				bundle = msg.getData();
				String result = bundle.getString("result");
				
				/*Toast toast= new Toast(getActivity()); 
				toast.setGravity(Gravity.LEFT , 0, 0); 
				toast.setDuration(Toast.LENGTH_LONG); */
				Toast toast = Toast.makeText(getActivity(), result, Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				
				Editor editor = sp.edit();
				editor.putString("username", username.getText().toString()
						.trim());
				editor.putString("password", password.getText().toString()
						.trim());
				editor.commit();
				break;
			case MSG_EXCEPTION:
				System.out.println("网络错误");
				Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case MSG_FAIL:
				System.out.println("登录失败");
				Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case MSG_NULL:
				Toast.makeText(getActivity(), "账号或密码不能为空", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.login_page, null);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		username = (EditText) view.findViewById(R.id.username);
		password = (EditText) view.findViewById(R.id.password);
		rangeGroup = (RadioGroup) view.findViewById(R.id.rangeGroup);
		connect = (Button) view.findViewById(R.id.connect);
		disconnect = (Button) view.findViewById(R.id.disconnect);
		getActivity();
		sp = getActivity().getSharedPreferences("config",
				Context.MODE_PRIVATE);
		String user = sp.getString("username", "");
		String pwd = sp.getString("password", "");
		if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pwd)) {
			username.setText(user);
			password.setText(pwd);
		}
		connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(!NetUtils.isHaveWIFI(getActivity())){
					Toast.makeText(getActivity(), "请先连接校园网wifi", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				dialog = MyDialog.createLoadingDialog(getActivity(), "正在连接到网络...");
				

				radioButton = (RadioButton) view.findViewById(rangeGroup
						.getCheckedRadioButtonId());
				if (radioButton.getText().toString().trim().equals("国内")) {
					range = "2";
				} else {
					range = "1";
				}
				operation = "connect";
				String u = username.getText().toString().trim();
				String p = password.getText().toString().trim();
				if(TextUtils.isEmpty(u) || TextUtils.isEmpty(p)){
					Message msg = new Message();
					msg.what = MSG_NULL;
					hander.sendMessage(msg);
					
				}else{
					dialog.show();
					new Thread(new LoginRunnable(u, p, range, operation)).start();
				}
					
				;
			}
		});
		disconnect.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!NetUtils.isHaveWIFI(getActivity())){
					Toast.makeText(getActivity(), "请先连接校园网wifi", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				dialog = MyDialog.createLoadingDialog(getActivity(), "正在断开网络...");
//				dialog.setIndeterminate(false);
				
				radioButton = (RadioButton) view.findViewById(rangeGroup
						.getCheckedRadioButtonId());
				if (radioButton.getText().toString().trim().equals("国内")) {
					range = "2";
				} else {
					range = "1";
				}
				operation = "disconnectall";
				String u = username.getText().toString().trim();
				String p = password.getText().toString().trim();
				if(TextUtils.isEmpty(u) || TextUtils.isEmpty(p)){
					Message msg = new Message();
					msg.what = MSG_NULL;
					hander.sendMessage(msg);
					
				}else{
					dialog.show();
					new Thread(new LoginRunnable(u, p, range, operation)).start();
				}
			}
		});

		return view;
	}

	class LoginRunnable implements Runnable {
		String username;
		String password, range, timeout, operation;

		public LoginRunnable(String username, String password, String range,
				String operation) {
			super();
			this.username = username;
			this.password = password;
			this.range = range;
			this.operation = operation;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(
					"http://202.118.1.95/ipgw/ipgw.ipgw");
			// 设置一个超时时间
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("uid", username));
			parameters.add(new BasicNameValuePair("password", password));
			parameters.add(new BasicNameValuePair("range", range));
			parameters.add(new BasicNameValuePair("timeout", "1"));
			parameters.add(new BasicNameValuePair("operation", operation));
			UrlEncodedFormEntity entity;
			try {
				entity = new UrlEncodedFormEntity(parameters, "utf-8");

				httpPost.setEntity(entity);
				HttpResponse response = client.execute(httpPost);

				int code = response.getStatusLine().getStatusCode();
				if (code == 200) {
					Bundle bundle = new Bundle();
					Message msg = new Message();
					msg.what = MSG_SUCCESS;
					InputStream is = response.getEntity().getContent();
					String xmlInfo = StreamTools.readFromStream(is);
					String result = ResultInfoTools.getResultInfo(xmlInfo).replace("&nbsp;", "  ");
				//	System.out.println("result:" + result);
					/* 获取中文 */
					/*
					 * String regex="([\u4e00-\u9fa5]+)"; Matcher matcher =
					 * Pattern.compile(regex).matcher(result); StringBuffer
					 * buffer = new StringBuffer(); while(matcher.find()){
					 * buffer.append(matcher.group(0));
					 * 
					 * }
					 */
					// if(matcher.find()){
					bundle.putString("result", result);
					msg.setData(bundle);
					hander.sendMessage(msg);
					// }
				} else {
					Message msg = new Message();
					msg.what = MSG_FAIL;
					hander.sendMessage(msg);
				}

			} catch (Exception e) {
				Message msg = new Message();
				msg.what = MSG_EXCEPTION;
				hander.sendMessage(msg);
				e.printStackTrace();
			}

		}

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

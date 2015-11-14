package com.neu.fragment;



import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.neu.helper.R;
import com.neu.tools.NetUtils;
import com.neu.tools.UpdateManager;

public class CheckFragment extends Fragment {

	private LinearLayout nowVersion;
	private LinearLayout checkVersion;
	UpdateManager manager;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.check_new_version, null);
		checkVersion = (LinearLayout) view.findViewById(R.id.now);
		checkVersion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				getVersionCode(getActivity());
			}
		});

		nowVersion = (LinearLayout) view.findViewById(R.id.check);
		nowVersion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
//				Toast.makeText(getActivity(), "目前没有更新", Toast.LENGTH_SHORT).show();
				if(NetUtils.isHaveInternet(getActivity())){
					manager = new UpdateManager(getActivity(),false);
					// 检查软件更新
					manager.checkUpdate(); 
				}else{
					Toast.makeText(getActivity(), "网络未连接", 2000).show();
				}
			}
		});

		return view;

	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private void getVersionCode(Context context) {
		String versionCode = null;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(
					getActivity().getPackageName(),
					PackageManager.GET_CONFIGURATIONS).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Toast.makeText(getActivity(), "当前版本" + versionCode, Toast.LENGTH_SHORT)
				.show();
	}

}

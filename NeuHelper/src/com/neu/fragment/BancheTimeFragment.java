/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neu.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import android.app.Dialog;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.exception.UnkownNetWorkException;
import com.neu.helper.R;
import com.neu.javabean.DaysArrangement;
import com.neu.javabean.Person;
import com.neu.javabean.TeacherBancheInfo;
import com.neu.tools.MyDialog;
import com.neu.tools.NetUtils;
import com.neu.tools.ParseXmlService;
import com.neu.tools.PopMenu;
import com.neu.tools.ResultInfoTools;
import com.neu.tools.StoreBancheInfoUtils;
import com.neu.tools.StreamTools;
import com.neu.tools.constant.ConstantUtils;

public class BancheTimeFragment extends Fragment {
	
	private final int MSG_NETERROR = 0;
	
	TableLayout tableInfo;
	private RelativeLayout fromHunnan;// 从浑南校区出发
	private RelativeLayout fromNanhu;// 从南湖校区出发
	private TextView from_tx;// 临时开车地点
	private LinearLayout stu_show;
	private ImageView selected_image;
	private TextView selected_name;

	String searchName = "";
	String searchName_fore = "";
	Dialog dialog;
	NodeList nodeList; // 记录班车信息表
	String from;
	SearchAsyncTask task;
	int screenWidth;
	int screenHeight;
	
	Toast toast ;
	
	TextView freshInfo;//刷新班车信息 存入本地
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_NETERROR:
				Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
			
		}
	};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.banche_info, null);
		screenWidth = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getActivity().getWindowManager().getDefaultDisplay()
				.getHeight();
		
		toast = new Toast(getActivity());
		
		// System.out.println(screenHeight+"px");
		// System.out.println("width:" + screenWidth);
		tableInfo = (TableLayout) view.findViewById(R.id.tableInfo);
		fromHunnan = (RelativeLayout) view.findViewById(R.id.hunnan_bt);
		fromNanhu = (RelativeLayout) view.findViewById(R.id.nanhu_bt);
		from_tx = (TextView) view.findViewById(R.id.from_tx);
		// spinner = (Spinner) view.findViewById(R.id.spinner_type);
		// 被选择的类型的头像和名字
		selected_image = (ImageView) view.findViewById(R.id.selected_image);
		selected_name = (TextView) view.findViewById(R.id.selected_name);
		searchName = selected_name.getText().toString();
		searchName_fore = searchName;

		stu_show = (LinearLayout) view.findViewById(R.id.stu_show);
		dialog = MyDialog.createLoadingDialog(getActivity(), "正在查询...");

		freshInfo = (TextView) view.findViewById(R.id.fresh_info);
		//刷新班车信息，存入本地
		freshInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				new Thread(new Runnable() {
					String result = "";
					@Override
					public void run() {
						try {
							// TODO Auto-generated method stub
							//从本地xml文件获取班车信息
						StoreBancheInfoUtils.storeInfoFromNet(getActivity());
						
							
						} catch(UnkownNetWorkException e){
							System.out.println(e.getMessage());
							
							Message msg = new Message();
							msg.what = MSG_NETERROR;
							handler.sendMessage(msg);
							
						}catch (IOException e) {
							// TODO: handle exception
						}
					}
				}).start();
			}
		});
		// popMenu
		final PopMenu popMenu = new PopMenu(getActivity(),screenWidth);

		// 按钮用来选择是老师，还是学生
		RelativeLayout showType = (RelativeLayout) view
				.findViewById(R.id.select_type);
		showType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				popMenu.showAsDropDown(view);

			}
		});
		// 选完后 指定点击事件
		popMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				searchName = ((Person) parent.getItemAtPosition(position))
						.getItem_name().toString();
				int drawable = ((Person) parent.getItemAtPosition(position))
						.getItem_image();
				selected_image.setBackgroundResource(drawable);
				selected_name.setText(searchName);
				if(searchName.equals("学生")){
					if(!searchName_fore.equals(searchName)){
						stu_show.setVisibility(View.VISIBLE);
						from_tx.setText("");
						tableInfo.removeAllViews();
					}
				}
				else if(searchName.equals("老师")){
					if(!searchName_fore.equals(searchName)){
						stu_show.setVisibility(View.GONE);
						from_tx.setText("");
						tableInfo.removeAllViews();
					}
				}
				searchName_fore = searchName;

				popMenu.dismiss();
			}
		});

		fromHunnan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
					from = "浑南";
					task = new SearchAsyncTask();
					task.dialog = dialog;
					task.from = from;
					task.stu_show = stu_show;
					task.from_tx = from_tx;
					task.toast = toast;
					task.searchName = searchName;
					task.tableInfo = tableInfo;
					task.execute();
			}
		});
		fromNanhu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
					if(searchName.equals("学生"))
						from = "宁恩承";
					else from = "南湖";
					task = new SearchAsyncTask();
					task.dialog = dialog;
					task.from = from;
					task.toast = toast;
					task.stu_show = stu_show;
					task.from_tx = from_tx;
					task.searchName = searchName;
					task.tableInfo = tableInfo;
					task.execute();
			}
		});

		return view;
	}

	class SearchAsyncTask extends AsyncTask<Object, Object, Object> {
		Dialog dialog;
		NodeList nodeList;
		ArrayList<TeacherBancheInfo> infos = null;//记录教师班车信息
		TableLayout tableInfo;
		TextView from_tx;
		LinearLayout stu_show;
		String from; // 记录从哪个校区出发
		String searchName;// 查询类型
		int sign; // 用来记录 接下来要展示的记录属于第几列
		
		Toast toast;
		

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				if (searchName.equals("学生")){
//					nodeList = ResultInfoTools.getBancheInfo(new URL(
//							ConstantUtils.stuBancheInfo), "MsoNormalTable");
			
//					InputStream is = ResultInfoTools.class.getClassLoader()
//							.getResourceAsStream("stu_newinfo.html");
					
					InputStream is = StoreBancheInfoUtils.getBancheInfoInputStream(getActivity(),ConstantUtils.stuInfoStr);
					if(is == null){
						//如果本地为空，就从网络获取
						nodeList = ResultInfoTools.getBancheInfo(new URL(
								ConstantUtils.stuBancheInfo), "MsoNormalTable");
					}else{
						//如果本地有，就从本地获取
						nodeList = ResultInfoTools.getBancheInfoStr(
								StreamTools.getStreamAsString(is, "utf-8"), "MsoNormalTable");
					}
				}
				else{
					/*从网络获取班车信息
					 * nodeList = ResultInfoTools.getBancheInfo(new URL(
							ConstantUtils.teaBancheInfo), "MsoTableGrid");*/
					//从本地xml文件获取班车信息
//					URL url = new URL(ConstantUtils.teaBancheInfo);
//					URLConnection connection = url.openConnection();
//					connection.connect();
//					InputStream is =  (InputStream) connection.getContent();
//					System.out.println(StreamTools.getStreamAsString(is, "utf-8"));
//					InputStream is = ParseXmlService.class.getClassLoader()
//							.getResourceAsStream("teacherBancheInfo.xml");
					InputStream is = StoreBancheInfoUtils.getBancheInfoInputStream(getActivity(),ConstantUtils.teaInfoStr);
					if(is == null){
						if(!NetUtils.isHaveInternet(getActivity())){
							throw new UnkownNetWorkException("网络未连接");
						}
						//如果为空 则从网络获取
						URL url = new URL(ConstantUtils.teaBancheInfo);
						URLConnection connection = url.openConnection();
						connection.connect();
						is =  (InputStream) connection.getContent();
					}
						infos = ResultInfoTools.getTeacherBancheInfo(is);
					
				}
			} catch (ParserException e) {

//				Toast.makeText(getActivity(), "parserException", 2000).show();
				System.out.println("parserException");
				e.printStackTrace();
			} catch(UnkownNetWorkException e){
				System.out.println(e.getMessage().toString());
				Message msg = new Message();
				msg.what = MSG_NETERROR;
				handler.sendMessage(msg);
				
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {

		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.hide();
			tableInfo.removeAllViews();
			if(searchName.equals("学生")){
				
				getStuInfo();
			}
			else{
				getTeaInfo();
			}
		}
		public void getTeaInfo(){
			int i;
			TeacherBancheInfo info = null;
			if(infos == null)return ;
			for(i=0;i<infos.size();i++){
				if(infos.get(i).getCampusName().contains(from)){
					info = infos.get(i);
					break;
				}
			}
			from_tx.setText(info.getCampusName()+"出发地："+info.getStartingPlace());
			android.widget.TableRow row,rowTemp;
			TableLayout table_temp ;
			for(DaysArrangement day : info.getDays()){
				row = new android.widget.TableRow(
						getActivity());
				TextView t = new TextView(getActivity());
				t.setHeight(screenHeight * 40 * day.getTimes().size()
						/ ConstantUtils.constantHeight);
				t.setTextColor(Color.BLACK);
				t.setGravity(Gravity.CENTER);
				t.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.textview_bar));
				t.setTextSize(16);
				t.setText(day.getDaysDate());
				t.setWidth(screenWidth / 2 );
				row.addView(t);
				
				table_temp = new TableLayout(getActivity());
				for(String s:day.getTimes()){
					rowTemp = new android.widget.TableRow(getActivity());
					TextView t_temp = new TextView(getActivity());
					t_temp.setHeight(screenHeight * 40 
							/ ConstantUtils.constantHeight);
					t_temp.setTextColor(Color.BLACK);
					t_temp.setGravity(Gravity.CENTER);
					t_temp.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.textview_bar));
					t_temp.setTextSize(16);
					t_temp.setText(s);
					t_temp.setWidth(screenWidth / 2 );
					rowTemp.addView(t_temp);
					table_temp.addView(rowTemp);
				}
				row.addView(table_temp);
				tableInfo.addView(row);
			}
			
			
			
			
		}
		public void getStuInfo() {
			int i, j;
			if(nodeList == null)return ;
			TableTag tag = (TableTag) nodeList.elementAt(0);
			TableRow[] rows = tag.getRows();
			TableColumn[] colsZero = rows[0].getColumns();
			// System.out.println(from);
			for (i = 0; i < colsZero.length; i++) {
				if (colsZero[i].toPlainTextString().contains(from)) {
					sign = i;
					from_tx.setHeight(screenHeight * 40 * 4
							/ ConstantUtils.constantHeight);
					from_tx.setText(colsZero[i].toPlainTextString());
					break;

				}
			}
			// tableInfo.addView(rowFrom);
			android.widget.TableRow rowTitle = new android.widget.TableRow(
					getActivity());
			TableColumn[] colsOne = rows[1].getColumns();
			for (i = 0; i < colsOne.length / 2; i++) {
				TextView t = new TextView(getActivity());
				t.setTextColor(Color.BLACK);
				t.setGravity(Gravity.CENTER);
				t.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.textview_bar));
				t.setTextSize(16);
				t.setText(colsOne[i].toPlainTextString());
				// System.out.println("t:" + colsOne[i].toPlainTextString());
				t.setWidth(screenWidth / 3 - 1);
				t.setHeight(40);
				rowTitle.addView(t);
			}
			// tableInfo.addView(rowTitle);

			android.widget.TableRow row, row_temp;
			TableLayout table_temp;
			table_temp = new TableLayout(getActivity());
			row = new android.widget.TableRow(getActivity());
			for (i = 2; i < rows.length; i++) {

				TableColumn[] cols = rows[i].getColumns();

				if (cols.length == colsOne.length) {
					if (i != 2) {
						row.addView(table_temp);
						tableInfo.addView(row);
						row = new android.widget.TableRow(getActivity());
						table_temp = new TableLayout(getActivity());
					}
					// table_temp.removeAllViews();

					row_temp = new android.widget.TableRow(getActivity());
					for (j = (colsOne.length / 2) * sign; j < (colsOne.length / 2)
							* sign + colsOne.length / 2; j++) {
						TextView t = new TextView(getActivity());
						if (j == (colsOne.length / 2) * sign) {
							t.setHeight(screenHeight * 40 * 4
									/ ConstantUtils.constantHeight);
							t.setTextColor(Color.BLACK);
							t.setGravity(Gravity.CENTER);
							t.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.textview_bar));
							t.setTextSize(16);
							t.setText(cols[j].toPlainTextString());
							t.setWidth(screenWidth / 3 - 1);
							row.addView(t);
						} else {
							t.setHeight(screenHeight * 40
									/ ConstantUtils.constantHeight);
							t.setTextColor(Color.BLACK);
							t.setGravity(Gravity.CENTER);
							t.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.textview_bar));
							t.setTextSize(16);
							t.setText(cols[j].toPlainTextString());
							t.setWidth(screenWidth / 3);
							row_temp.addView(t);
						}
					}
					table_temp.addView(row_temp);
				} else {
					row_temp = new android.widget.TableRow(getActivity());
					for (j = (cols.length / 2) * sign; j < (cols.length / 2)
							* sign + cols.length / 2; j++) {
						TextView t = new TextView(getActivity());
						t.setHeight(screenHeight * 40
								/ ConstantUtils.constantHeight);
						t.setTextColor(Color.BLACK);
						t.setGravity(Gravity.CENTER);
						t.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.textview_bar));
						t.setTextSize(16);
						t.setText(cols[j].toPlainTextString());
						t.setWidth(screenWidth / 3);
						row_temp.addView(t);
					}

					table_temp.addView(row_temp);
				}
			}
			row.addView(table_temp);
			tableInfo.addView(row);

		}
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

package com.neu.tools;

import java.util.ArrayList;

import com.neu.adapter.SelectorAdapter;
import com.neu.helper.R;
import com.neu.javabean.Person;
import com.neu.tools.constant.ConstantUtils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

public class PopMenu {

	private ArrayList<Person> itemList = null;  
    private Context context;  
    private PopupWindow popupWindow ;  
    private ListView listView;
    
    int screenWidth ; 
	public PopMenu(Context context,int screeWidth) {
		super();
		
		this.context = context;
		this.screenWidth = screeWidth;
		View view = View.inflate(context, R.layout.popmenu, null);
		//设置listview
		listView = (ListView) view.findViewById(R.id.pop_listview);
	
		itemList = new ArrayList<Person>();
		itemList.add(new Person(R.drawable.student_,"学生"));
		itemList.add(new Person(R.drawable.teacher_, "老师"));
		// 建立Adapter并且绑定数据源		
		
		listView.setAdapter(new SelectorAdapter(itemList, context));
		
		popupWindow = new PopupWindow(view, 140*screeWidth/ConstantUtils.constantWidth, LayoutParams.WRAP_CONTENT);   
          
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）  
        popupWindow.setBackgroundDrawable(new BitmapDrawable());  
		
	}  


	//设置菜单项点击监听器  
    public void setOnItemClickListener(OnItemClickListener listener) {  
        //this.listener = listener;  
        listView.setOnItemClickListener(listener);  
    }  
  
    //批量添加菜单项  
    public void addItems(Person[] items) {  
        for (Person s : items)  
            itemList.add(s);  
    }  
  
    //单个添加菜单项  
    public void addItem(Person item) {  
        itemList.add(item);  
    }  
  
    //下拉式 弹出 pop菜单 parent 右下角  
    public void showAsDropDown(View parent) {  
        popupWindow.showAsDropDown(parent, 10,   
                //保证尺寸是根据屏幕像素密度来的  
                -10);  
          
        // 使其聚集  
        popupWindow.setFocusable(true);  
        // 设置允许在外点击消失  
        popupWindow.setOutsideTouchable(true);  
        //刷新状态  
        popupWindow.update();  
    }  
      
    //隐藏菜单  
    public void dismiss() {  
        popupWindow.dismiss();  
    }  
  
 

    
    
}

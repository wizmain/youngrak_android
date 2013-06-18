package com.youngrak;

import java.util.ArrayList;

import com.youngrak.common.dialog.DialogUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Introduce3 extends BaseActivity {
	ArrayList<String> listData;
	BasicListAdapter adapter;
	ListView listview;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_introduce);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "사이버추모");
        
        listview = (ListView)findViewById(R.id.listview);
        
        listData = new ArrayList<String>();
        listData.add("사이버추모관이용안내");
        listData.add("추모방검색");
        listData.add("내가개설한사이버추모관");
        
        adapter = new BasicListAdapter(this, R.layout.basic_row, listData);
        //listview.setAdapter(adapter);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);
        //listview.setAdapter(adapter);
        //ItemAdapter adapter = new ItemAdapter(this, R.layout.basic_row, listData);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> args0, View view, int position, long id) {
				if(position == 0){
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/3_1.php");
					i.putExtra("title", "사이버추모관이용안내");
					startActivity(i);
				} else if(position == 1) {
					Intent i = new Intent(thisContext, TributeSearch.class);
					startActivity(i);
				} else if(position == 2) {
					if(appEx.isLogin()){
						Intent i = new Intent(thisContext, MyTributeRoom.class);
						startActivity(i);
					} else {
						DialogUtils.alert(thisContext, "", "로그인 후에 이용해 주세요");
					}
					
				}
			}
        	
        });
	}
}

package com.youngrak;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Introduce6 extends BaseActivity {
	ArrayList<String> listData;
	BasicListAdapter adapter;
	ListView listview;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_introduce);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "영락공원소개");
        
        listview = (ListView)findViewById(R.id.listview);
        
        listData = new ArrayList<String>();
        listData.add("영락공원소개");
        listData.add("공지사항");
        //listData.add("민원안내");
        listData.add("오시는길");
        listData.add("장사시설안내");
        
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
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/5_1.php");
					i.putExtra("title", "영락공원소개");
					startActivity(i);
				} else if(position == 1) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/cms/bbs/board_mobile.php?dk_table=cyber_06_1");
					i.putExtra("title", "공지사항");
					startActivity(i);
				} else if(position == 2) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/5_5.php");
					i.putExtra("title", "오시는길");
					startActivity(i); 
				} else if(position == 3) {
					Intent i = new Intent(thisContext, Introduce6_4.class);
					startActivity(i); 
				}
			}
        	
        });
	}
}

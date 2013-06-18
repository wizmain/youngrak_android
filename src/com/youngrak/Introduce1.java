package com.youngrak;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Introduce1 extends BaseActivity {
	
	
	ArrayList<String> listData;
	BasicListAdapter adapter;
	ListView listview;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_introduce);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "사이버추모관안내");
        
        listview = (ListView)findViewById(R.id.listview);
        
        listData = new ArrayList<String>();
        listData.add("사이버추모관안내");
        listData.add("이용안내 및 신청");
        listData.add("사이버 추모관 검색");
        listData.add("하늘나라 우체국 편지");
        
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
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/1_1.php");
					i.putExtra("title", "사이버추모관안내");
					startActivity(i);
				} else if(position == 1) {
					//Intent i = new Intent(thisContext, Webview.class);
					//i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/1_2.php");
					//i.putExtra("title", "이용안내 및 신청");
					Intent i = new Intent(thisContext, Introduce1_2.class);
					startActivity(i);
				} else if(position == 2) {
					Intent i = new Intent(thisContext, TributeSearch.class);
					startActivity(i);
				} else if(position == 3) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/cms/bbs/board_mobile.php?dk_table=cyber_04_4");
					i.putExtra("title", "하늘나라우체국편지");
					startActivity(i);
					
				}
			}
        	
        });
	}
}

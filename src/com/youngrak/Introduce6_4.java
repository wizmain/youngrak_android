package com.youngrak;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Introduce6_4 extends BaseActivity {
	ArrayList<String> listData;
	BasicListAdapter adapter;
	ListView listview;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_introduce);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "장사시설안내");
        
        listview = (ListView)findViewById(R.id.listview);
        
        listData = new ArrayList<String>();
        listData.add("화장시설");
        listData.add("일반매장");
        listData.add("추모관(봉안당)");
        listData.add("자연장");
        listData.add("가족봉안묘");
        listData.add("2기평장분묘");
        
        
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
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_1.php");
					i.putExtra("title", "화장시설");
					startActivity(i);
				} else if(position == 1) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_2.php");
					i.putExtra("title", "일반매장");
					startActivity(i);
				} else if(position == 2) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_3.php");
					i.putExtra("title", "추모관(봉안당)");
					startActivity(i);
				} else if(position == 3) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_4.php");
					i.putExtra("title", "자연장");
					startActivity(i);
				} else if(position == 4) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_5.php");
					i.putExtra("title", "가족봉안묘");
					startActivity(i);
				} else if(position == 5) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_6.php");
					i.putExtra("title", "2기평장분묘");
					startActivity(i);
				}
			}
        	
        });
	}
}

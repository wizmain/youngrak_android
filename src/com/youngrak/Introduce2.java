package com.youngrak;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Introduce2 extends BaseActivity {
	ArrayList<String> listData;
	BasicListAdapter adapter;
	ListView listview;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_introduce);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "고인찾기");
        
        listview = (ListView)findViewById(R.id.listview);
        
        listData = new ArrayList<String>();
        listData.add("고인검색");
        listData.add("위치보기");
        listData.add("추모의방");
        
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
					Intent i = new Intent(thisContext, UserSearch.class);
					startActivity(i);
				} else if(position == 1) {
					
				} else if(position == 2) {
					
				}
			}
        	
        });
	}
}

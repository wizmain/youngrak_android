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
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "���ü��ȳ�");
        
        listview = (ListView)findViewById(R.id.listview);
        
        listData = new ArrayList<String>();
        listData.add("ȭ��ü�");
        listData.add("�Ϲݸ���");
        listData.add("�߸��(���ȴ�)");
        listData.add("�ڿ���");
        listData.add("�������ȹ�");
        listData.add("2������й�");
        
        
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
					i.putExtra("title", "ȭ��ü�");
					startActivity(i);
				} else if(position == 1) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_2.php");
					i.putExtra("title", "�Ϲݸ���");
					startActivity(i);
				} else if(position == 2) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_3.php");
					i.putExtra("title", "�߸��(���ȴ�)");
					startActivity(i);
				} else if(position == 3) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_4.php");
					i.putExtra("title", "�ڿ���");
					startActivity(i);
				} else if(position == 4) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_5.php");
					i.putExtra("title", "�������ȹ�");
					startActivity(i);
				} else if(position == 5) {
					Intent i = new Intent(thisContext, Webview.class);
					i.putExtra("url", "http://www.cyberyoungrak.or.kr/m/6_4_6.php");
					i.putExtra("title", "2������й�");
					startActivity(i);
				}
			}
        	
        });
	}
}

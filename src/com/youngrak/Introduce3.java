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
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "���̹��߸�");
        
        listview = (ListView)findViewById(R.id.listview);
        
        listData = new ArrayList<String>();
        listData.add("���̹��߸���̿�ȳ�");
        listData.add("�߸��˻�");
        listData.add("���������ѻ��̹��߸��");
        
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
					i.putExtra("title", "���̹��߸���̿�ȳ�");
					startActivity(i);
				} else if(position == 1) {
					Intent i = new Intent(thisContext, TributeSearch.class);
					startActivity(i);
				} else if(position == 2) {
					if(appEx.isLogin()){
						Intent i = new Intent(thisContext, MyTributeRoom.class);
						startActivity(i);
					} else {
						DialogUtils.alert(thisContext, "", "�α��� �Ŀ� �̿��� �ּ���");
					}
					
				}
			}
        	
        });
	}
}

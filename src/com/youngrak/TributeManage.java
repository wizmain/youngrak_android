package com.youngrak;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.youngrak.common.Constant;
import com.youngrak.common.HttpDataHelper;
import com.youngrak.common.StringUtils;
import com.youngrak.model.TributeRoom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TributeManage extends BaseActivity {

	ListView listview;
	ArrayList<String> listData;
	BasicListAdapter adapter;
	String currImageUrl = "";
	String cm1ID;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_introduce);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "추모의방 관리");
        
        currImageUrl = getIntent().getStringExtra("currImageUrl");
        cm1ID = getIntent().getStringExtra("cm1ID");
        
        listview = (ListView)findViewById(R.id.listview);
        
        listData = new ArrayList<String>();
        listData.add("고인 사진 변경");
        listData.add("동영상 및 육성 파일");
        listData.add("추모앨범");
        
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
					Intent i = new Intent(thisContext, ChangeImage.class);
					i.putExtra("currImageUrl", currImageUrl);
					i.putExtra("cm1ID", cm1ID);
					startActivity(i);
				} else if(position == 1) {
					Intent i = new Intent(thisContext, TributeMovieUpload.class);
					i.putExtra("cm1ID", cm1ID);
					startActivity(i);
				} else if(position == 2) {
					Intent i = new Intent(thisContext, TributeAlbum.class);
					i.putExtra("cm1ID", cm1ID);
					i.putExtra("isManage", true);
					startActivity(i);
				}
			}
        	
        });
	}
	
	
}

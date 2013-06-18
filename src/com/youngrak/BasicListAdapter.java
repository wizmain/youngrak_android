package com.youngrak;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class BasicListAdapter extends ArrayAdapter<String> {
	
	private static String TAG = "BasicListAdapter";
	private Context context;
	private List<String> itemList;
	private int viewId;
	
	public BasicListAdapter(Context context, int textViewResourceId, List<String> itemList) {
		super(context, textViewResourceId, itemList);
		this.itemList = itemList;
		this.context = context;
		viewId=textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if(v == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(viewId, null);
		}
		
		if(itemList != null){
			
			Log.d(TAG, "itemList != null");
			
			final String item = itemList.get(position);
			TextView itemName = (TextView) v.findViewById(R.id.title);
			
			if(itemName != null){
				itemName.setText(item);
			}
			
		}
		return v;
	}
}

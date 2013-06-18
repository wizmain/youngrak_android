package com.youngrak;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.youngrak.common.Constant;
import com.youngrak.common.HttpDataHelper;
import com.youngrak.common.ImageUtil;
import com.youngrak.common.StringUtils;
import com.youngrak.common.HttpMultipartClient.OnProgressListener;
import com.youngrak.common.dialog.DialogUtils;
import com.youngrak.model.TributeRoom;

public class TributeAlbum extends BaseActivity {
	
	private static final String TAG = "TributeAlbum";
	ListView mListView;
	ProgressBar pageLoading;
	String mMessage;
	ArrayList<TributeRoom> mListData;
	TributeAlbumAdapter adapter;
	String cm1ID;
	boolean isManage = false;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_album);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "사이버추모 앨범");
        
        cm1ID = getIntent().getStringExtra("cm1ID");
        isManage = getIntent().getBooleanExtra("isManage", false);
        
        mListView = (ListView)findViewById(R.id.listview);
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        
        ImageButton addAlbum = (ImageButton)findViewById(R.id.addAlbum);
        
        if(isManage){
        	addAlbum.setVisibility(View.VISIBLE);
        	addAlbum.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				Intent i = new Intent(thisContext, TributeAlbumUpload.class);
    				i.putExtra("cm1ID", cm1ID);
    				startActivityForResult(i, 1);
    				//startActivity(i);
    			}
    		});
        } else {
        	addAlbum.setVisibility(View.GONE);
        }
        
        
        new PageLoadTask().execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("AlbumUPload", "ResultCode="+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		
		new PageLoadTask().execute();
	}
	
	private Runnable showDialog = new Runnable() {
		@Override
		public void run(){
			DialogUtils.alert(thisContext, "", mMessage);
		}
	};
	
	private void requestMyTributeAlbum(){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), Constant.TRIBUTE_ALBUM_URL+"?cm1_id2="+cm1ID, "GET", null, -1);
			
			Log.d("MyTributeRoom", result);
    		
			
    		JSONArray data = new JSONArray(result);
    		
    		mListData = new ArrayList<TributeRoom>();
    		
    		for(int i=0;i<data.length();i++){
    			JSONObject o = (JSONObject)data.get(i);
    			mListData.add(TributeRoom.bindJSONObject(o));
    		}
    		
    		
		} catch(JSONException e) {
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage());
        	//DialogUtils.alert(thisContext, "", e.getMessage());
        	mMessage = "JSON 오류가 발생했습니다";
        	thisHandler.post(showDialog);
        } catch(ClientProtocolException e) {
        	e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "서버 접속에 실패했습니다");
    		mMessage = "서버 접속에 실패했습니다.";
    		thisHandler.post(showDialog);
    	} catch(IOException e) {
    		e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "서버에 연결할 수 없습니다");
    		mMessage = "서버에 연결할 수 없습니다.";
    		thisHandler.post(showDialog);
    	} catch(Exception e) {
    		e.printStackTrace();
    		//Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(MyLecture.this, "", e.getMessage());
    	}
	}
	
	private void requestDeleteAlbum(String id){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), Constant.TRIBUTE_ALBUM_DELETE_URL+"?id_no="+id, "GET", null, -1);
			
			Log.d("MyTributeRoom", result);
    		
		} catch(ClientProtocolException e) {
        	e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "서버 접속에 실패했습니다");
    		mMessage = "서버 접속에 실패했습니다.";
    		thisHandler.post(showDialog);
    	} catch(IOException e) {
    		e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "서버에 연결할 수 없습니다");
    		mMessage = "서버에 연결할 수 없습니다.";
    		thisHandler.post(showDialog);
    	} catch(Exception e) {
    		e.printStackTrace();
    		//Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(MyLecture.this, "", e.getMessage());
    	}
	}
	
	private class PageLoadTask extends AsyncTask<String, Integer, Integer> {
 		
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
 		}
 		
 		@Override
 		protected Integer doInBackground(String... args) {
 			
 			requestMyTributeAlbum();
 			return 0;
 		}
 		
 		protected void onPostExecute(Integer result) {
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			if(mListData != null) {
 				Log.d(TAG, "mInterviewData not null length = "+mListData.size());
 	        	adapter = new TributeAlbumAdapter(thisContext, R.layout.tribute_album_row, mListData);
 	        
 	        	mListView.setAdapter(adapter);

 	        	if(mListData.size() == 0){
 	        		DialogUtils.alert(thisContext, "", "등록된 앨범이 없습니다.");
 	        	}
 	    	} else {
 	    		DialogUtils.alert(thisContext, "", "등록된 앨범이 없습니다.");
 	    	}
    		
 		}
 		
 		@Override
 		protected void onCancelled() {
 			// TODO Auto-generated method stub
 			super.onCancelled();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 		}
 	
 	}
	
	
	
	private class AlbumDeleteTask extends AsyncTask<String, Void, Integer> {
	 		
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
 		}
 		
 		
 		@Override
 		protected Integer doInBackground(String... args) {
 			requestDeleteAlbum(args[0]);
 			requestMyTributeAlbum();
 			return 0;
 		}
 		
 		protected void onPostExecute(Integer result) {
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			if(mListData != null) {
 				Log.d(TAG, "mInterviewData not null length = "+mListData.size());
 	        	adapter = new TributeAlbumAdapter(thisContext, R.layout.tribute_album_row, mListData);
 	        
 	        	mListView.setAdapter(adapter);
 	    	}
 		}
 		
 		
 		// AsyncTask.cancel(boolean) 메소드가 true 인자로
 		// 실행되면 호출되는 콜백.
 		// background 작업이 취소될때 꼭 해야될 작업은  여기에 구현.
 		@Override
 		protected void onCancelled() {
 			// TODO Auto-generated method stub
 			super.onCancelled();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 		}
 	
 	}
	
	private View.OnClickListener deleteButtonClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			String id = (String)v.getTag();
			new AlbumDeleteTask().execute(id);
		}
	};
	
	private class TributeAlbumAdapter extends ArrayAdapter<TributeRoom> {
 		
 		private Context context;
 		private List<TributeRoom> itemList;
 		private int viewId;
 		private ImageView tributeImageAlbum;
 		private ImageUtil imageUtil = new ImageUtil();
 		
 		public TributeAlbumAdapter(Context context, int textViewResourceId, List<TributeRoom> itemList) {
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
 				
 				final TributeRoom tributeRoom = itemList.get(position);
 				tributeImageAlbum = (ImageView) v.findViewById(R.id.tribute_image);
 				if(tributeImageAlbum != null){
 					//tributeImage.setImageResource(resId);
 					if(!StringUtils.isEmptyOrBlank(tributeRoom.getAlbumName())){
 						//Log.d(TAG, "image url = "+Constant.SERVER_ADDR + tributeRoom.getImage());
 						//String url = Constant.SERVER_ADDR + tributeRoom.getAlbumName();
 						//Log.d(TAG, "url="+url);
 						//new ImageLoadTask().execute(tributeRoom.getAlbumName());
 						imageUtil.getImageLoad(tributeImageAlbum, tributeRoom.getAlbumName());
 						
 					}
 				}
 				
 				ImageButton deleteButton = (ImageButton)findViewById(R.id.delete_album);
 				
 				if(deleteButton != null){
 					Log.d(TAG, "isManage="+isManage);
 					if(isManage){
 						
 						deleteButton.setTag(tributeRoom.getId());
 						deleteButton.setVisibility(View.VISIBLE);
		 				deleteButton.setOnClickListener(deleteButtonClick);
 					} else {
 						deleteButton.setVisibility(View.GONE);
 					}
 					
 				} else {
 					Log.d(TAG, "deletebutton null isManage="+isManage);
 				}
 				
 			}
 			return v;
 		}
 		
 	 	
 	}
}

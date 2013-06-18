package com.youngrak;

import java.io.IOException;
import java.io.InputStream;
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
import com.youngrak.common.ImageUtil;
import com.youngrak.common.StringUtils;
import com.youngrak.common.dialog.DialogUtils;
import com.youngrak.model.Tomb;
import com.youngrak.model.TributeRoom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class MyTributeRoom extends BaseActivity {
	
	ListView listview;
	TributeRoomAdapter adapter;
	ArrayList<TributeRoom> mListData;
	ProgressBar pageLoading;
	String mMessage = "";
	final static String TAG = "MyTributeRoom";
	ListView mListView;
	
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_introduce);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "���̹��߸�");
        
        mListView = (ListView)findViewById(R.id.listview);
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        //adapter = new TributeRoomAdapter(thisContext, R.layout.user_row, mListData);
        //listview.setAdapter(adapter);
        
        new PageLoadTask().execute();
	}
	
	private Runnable showDialog = new Runnable() {
		@Override
		public void run(){
			DialogUtils.alert(thisContext, "", mMessage);
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("ChangeImage", "ResultCode="+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		
		new PageLoadTask().execute();
	}
	
	private void requestMyTributeRoom(){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), Constant.MY_TRIBUTEROOM_URL, "GET", null, -1);
			
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
        	mMessage = "JSON ������ �߻��߽��ϴ�";
        	thisHandler.post(showDialog);
        } catch(ClientProtocolException e) {
        	e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "���� ���ӿ� �����߽��ϴ�");
    		mMessage = "���� ���ӿ� �����߽��ϴ�.";
    		thisHandler.post(showDialog);
    	} catch(IOException e) {
    		e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "������ ������ �� �����ϴ�");
    		mMessage = "������ ������ �� �����ϴ�.";
    		thisHandler.post(showDialog);
    	} catch(Exception e) {
    		e.printStackTrace();
    		//Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(MyLecture.this, "", e.getMessage());
    	}
	}
	
	private void requestDeleteTributeRoom(String cm1ID){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), Constant.TRIBUTE_DELETE_URL+"?cm1_id="+cm1ID, "GET", null, -1);
			
			Log.d("MyTributeRoom", result);
    		
			
    		JSONObject data = new JSONObject(result);
    		
		} catch(JSONException e) {
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage());
        	//DialogUtils.alert(thisContext, "", e.getMessage());
        	mMessage = "JSON ������ �߻��߽��ϴ�";
        	thisHandler.post(showDialog);
        } catch(ClientProtocolException e) {
        	e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "���� ���ӿ� �����߽��ϴ�");
    		mMessage = "���� ���ӿ� �����߽��ϴ�.";
    		thisHandler.post(showDialog);
    	} catch(IOException e) {
    		e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "������ ������ �� �����ϴ�");
    		mMessage = "������ ������ �� �����ϴ�.";
    		thisHandler.post(showDialog);
    	} catch(Exception e) {
    		e.printStackTrace();
    		//Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(MyLecture.this, "", e.getMessage());
    	}
	}
	
	private class PageLoadTask extends AsyncTask<String, Integer, Integer> {
 		
 		// �̰��� ���Ե� code�� AsyncTask�� execute ���� ���� UI �����忡�� �����.
 		// �۾� ������ UI�� ǥ���ϰų�
 		// background �۾��� ���� ProgressBar�� ���� �ִ� ���� �ڵ带 �ۼ�.
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
 		}
 		
 		// UI �����忡�� AsynchTask��ü.execute(...) ������� ����Ǵ� callback
 		@Override
 		protected Integer doInBackground(String... args) {

 			requestMyTributeRoom();
 			return 0;
 		}
 		
 		// onInBackground(...)�� �Ϸ�Ǹ� �ڵ����� ����Ǵ� callback
 		// �̰����� onInBackground�� ������ ������ UI������ ǥ�� �ϴ� ���� �۾��� ������.
 		// (���������� �۾��� �ɸ� �� �ð��� UI���� �� TextView�� ǥ����)
 		protected void onPostExecute(Integer result) {
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			
 			
 			if(mListData != null) {
 				Log.d(TAG, "mInterviewData not null length = "+mListData.size());
 	        	adapter = new TributeRoomAdapter(thisContext, R.layout.my_tribute_room_row, mListData);
 	        
 	        	mListView.setAdapter(adapter);

 	    	}
    		
 		}
 		
 		
 		// AsyncTask.cancel(boolean) �޼ҵ尡 true ���ڷ�
 		// ����Ǹ� ȣ��Ǵ� �ݹ�.
 		// background �۾��� ��ҵɶ� �� �ؾߵ� �۾���  ���⿡ ����.
 		@Override
 		protected void onCancelled() {
 			// TODO Auto-generated method stub
 			super.onCancelled();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 		}
 	
 	}
	
	private class TributeRoomDeleteTask extends AsyncTask<String, Integer, Integer> {
 		
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
 		}
 		
 		@Override
 		protected Integer doInBackground(String... args) {

 			requestDeleteTributeRoom(args[0]);
 			
 			requestMyTributeRoom();
 			return 0;
 		}
 		
 		protected void onPostExecute(Integer result) {
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			if(mListData != null) {
 				Log.d(TAG, "mInterviewData not null length = "+mListData.size());
 	        	adapter = new TributeRoomAdapter(thisContext, R.layout.my_tribute_room_row, mListData);
 	        
 	        	mListView.setAdapter(adapter);

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
	
	private class TributeRoomAdapter extends ArrayAdapter<TributeRoom> {
 		
 		private Context context;
 		private List<TributeRoom> itemList;
 		private int viewId;
 		private ImageView tributeImage;
 		private ImageUtil imageUtil = new ImageUtil();
 		
 		public TributeRoomAdapter(Context context, int textViewResourceId, List<TributeRoom> itemList) {
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
 				tributeImage = (ImageView) v.findViewById(R.id.tribute_image);
 				TextView deadDate = (TextView) v.findViewById(R.id.dead_date);
 				TextView deadName = (TextView) v.findViewById(R.id.dead_name);
 				
 				if(tributeImage != null){
 					//tributeImage.setImageResource(resId);
 					if(!StringUtils.isEmptyOrBlank(tributeRoom.getImage())){
 						//Log.d(TAG, "image url = "+Constant.SERVER_ADDR + tributeRoom.getImage());
 						
 						//new ImageLoadTask().execute(tributeRoom.getImage());
 						imageUtil.getImageLoad(tributeImage, tributeRoom.getImage());
 					}
 				}
 				deadDate.setText(tributeRoom.getDeadDate());
 				deadName.setText(tributeRoom.getDeadName());
 				
 				ImageButton changeImageButton = (ImageButton)v.findViewById(R.id.btnChangePicture);
 				changeImageButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent i = new Intent(thisContext, ChangeImage.class);
						i.putExtra("cm1ID", tributeRoom.getCmID());
						i.putExtra("currImageUrl", tributeRoom.getImage());
						startActivityForResult(i, 1);
						//startActivity(i);
					}
				});
 				
 				ImageButton cherishRoomButton = (ImageButton)v.findViewById(R.id.btnCherishRoom);
 				cherishRoomButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Log.d(TAG,"cherishRoomButton click");
						Intent i = new Intent(thisContext, CyberTributeRoom.class);
						i.putExtra("cm1ID", tributeRoom.getCmID());
						i.putExtra("deadName", tributeRoom.getDeadName());
						startActivity(i);
						
					}
				});
 				
 				ImageButton cherishRoomDelButton = (ImageButton)v.findViewById(R.id.btnCherishRoomDel);
 				cherishRoomDelButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						new TributeRoomDeleteTask().execute(tributeRoom.getCmID());
					}
				});
 				
 			}
 			return v;
 		}
 		
 		private class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {
 	 		
 	 		protected void onPreExecute(){
 	 			super.onPreExecute();
 	 		}
 	 		
 	 		@Override
 	 		protected Bitmap doInBackground(String... args) {
 	 			URL url;
 	 			Bitmap bmp = null;
 				try {
 					url = new URL(Constant.SERVER_ADDR + args[0]);
 					
 					/*
 					 * Options opts = new Options(); 
                opts.inJustDecodeBounds = true; 
                BitmapFactory.decodeFile(path, opts); 
                Log.e("optwidth",opts.outWidth+""); 
                opts.inJustDecodeBounds = false; 
                if(opts.outWidth>500){ 
                        opts.inSampleSize = 4; 
                        mBitmap = BitmapFactory.decodeFile(path, opts); 
                } 
                else mBitmap = BitmapFactory.decodeFile(path, opts); 
 					 */
 					
 					Options opts = new Options(); 
 	                //opts.inJustDecodeBounds = true;
 					opts.inSampleSize = 8;
 	                
 					bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, opts);
 					
 					
 				} catch (MalformedURLException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (IOException e) {
 					e.printStackTrace();
 				}
 	 			return bmp;
 	 		}
 	 		
 	 		protected void onPostExecute(Bitmap result) {
 	 			tributeImage.setImageBitmap(result);
 	 		}
 	 		
 	 		@Override
 	 		protected void onCancelled() {
 	 			// TODO Auto-generated method stub
 	 			super.onCancelled();
 	 		}
 	 	
 	 	}
 	}
}

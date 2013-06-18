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
import com.youngrak.common.ImageDownloader;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class TributeSearch extends BaseActivity implements RadioGroup.OnCheckedChangeListener ,OnEditorActionListener{
	static final String TAG = "TributeSearch";
	ImageButton searchButton;
	EditText searchText;
	String areaType;
	String searchUserName;
	String mMessage = "";
	ArrayList<TributeRoom> mListData = null;
	ProgressBar pageLoading = null;
	TributeRoomAdapter adapter = null;
	ListView mListView = null;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.user_search);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "�߸���˻�");
        
        areaType = "2";
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        mListView = (ListView)findViewById(R.id.listview);
        RadioGroup areaTypeGroup = (RadioGroup)findViewById(R.id.areaTypeGroup);
        areaTypeGroup.setOnCheckedChangeListener(this);
        searchText = (EditText)findViewById(R.id.searchText);
        searchText.setOnEditorActionListener(this);
        searchButton = (ImageButton)findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(StringUtils.isEmptyOrBlank(searchText.getText().toString())){
					DialogUtils.alert(thisContext, "", "������ �̸��� �Է��ϼ���");
				} else {
				
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
					
					new PageLoadTask().execute();
				}
			}
		});
        
        mListView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> args0, View view, int position, long id) {
        		//Intent i = new Intent(thisContext, UserInfo.class);
        		//startActivity(i);
        	}
        });
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		if(checkedId == R.id.areaType1){
			areaType = "1";
		} else if(checkedId == R.id.areaType2){
			areaType = "2";
		}
		
	}
	
	private Runnable showDialog = new Runnable() {
		@Override
		public void run(){
			DialogUtils.alert(thisContext, "", mMessage);
		}
	};
	
	private void requestSearchUser(){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String url = Constant.TRIBUTE_SEARCH_URL + "?check_type="+areaType+"&dead_name="+searchText.getText().toString();
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), url, "GET", null, -1);
			
			Log.d(TAG, result);
    		
			
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
	
	// AsyncTaskŬ������ �׻� Subclassing �ؼ� ��� �ؾ� ��.
 	// ��� �ڷ�����
 	// background �۾��� ����� data�� �ڷ���: String ��
 	// background �۾� ���� ǥ�ø� ���� ����� ����: Integer��
 	// background �۾��� ����� ǥ���� �ڷ���: Long
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

 			requestSearchUser();
 			return 0;
 		}
 		
 		// onInBackground(...)�� �Ϸ�Ǹ� �ڵ����� ����Ǵ� callback
 		// �̰����� onInBackground�� ������ ������ UI������ ǥ�� �ϴ� ���� �۾��� ������.
 		// (���������� �۾��� �ɸ� �� �ð��� UI���� �� TextView�� ǥ����)
 		protected void onPostExecute(Integer result) {
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			searchText.setText("");
 			
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
 	
 	private class TributeRoomAdapter extends ArrayAdapter<TributeRoom> {
 		
 		private Context context;
 		private List<TributeRoom> itemList;
 		private int viewId;
 		//private ImageView tributeImage;
 		private ImageUtil imageUtil = new ImageUtil();
 		private final ImageDownloader imageDownloader = new ImageDownloader();
 		
 		public TributeRoomAdapter(Context context, int textViewResourceId, List<TributeRoom> itemList) {
 			super(context, textViewResourceId, itemList);
 			this.itemList = itemList;
 			this.context = context;
 			viewId=textViewResourceId;
 			imageDownloader.setMode(ImageDownloader.Mode.CORRECT);
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
 				ImageView tributeImage = (ImageView) v.findViewById(R.id.tribute_image);
 				TextView deadDate = (TextView) v.findViewById(R.id.dead_date);
 				TextView deadName = (TextView) v.findViewById(R.id.dead_name);
 				
 				if(tributeImage != null){
 					//tributeImage.setImageResource(resId);
 					if(!StringUtils.isEmptyOrBlank(tributeRoom.getImage())){
 						String imageUrl = Constant.SERVER_ADDR + tributeRoom.getImage();
 						//Log.d(TAG, "image url = "+Constant.SERVER_ADDR + tributeRoom.getImage());
 						//tributeImage.setImageDrawable(LoadImageFromUrlWeb(Constant.SERVER_ADDR + tributeRoom.getImage()));
 						//new ImageLoadTask().execute(tributeRoom.getImage());
 						//imageUtil.getImageLoad(tributeImage, tributeRoom.getImage());
 						imageDownloader.download(imageUrl, tributeImage);
 					}
 				}
 				deadDate.setText(tributeRoom.getDeadDate());
 				deadName.setText(tributeRoom.getDeadName());
 				
 				ImageButton changeImageButton = (ImageButton)v.findViewById(R.id.btnChangePicture);
 				ImageButton cherishRoomButton = (ImageButton)v.findViewById(R.id.btnCherishRoom);
 				ImageButton cherishRoomDelButton = (ImageButton)v.findViewById(R.id.btnCherishRoomDel);
 				cherishRoomButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(appEx.isLogin()){
							Intent i = new Intent(thisContext, CyberTributeRoom.class);
							i.putExtra("deadName", tributeRoom.getDeadName());
							i.putExtra("cm1ID", tributeRoom.getCmID());
							startActivity(i);
						} else {
							mMessage = "�α��� �Ŀ� �̿��� �ּ���.";
				    		thisHandler.post(showDialog);
						}
					}
				});
 				
 				changeImageButton.setVisibility(View.GONE);
 				cherishRoomDelButton.setVisibility(View.GONE);
 				/*
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
 				
 				
 				
 				
 				cherishRoomDelButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						
					}
				});
 				*/
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
 	                opts.inJustDecodeBounds = true;
 	                opts.inSampleSize = 2;
 	                
 					//bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, opts);
 	                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
 					
 					
 				} catch (MalformedURLException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (IOException e) {
 					e.printStackTrace();
 				}
 	 			return bmp;
 	 		}
 	 		
 	 		protected void onPostExecute(Bitmap result) {
 	 			//tributeImage.setImageBitmap(result);
 	 		}
 	 		
 	 		@Override
 	 		protected void onCancelled() {
 	 			// TODO Auto-generated method stub
 	 			super.onCancelled();
 	 		}
 	 	
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
 	
 	private class TributeRoomDeleteTask extends AsyncTask<String, Integer, Integer> {
 		
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
 		}
 		
 		@Override
 		protected Integer doInBackground(String... args) {

 			requestDeleteTributeRoom(args[0]);
 			
 			
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

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if((actionId == EditorInfo.IME_ACTION_DONE) || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
    		
    		Log.d(TAG, Integer.toString(v.getId()));
    		Log.d(TAG, Integer.toString(v.getId()));
    		if(StringUtils.isEmptyOrBlank(v.getText().toString())){
    			DialogUtils.alert(thisContext, "", "������ ���� �Է��� �ּ���");
    		} else {
    			new PageLoadTask().execute();
    		}
    	}
    	
    	
		return false;
	}
}

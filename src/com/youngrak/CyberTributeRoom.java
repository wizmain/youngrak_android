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
import com.youngrak.common.dialog.DialogUtils;
import com.youngrak.model.Article;
import com.youngrak.model.Tomb;
import com.youngrak.model.TributeRoom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CyberTributeRoom extends BaseActivity {

	final static String TAG = "CyberTributeRoom";
	ArrayList<Article> listData;
	ArticleAdapter adapter;
	ListView listview;
	String deadName = "";
	String cm1ID = "";
	ProgressBar pageLoading = null;
	String mMessage = "";
	ImageButton movieButton, photoButton, manageButton, tributeMessageButton;
	TributeRoom tributeRoom = null;
	ImageView tributeImage;
	String tributeMovie = "";
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_room);
        
        deadName = getIntent().getStringExtra("deadName");
        cm1ID = getIntent().getStringExtra("cm1ID");
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), deadName);
        
        tributeImage = (ImageView)findViewById(R.id.cyber_image);
        listview = (ListView)findViewById(R.id.listview);
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        
        movieButton = (ImageButton)findViewById(R.id.movieButton);
        photoButton = (ImageButton)findViewById(R.id.photoButton);
        manageButton = (ImageButton)findViewById(R.id.manageButton);
        tributeMessageButton = (ImageButton)findViewById(R.id.tributeMessageButton);
        
        LinearLayout memoryTitleLayout = (LinearLayout)findViewById(R.id.memoryTitleLayout);
        memoryTitleLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(thisContext, Webview.class);
				i.putExtra("url", "http://www.cyberyoungrak.or.kr/cms/cyber/cyber_memory2_list_mobile2.php?cm1_id2="+cm1ID);
				i.putExtra("title", "추모의글");
				startActivity(i);
			}
		});
        
      //?cm1_id2=3266
        new ArticleListTask().execute("1");
        
        movieButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(StringUtils.isEmptyOrBlank(tributeMovie)){
					DialogUtils.alert(thisContext, "", "동영상이 존재하지 않습니다");
				} else {
					Intent i = new Intent(thisContext, MovieView.class);
					i.putExtra("movieFile", tributeMovie);
					startActivity(i);
				}
				
			}
		});
        
        photoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(thisContext, TributeAlbum.class);
				i.putExtra("cm1ID", cm1ID);
				i.putExtra("deadName", deadName);
				startActivity(i);
			}
		});
        
        manageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(appEx.isLogin()){
					
					if(tributeRoom.getMemID().equals(appEx.getUserID())){
						Intent i = new Intent(thisContext, TributeManage.class);
						i.putExtra("cm1ID", cm1ID);
						i.putExtra("deadName", deadName);
						i.putExtra("currImageUrl", tributeRoom.getCm1Image());
						startActivity(i);
					} else {
						DialogUtils.alert(thisContext, "", "개설자만 수정 할 있습니다");
					}
				} else {
					DialogUtils.alert(thisContext, "", "로그인 후에 이용해 주세요");
				}
			}
		});
        
        tributeMessageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(thisContext, TributeMessage.class);
				i.putExtra("cm1id",cm1ID);
				startActivityForResult(i,1);
				
			}
		});
        
        listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> args0, View view, int position, long id) {
				
				/*
				Article a = listData.get(position);
				
				Intent i = new Intent(thisContext, TributeMessageView.class);
				i.putExtra("article", a);
				startActivity(i);
				*/
				Intent i = new Intent(thisContext, Webview.class);
				i.putExtra("url", "http://www.cyberyoungrak.or.kr/cms/cyber/cyber_memory2_list_mobile2.php?cm1_id2="+cm1ID);
				i.putExtra("title", "추모의글");
				startActivity(i);
			}
        });
	}
	
	
	private View.OnClickListener buttonListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == movieButton.getId()){
				
			} else if(v.getId() == photoButton.getId()){
				
			} else if(v.getId() == manageButton.getId()){
				
			}
		}
	};
	
	private Runnable showDialog = new Runnable() {
		@Override
		public void run(){
			DialogUtils.alert(thisContext, "", mMessage);
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("TributeMessage", "ResultCode="+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		
		
	    if (resultCode == RESULT_OK) {
	        if (requestCode == 1) {
	        	Log.d("TributeMessage", "requestCode="+requestCode);
	        	String result = data.getStringExtra("result"); 
	        	if(result != null){
	        		if(result.equals("success")){
	        			new ArticleListTask().execute("1");
	        		}
	        	}
	        	
	        }
	    }
	    
	}
	
	private void requestMemoryList(String type){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String url = "";
			if(type.equals("3")){
				url = Constant.MEMORY3_LIST_URL + "?cm1_id2="+cm1ID;
			} else {
				url = Constant.MEMORY_LIST_URL + "?cm1_id2="+cm1ID;
			}
			
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), url, "GET", null, -1);
			
			Log.d(TAG, result);
    		
			
    		JSONArray data = new JSONArray(result);
    		
    		listData = new ArrayList<Article>();
    		
    		for(int i=0;i<data.length();i++){
    			JSONObject o = (JSONObject)data.get(i);
    			listData.add(Article.bindJSONObject(o));
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
	
	private void requestTribute(){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String url = "";
			
			url = Constant.TRIBUTE_ROOM_URL + "?cm1_id2="+cm1ID;
			
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), url, "GET", null, -1);
			
			Log.d(TAG, result);
    		
			
    		JSONObject data = new JSONObject(result);
    		tributeRoom = TributeRoom.bindJSONObject(data);
    		
    		
    		
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
	
	private void requestTributeMovie(){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String url = "";
			
			url = Constant.TRIBUTE_MOVIE_URL + "?cm1_id="+cm1ID;
			
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), url, "GET", null, -1);
			
			Log.d(TAG, result);
    		
			
    		JSONObject data = new JSONObject(result);
    		tributeMovie = data.getString("movie_file");
    		
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
	
	
	private class ArticleListTask extends AsyncTask<String, Integer, Integer> {
 		
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
 		}
 		
 		@Override
 		protected Integer doInBackground(String... args) {

 			String type = args[0];
 			requestMemoryList(type);
 			
 			requestTribute();
 			
 			requestTributeMovie();
 			
 			return 0;
 		}
 		
 		protected void onPostExecute(Integer result) {
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			if(listData != null) {
 				Log.d(TAG, "mInterviewData not null length = "+listData.size());
 	        	adapter = new ArticleAdapter(thisContext, R.layout.article_list_row, listData);
 	        
 	        	listview.setAdapter(adapter);
 	        	
 	        	if(tributeRoom != null){
 	        		Log.d(TAG, "tributeRoom not null");
 	        		if(!StringUtils.isEmptyOrBlank(tributeRoom.getCm1Image())){
 	        			Log.d(TAG, "cm1Image not null "+tributeRoom.getCm1Image());
 	        			new ImageLoadTask().execute(tributeRoom.getCm1Image());
 	        		}
 	        	}
 	        	
 	        	if(!StringUtils.isEmptyOrBlank(tributeMovie)) {
 	        		
 	        	}
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
 	
 	private class ArticleAdapter extends ArrayAdapter<Article> {
 		
 		private Context context;
 		private List<Article> itemList;
 		private int viewId;
 		
 		public ArticleAdapter(Context context, int textViewResourceId, List<Article> itemList) {
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
 				
 				final Article item = itemList.get(position);
 				TextView title = (TextView) v.findViewById(R.id.title);
 				TextView writer = (TextView) v.findViewById(R.id.writer);
 				TextView writeDate = (TextView) v.findViewById(R.id.write_date);
 				
 				if(title != null){
 					title.setText(item.getTitle());
 				}
 				if(writer != null){
 					writer.setText(item.getName());
 				}
 				if(writeDate != null){
 					writeDate.setText(item.getWriteDate());
 				}
 				
 			}
 			return v;
 		}
 	}
 	
 	private class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {
	 		
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
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
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			tributeImage.setImageBitmap(result);
 			
 		}
 		
 		@Override
 		protected void onCancelled() {
 			// TODO Auto-generated method stub
 			super.onCancelled();
 		}
 	
 	}
}

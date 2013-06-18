package com.youngrak;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.youngrak.common.Constant;
import com.youngrak.common.HttpDataHelper;
import com.youngrak.common.StringUtils;
import com.youngrak.common.dialog.DialogUtils;
import com.youngrak.model.Article;
import com.youngrak.model.TributeRoom;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TributeMessageView extends BaseActivity {
	
	String cm1Id2;
	Article a;
	ProgressBar pageLoading = null;
	ApplicationEx app;
	static final String TAG = "TributeMessageView";
	String mMessage;
	TextView title, content;
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_message_view);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "추모글보기");
        
        app = (ApplicationEx)this.getApplication();
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        a = (Article)getIntent().getSerializableExtra("article");
        
        title = (TextView)findViewById(R.id.messageTitle);
        content = (TextView)findViewById(R.id.messageContent);
        
        new ArticleListTask().execute();
	}
	
	private Runnable showDialog = new Runnable() {
		@Override
		public void run(){
			DialogUtils.alert(thisContext, "", mMessage);
		}
	};
	
	private class ArticleListTask extends AsyncTask<String, Integer, Integer> {
 		
		JSONObject data;
		
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
 		}
 		
 		@Override
 		protected Integer doInBackground(String... args) {

 			try{
 				
 				String url = "";
 				
 				url = Constant.TRIBUTE_MESSAGEURL + "?cm2_id="+a.getCm1ID()+"&cm2_id="+a.getCm2ID();
 				
 				String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), url, "GET", null, -1);
 				
 	    		data = new JSONObject(result);
 	    		
 	    		
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
 			
 			
 			
 			return 0;
 		}
 		
 		protected void onPostExecute(Integer result) {
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			if(data != null){
 				try {
					title.setText(data.getString("cm2_title"));
					content.setText(data.getString("cm2_contents"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 		        
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
}

package com.youngrak;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.youngrak.common.Constant;
import com.youngrak.common.HttpDataHelper;
import com.youngrak.common.dialog.DialogUtils;
import com.youngrak.model.Tomb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UserInfo extends BaseActivity {
	
	Tomb tomb;
	String mMessage;
	static final String TAG = "UserInfo";
	ProgressBar pageLoading = null;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.user_info);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "����ã��");
        
        tomb = (Tomb)getIntent().getSerializableExtra("tomb");
        
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        TextView userName = (TextView)findViewById(R.id.userName);
        ImageButton infoButton1 = (ImageButton)findViewById(R.id.infoButton1);
        ImageButton infoButton2 = (ImageButton)findViewById(R.id.infoButton2);
        TextView userSex = (TextView)findViewById(R.id.userSex);
        TextView buryDate = (TextView)findViewById(R.id.buryDate);
        TextView areaType = (TextView)findViewById(R.id.areaType);
        TextView payName = (TextView)findViewById(R.id.payName);
        TextView deadID = (TextView)findViewById(R.id.deadID);
        TextView deadDate = (TextView)findViewById(R.id.deadDate);
        
        userName.setText(tomb.getDeadName());
        userSex.setText(tomb.getDeadSex());
        buryDate.setText(tomb.getBuryDate());
        areaType.setText(tomb.getAreaType());
        deadID.setText(tomb.getBuryNo());
        payName.setText(tomb.getPayName());
        deadDate.setText(tomb.getDeadDate());
        
        infoButton1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(thisContext, MapInfo.class);
				startActivity(i);
				
			}
		});
        
        infoButton2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(appEx.isLogin())
					new RequestTributeTask().execute();
				else {
					DialogUtils.alert(thisContext, "", "�α��� �Ŀ� �̿��� �ּ���");
				}
			}
		});
	}
	
	private Runnable showDialog = new Runnable() {
		@Override
		public void run(){
			DialogUtils.alert(thisContext, "", mMessage);
		}
	};
	
	private String requestTribute(){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String query = "?ht_id=cyber_04_1&dead_id="+tomb.getDeadID()+"&mode=ins&check_type="+tomb.getCheckType()+"&area_type="+tomb.getAreaType()+"&ss_mem_id="+appEx.getUserID();
			String url = Constant.TRIBUTE_INSERT_URL + query;
			Log.d(TAG, "url="+url);
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), url, "GET", null, -1);
			
			Log.d(TAG, result);
    		
			
    		JSONObject data = new JSONObject(result);
    		
    		if(data.getString("result") != null){
    			if(data.getString("result").equals("success")){
    				return "success";
    			} else {
    				mMessage = data.getString("message");
    	        	thisHandler.post(showDialog);
    	        	return "fail";
    			}
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
		
		return "fail";
	}
	
	private class RequestTributeTask extends AsyncTask<String, Void, String> {
 		
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
 		}
 		
 		@Override
 		protected String doInBackground(String... args) {

 			return requestTribute();
 			
 		}
 		
 		protected void onPostExecute(String result) {
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			if(result.equals("success")){
 				Intent i = new Intent(thisContext, MyTributeRoom.class);
 	 			startActivity(i);
 			} else {
 			
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

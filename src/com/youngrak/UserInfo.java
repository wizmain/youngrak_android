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
	String checkType = "2";
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.user_info);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "고인찾기");
        
        tomb = (Tomb)getIntent().getSerializableExtra("tomb");
        checkType = getIntent().getStringExtra("check_type");
        
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
        
        if(checkType.equals("1")){
        	infoButton1.setVisibility(View.GONE);
        }
        
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
				//12위, 1위, 4위, 6위, 개나리, 개나리6기, 무궁화, 철쭉, 2추모관, 1추모관
				
				String mapInfo = "";
				if(tomb.getSType().equals("개나리")){
					mapInfo = "http://www.cyberyoungrak.or.kr/map6";
				} else if(tomb.getSType().equals("철쭉")){
					mapInfo = "http://www.cyberyoungrak.or.kr/map1";
				} else if(tomb.getSType().equals("무궁화")){
					//mapInfo = "http://www.cyberyoungrak.or.kr/map1";
				} else if(tomb.getSType().equals("12위")){
					mapInfo = "http://www.cyberyoungrak.or.kr/map4";
				} else if(tomb.getSType().equals("1위")){
					
				} else if(tomb.getSType().equals("4위")){
					
				} else if(tomb.getSType().equals("6위")){
					mapInfo = "http://www.cyberyoungrak.or.kr/map3";
				} else if(tomb.getSType().equals("개나리6기")){
					mapInfo = "http://www.cyberyoungrak.or.kr/map6";
				} else if(tomb.getSType().equals("1추모관")){
					mapInfo = "http://www.cyberyoungrak.or.kr/map9";
				} else if(tomb.getSType().equals("2추모관")){
					mapInfo = "http://www.cyberyoungrak.or.kr/map10";
				}
				
				Log.d(TAG, "tomb.getSType()="+tomb.getSType() +" mapInfo="+mapInfo);
				
				Intent i = new Intent(thisContext, MapInfo.class);
				i.putExtra("mapinfo", mapInfo);
				startActivity(i);
				
			}
		});
        
        infoButton2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(appEx.isLogin())
					new RequestTributeTask().execute();
				else {
					DialogUtils.alert(thisContext, "", "로그인 후에 이용해 주세요");
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

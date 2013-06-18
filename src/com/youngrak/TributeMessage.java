package com.youngrak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.youngrak.common.Constant;
import com.youngrak.common.HttpDataHelper;
import com.youngrak.common.StringUtils;
import com.youngrak.common.dialog.DialogUtils;
import com.youngrak.model.TributeRoom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class TributeMessage extends BaseActivity {
	
	EditText title;
	EditText content;
	String cm1Id2;
	static final String TAG = "TributeMessage";
	String mMessage;
	ProgressBar pageLoading = null;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.tribute_message);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "추모글쓰기");
        
        cm1Id2 = getIntent().getStringExtra("cm1id");
        
        title = (EditText)findViewById(R.id.messageTitle);
        content = (EditText)findViewById(R.id.messageContent);
        
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        ImageButton regButton = (ImageButton)findViewById(R.id.registerButton);
        
        regButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(StringUtils.isEmptyOrBlank(title.getText().toString())){
					DialogUtils.alert(thisContext, "", "제목을 입력해 주세요");
					return;
				}
				
				if(StringUtils.isEmptyOrBlank(content.getText().toString())){
					DialogUtils.alert(thisContext, "", "내용을 입력해 주세요");
					return;
				}
				
				new PageLoadTask().execute();
				
			}
		});
        
	}
	
	private Runnable showDialog = new Runnable() {
		@Override
		public void run(){
			DialogUtils.alert(thisContext, "", mMessage);
		}
	};
	
	private String sendMessage(){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String url = Constant.TRIBUTE_MESSAGE_INS_URL;
			
			
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("cm1_id2", cm1Id2));
			parameters.add(new BasicNameValuePair("mode", "ins"));
			parameters.add(new BasicNameValuePair("cm2_title", title.getText().toString()));
			parameters.add(new BasicNameValuePair("cm2_contents", content.getText().toString()));
			
			
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), url, "POST", parameters, -1);
			Log.d(TAG, result);
			
    		JSONObject data = new JSONObject(result);
    		
    		if(data.getString("result") != null){
    			mMessage = data.getString("message");
        		thisHandler.post(showDialog);
    			return data.getString("result");
    			
    		} else {
    			return "fail";
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
	
	// AsyncTask클래스는 항상 Subclassing 해서 사용 해야 함.
 	// 사용 자료형은
 	// background 작업에 사용할 data의 자료형: String 형
 	// background 작업 진행 표시를 위해 사용할 인자: Integer형
 	// background 작업의 결과를 표현할 자료형: Long
 	private class PageLoadTask extends AsyncTask<String, Integer, String> {
 		
 		// 이곳에 포함된 code는 AsyncTask가 execute 되자 마자 UI 스레드에서 실행됨.
 		// 작업 시작을 UI에 표현하거나
 		// background 작업을 위한 ProgressBar를 보여 주는 등의 코드를 작성.
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
 		}
 		
 		// UI 스레드에서 AsynchTask객체.execute(...) 명령으로 실행되는 callback
 		@Override
 		protected String doInBackground(String... args) {

 			return sendMessage();
 			
 		}
 		
 		// onInBackground(...)가 완료되면 자동으로 실행되는 callback
 		// 이곳에서 onInBackground가 리턴한 정보를 UI위젯에 표시 하는 등의 작업을 수행함.
 		// (예제에서는 작업에 걸린 총 시간을 UI위젯 중 TextView에 표시함)
 		protected void onPostExecute(String result) {
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			Intent i = new Intent();
 			if(result.equals("success")){
 				
 				i.putExtra("result", "success");
 				
 			} else {
 				i.putExtra("result", "fail");
 			}
 			setResult(RESULT_OK, i);
 			finish();
    		
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
}

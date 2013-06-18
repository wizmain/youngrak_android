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
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "�߸�۾���");
        
        cm1Id2 = getIntent().getStringExtra("cm1id");
        
        title = (EditText)findViewById(R.id.messageTitle);
        content = (EditText)findViewById(R.id.messageContent);
        
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        ImageButton regButton = (ImageButton)findViewById(R.id.registerButton);
        
        regButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(StringUtils.isEmptyOrBlank(title.getText().toString())){
					DialogUtils.alert(thisContext, "", "������ �Է��� �ּ���");
					return;
				}
				
				if(StringUtils.isEmptyOrBlank(content.getText().toString())){
					DialogUtils.alert(thisContext, "", "������ �Է��� �ּ���");
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
	
	// AsyncTaskŬ������ �׻� Subclassing �ؼ� ��� �ؾ� ��.
 	// ��� �ڷ�����
 	// background �۾��� ����� data�� �ڷ���: String ��
 	// background �۾� ���� ǥ�ø� ���� ����� ����: Integer��
 	// background �۾��� ����� ǥ���� �ڷ���: Long
 	private class PageLoadTask extends AsyncTask<String, Integer, String> {
 		
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
 		protected String doInBackground(String... args) {

 			return sendMessage();
 			
 		}
 		
 		// onInBackground(...)�� �Ϸ�Ǹ� �ڵ����� ����Ǵ� callback
 		// �̰����� onInBackground�� ������ ������ UI������ ǥ�� �ϴ� ���� �۾��� ������.
 		// (���������� �۾��� �ɸ� �� �ð��� UI���� �� TextView�� ǥ����)
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
}

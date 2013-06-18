package com.youngrak;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.youngrak.common.Constant;
import com.youngrak.common.HttpDataHelper;
import com.youngrak.common.StringUtils;
import com.youngrak.common.dialog.DialogUtils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Login extends Activity {
	private String TAG = "Login";
	private HttpClient httpClient;
	private ProgressDialog loginProgressDialog;
	private String loginFailMsg;
	EditText loginID = null;
	EditText loginPW = null;
	TextView versionText = null;
	CheckBox autoLogin = null;
	ApplicationEx appEx = null;
	Context thisContext = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        appEx = (ApplicationEx)this.getApplication();
        thisContext = this;
        
        loginID = (EditText)findViewById(R.id.loginID);
        loginPW = (EditText)findViewById(R.id.loginPW);
        
        ImageButton loginButton = (ImageButton)findViewById(R.id.loginButton);
        httpClient = appEx.getHttpClient();
        
        CookieSyncManager.createInstance(this);
        CookieSyncManager mgr = CookieSyncManager.getInstance();
		CookieManager cm = CookieManager.getInstance();
		
		String cookie = cm.getCookie(Constant.SERVER_ADDR);
        if(StringUtils.isEmptyOrBlank(cookie)){
        	
        } else {
			
        }
        
        
        loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "LoginButtonClicked");
				
				String userID = loginID.getText().toString().trim();
	    		String passwd = loginPW.getText().toString().trim();
	    		
	    		if(StringUtils.isEmptyOrBlank(userID)){
	    			DialogUtils.alert(Login.this, "Error", "아이디를 입력해 주세요");
	    			return;
	    		}
	    		
	    		if(StringUtils.isEmptyOrBlank(passwd)){
	    			DialogUtils.alert(Login.this, "", "비밀번호를 입력해 주세요");
	    			return;
	    		}
				
	    		
				loginProgressDialog = ProgressDialog.show(thisContext, "", "로그인중입니다");
				new Thread(loginThread).start();
	    		
	    		//new LoginTask().execute();
			}
		});
	}
	
	private Runnable loginThread = new Runnable(){
    	@Override
    	public void run(){
    		
    		loginProcess();
    	}
    };
    
    Handler handler = new Handler();
	
	private Runnable updateMsg = new Runnable(){
    	public void run(){
    		clearLoadingAndMsg();
    	}
    };
    
    private void clearLoadingAndMsg(){
    	if(loginProgressDialog != null)
    		loginProgressDialog.dismiss();
		//DialogUtils.alert(thisContext, "", loginFailMsg);
		
		new AlertDialog.Builder(Login.this)
		.setTitle("로그인")
		.setMessage(loginFailMsg)
		.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		}).show();
		
    }
    
    private void loginProcess() {
    	
    	
    	String userID = "";
    	String passwd = "";
    	
    	try{
    		//로그인정보 아이디/ 패스워드 저장
    		userID = loginID.getText().toString().trim();
    		passwd = loginPW.getText().toString().trim();
    		
    		if(StringUtils.isEmptyOrBlank(userID)){
    			loginFailMsg = "아이디를 입력해 주세요";
    			handler.post(updateMsg);
    			return;
    		}
    		
    		if(StringUtils.isEmptyOrBlank(passwd)){
    			loginFailMsg = "비밀번호를 입력해 주세요";
    			handler.post(updateMsg);
    			return;
    		}
    		
    		//SharedPreferences savePref = getSharedPreferences("savedLogin", MODE_PRIVATE);
    		//SharedPreferences.Editor prefEditor = savePref.edit();
    		//prefEditor.putString("userID", userID);//아이디는기본저장해둠
    		
    		/*
    		if(mChkSaveID.isChecked()){
    			prefEditor.putBoolean("chkSaveID", true);
    		} else {
    			prefEditor.putBoolean("chkSaveID", false);
    		}
    		*/
    		/*
    		if(autoLogin.isChecked()){
    			Log.d(TAG, "autoLoginChecked");
    			prefEditor.putBoolean("chkAutoLogin", true);
    			prefEditor.putString("password", passwd);
    			Log.d(TAG, "passwd = " + passwd);
    		} else {
    			prefEditor.putBoolean("chkAutoLogin", false);
    			prefEditor.putString("password", "");
    		}
    		prefEditor.commit();
    		*/
    		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    		//Post 파라메터 설정
	        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	        postParameters.add(new BasicNameValuePair("mb_id", userID));
	        postParameters.add(new BasicNameValuePair("mb_password", passwd));
	        
	        String result = HttpDataHelper.getHttpJsonData(httpClient, Constant.LOGIN_URL, "POST", postParameters, -1);
	        
	        //String result = HttpDataHelper.getHttpJsonData(httpClient, Constant.SERVER_ADDR_HTTPS, Constant.LOGIN_URL, "POST", postParameters, -1);
	        Log.d(TAG, result);
	        
	        try{
	        	JSONObject jsonResult = new JSONObject(result);
	        	//JSONObject loginResult = new JSONObject(result);
	        	JSONObject loginResult = jsonResult.getJSONObject("userSession");
	        	//Log.d(TAG, loginResult.getString("result"));
	        	
	        	if(loginResult.getString("result").equals("success")){
	        		appEx.setLoginCookieName(loginResult.getString("login_c"));
	        		appEx.setUserID(userID);
	        		CookieSyncManager mgr = CookieSyncManager.getInstance();
	        		
	        		CookieManager cm = CookieManager.getInstance();
	        		cm.setAcceptCookie(true);
	        		
	        		List<Cookie> cookies = ((DefaultHttpClient)httpClient).getCookieStore().getCookies();
	        		
	        		if(!cookies.isEmpty()){
	        			
	        			cm.removeSessionCookie();
	        			try{
	        				Thread.sleep(1000);
	        			}catch(Exception e){}
	        			
	        			String cookieString = "";
	        			
	        			for(int i=0;i<cookies.size();i++){
	        				Cookie cookie = cookies.get(i);
	        				Cookie sessionCookie = cookie;
	        				if(sessionCookie != null){
	        					cookieString += sessionCookie.getName() + "=" + sessionCookie.getValue() + ";";
	        					if(i==(cookies.size() - 1)){
	        						cookieString += "domain=" + sessionCookie.getDomain();
	        					}
	        				}
	        			}
	        			Log.d(TAG, cookieString);
	        			cm.setCookie(Constant.SERVER_ADDR, cookieString);
	        			mgr.startSync();
	        			
	        			
	        		}
	        		
	        		if(loginProgressDialog != null)
        	        	loginProgressDialog.dismiss();
	        		
        	        Intent i = new Intent(Login.this, Home.class);
        	        startActivity(i);
        	        
        	        finish();
	        		
	        	} else {
	        		loginFailMsg = loginResult.getString("message");
	        		
	        		if(loginProgressDialog != null)
	    	        	loginProgressDialog.dismiss();
	        		
	        		Log.d(TAG, "loginFail1");
	        		handler.post(updateMsg);
	        		
	        		
	        	}
	        	
	        } catch(JSONException e) {
	        	e.printStackTrace();
	        	Log.e(TAG, e.getMessage());
	        	//loginFailMsg = e.getMessage();
	        	//Log.d(TAG, "LoginFail2");
	        	loginFailMsg = "JSON 오류가 발생했습니다";
        		handler.post(updateMsg);
	        }
	        
    	} catch(ClientProtocolException e) {
    		e.printStackTrace();
    		//Log.e(TAG, e.getMessage());
    		loginFailMsg = "서버에 연결할 수 없습니다";
    		handler.post(updateMsg);
    		
    	} catch(IOException e) {
    		//Log.e(TAG, e.getMessage());
    		e.printStackTrace();
    		loginFailMsg = "서버에 연결할 수 없습니다";
    		handler.post(updateMsg);
    		
    	} catch(Exception e) {
    		//Log.e(TAG, e.getMessage());
    		e.printStackTrace();
    		Log.d(TAG, "LoginFail3");
    		//loginFailMsg = e.getMessage();
    		loginFailMsg = "오류발생";
    		handler.post(updateMsg);
    		
    	} finally {
    		//handler.post(updateMsg);
    		//finish();
    		if(loginProgressDialog != null)
	        	loginProgressDialog.dismiss();
    	}
        
    }
    
    
}

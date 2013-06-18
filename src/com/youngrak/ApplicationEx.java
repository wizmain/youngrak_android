package com.youngrak;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import com.youngrak.R;
import com.youngrak.common.StringUtils;

/**
 * 애플리케이션 시작시 시작되어 항상 로딩되어 있
 * @author wizmain
 *
 */
public class ApplicationEx extends Application {
	
	private static final String TAG = "ApplicationEx";
	private HttpClient httpClient;
	private Object mLock = new Object();
	private CookieStore mCookie = null;
	private int userNo = -1;
	private String userID = "";
	private String version = "";
	private String loginCookieName = "";
	private int backPressCount = 0;
	private long recentBackPressTime = 0;
	private static Context context;
	@Override
	public void onCreate(){
		super.onCreate();
		ApplicationEx.context = getApplicationContext();
		httpClient = createHttpClient();
		
		recentBackPressTime = Calendar.getInstance().getTimeInMillis();
		
	}
	
	@Override
	public void onLowMemory(){
		super.onLowMemory();
		shutdownHttpClient();
	}
	
	@Override
	public void onTerminate(){
		super.onTerminate();
		shutdownHttpClient();
	}
	
	//http  client 생성 
	private HttpClient createHttpClient(){
		Log.d(TAG,"createHttpClient()...");
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(),443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		
		return new DefaultHttpClient(conMgr, params);
	}
	
	public HttpClient getHttpClient(){
		synchronized (mLock) {
            if (mCookie == null) {
                    mCookie = ((DefaultHttpClient) httpClient).getCookieStore();
            } else {
                    ((DefaultHttpClient) httpClient).setCookieStore(mCookie);
            }
		}
		
		return httpClient;
	}
	
	//http연결 닫기
	private void shutdownHttpClient(){
		
		try{
			if(httpClient != null){
				if(httpClient.getConnectionManager() != null){
					httpClient.getConnectionManager().shutdown();				
				}
			}
		}catch(Exception e){
			//Log.e(TAG, e.getStackTrace());
			e.printStackTrace();
		}
		
	}
	
	public boolean isLogin(){
		CookieStore cookieStore = getCookieStore();
		
		if(cookieStore == null) {
			return false;
		} else {
			if(getCookieValue(loginCookieName)==null){
				return false;
			} else {
				return true;
			}
		}
	}
	
	//쿠키 정보
	public CookieStore getCookieStore(){
		if (mCookie == null) {
            mCookie = ((DefaultHttpClient) httpClient).getCookieStore();
	    }
		
		return mCookie;
	}
	
	//쿠키 정보가져오기
	public String getCookieValue(String cookieName){
		
		CookieStore cookieStore = getCookieStore();
		if(cookieStore != null){
		    List<Cookie> cookieList = cookieStore.getCookies();
		    String cookieValue = null;
		    
		    if(cookieList != null){
		        for(Cookie c:cookieList){
		        	if(c.getName().equals(cookieName)){
		        		
		        		/*
		        		if(c.getName().equals("userKind") || c.getName().equals("username")){
			        		try {
			        			cookieValue = URLDecoder.decode(c.getValue(), "UTF-8");
							} catch (UnsupportedEncodingException e) {
								
								e.printStackTrace();
								return null;
							}
			        	} else {
			        		cookieValue = c.getValue();
			        	}
			        	*/
		        		//cookieValue = StringUtils.getBase64decode(c.getValue());
		        		cookieValue = c.getValue();
		        	}
		        }
		    }
		    return cookieValue;
		} else {
			return null;
		}    
	}
	
	//현재 네트웍 연결 상황 
	public String getNetworkState(){
		ConnectivityManager manager = 
		         (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		if(is3g){
			return "3G";
		} else if(isWifi){
			return "wifi";
		} else {
			return "";
		}
	}
	
	//버전 텍스트
	public String getVersion() {
		return version;
	}
	
	//버전 설정
	public void setVersion(String version) {
		this.version = version;
	}
	
	//로그인 상태인지
	public boolean isAuthenticated(){
		
		String userID = getCookieValue(loginCookieName);
		if(userID != null){
			return true;
		}
		return false;
	}
	
	public int addBackPressCount(){
		return backPressCount++;
	}

	public int getBackPressCount() {
		return backPressCount;
	}

	public void setBackPressCount(int backPressCount) {
		this.backPressCount = backPressCount;
	}

	public long getRecentBackPressTime() {
		return recentBackPressTime;
	}

	public void setRecentBackPressTime(long recentBackPressTime) {
		this.recentBackPressTime = recentBackPressTime;
	}
	
	public String getUserID(){
		//return getCookieValue(loginCookieName);
		return this.userID;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	
	public int getUserNo(){
		return StringUtils.toInteger(getCookieValue("userUid"), -1);
	}
	
	public static Context getAppContext() {
		return ApplicationEx.context;
	}
	
	public void setLoginCookieName(String name){
		this.loginCookieName = name;
	}
	
	public String getLoginCookieName(){
		return loginCookieName;
	}
	
}

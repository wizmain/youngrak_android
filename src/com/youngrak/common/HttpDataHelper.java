package com.youngrak.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.HTTP;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class HttpDataHelper {
	
	private static final String TAG = "HttpDataHelper";
	private static final int HTTP_CONNECTION_TIMEOUT = 10000;
	
	public HttpDataHelper(){
		
	}
	
	public static String getHttpJsonData(HttpClient httpClient, String url, String httpMethod, List<NameValuePair> parameters, int timeOut) 
		throws ClientProtocolException, IOException{
		
		return getHttpJsonData(httpClient, Constant.SERVER_ADDR, url, httpMethod, parameters, timeOut);
        
	}
	
	public static String getHttpJsonData(HttpClient httpClient, String serverAddr, String url, String httpMethod, List<NameValuePair> parameters, int timeOut) 
			throws ClientProtocolException, IOException{
		HttpResponse response = getHttpResponse(httpClient, serverAddr, url, httpMethod, parameters, timeOut);
		Log.d(TAG, "getHttpJsonData = " + url);
        
		BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.seperator");
        while((line = in.readLine()) != null){
        	sb.append(line + NL);
        }
        in.close();
        
        return sb.toString();
	}
	
	public static HttpResponse getHttpResponse(HttpClient httpClient, String serverAddr, String url, String httpMethod, List<NameValuePair> parameters, int timeOut) 
			throws ClientProtocolException, IOException{
		Log.d(TAG, "getHttpResponse() by server"+serverAddr+url);
		
		HttpResponse response = null;
		
		if(timeOut > 1000){
	    	httpClient.getParams().setParameter("http.protocol.expect-continue", false);
	    	httpClient.getParams().setParameter("http.connection.timeout", timeOut);
	    	httpClient.getParams().setParameter("http.socket.timeout", timeOut);
		} else {
			httpClient.getParams().setParameter("http.protocol.expect-continue", false);
	    	httpClient.getParams().setParameter("http.connection.timeout", HTTP_CONNECTION_TIMEOUT);
	    	httpClient.getParams().setParameter("http.socket.timeout", HTTP_CONNECTION_TIMEOUT);
		}
		
		
		
		if(httpMethod.equals("POST")){
			
			HttpPost request = new HttpPost(serverAddr + url);
			if(parameters != null){
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
				request.setEntity(formEntity);
			}
			
			response = httpClient.execute(request);
			    		
		} else {
			HttpGet request = new HttpGet(serverAddr + url);
			response = httpClient.execute(request);
		}
	    
	    return response;
	}
	
	public static HttpResponse getHttpResponse(HttpClient httpClient, String url, String httpMethod, List<NameValuePair> parameters, int timeOut) 
		throws ClientProtocolException, IOException{
		
		return getHttpResponse(httpClient, Constant.SERVER_ADDR, url, httpMethod, parameters, timeOut);
	    
	}
	
	/**
	 * url 주소의 이미지 얻기
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpImageBitmap(String url){
		
		URL imageUrl = null;
		HttpURLConnection conn = null;
		Bitmap bm = null;
		BufferedInputStream bis = null;
		
		try {
			imageUrl = new URL(Constant.SERVER_ADDR + url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.d(TAG, "Invalid Url");
		}
		
		
		try {
			conn = (HttpURLConnection)imageUrl.openConnection();
			
			bis = new BufferedInputStream(conn.getInputStream(), 10240);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "서버에 연결할 수 없습니다");
		} finally {
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return bm;
	}
	
	
	
	
}

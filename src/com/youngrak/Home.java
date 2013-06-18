package com.youngrak;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.youngrak.R;
import com.youngrak.common.Constant;
import com.youngrak.common.DateTimeUtil;
import com.youngrak.common.HttpDataHelper;
import com.youngrak.model.Article;
import com.youngrak.model.Tomb;

public class Home extends BaseActivity {
	
	private final static String TAG = "Home";
	Button button1, button2, button3, button4, button5, button6;
	ImageButton loginButton;
	ArrayList<Article> rssList;
	ProgressBar pageLoading = null;
	TextView letterTitle1, letterTitle2, letterTitle3, letterDate1, letterDate2, letterDate3;
	/**
     * 
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.home);
        
        button1 = (Button)findViewById(R.id.mainButton1);
        button2 = (Button)findViewById(R.id.mainButton2);
        button3 = (Button)findViewById(R.id.mainButton3);
        button4 = (Button)findViewById(R.id.mainButton4);
        button5 = (Button)findViewById(R.id.mainButton5);
        button6 = (Button)findViewById(R.id.mainButton6);
        
        button1.setOnClickListener(buttonClickListener);
        button2.setOnClickListener(buttonClickListener);
        button3.setOnClickListener(buttonClickListener);
        button4.setOnClickListener(buttonClickListener);
        button5.setOnClickListener(buttonClickListener);
        button6.setOnClickListener(buttonClickListener);
        
        letterTitle1 = (TextView)findViewById(R.id.letterTitle1);
        letterTitle2 = (TextView)findViewById(R.id.letterTitle2);
        letterTitle3 = (TextView)findViewById(R.id.letterTitle3);
        letterDate1 = (TextView)findViewById(R.id.letterDate1);
        letterDate2 = (TextView)findViewById(R.id.letterDate2);
        letterDate3 = (TextView)findViewById(R.id.letterDate3);
        
        TableLayout letterTable = (TableLayout)findViewById(R.id.letterTable);
        letterTable.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(thisContext, Webview.class);
				i.putExtra("url", "http://www.cyberyoungrak.or.kr/cms/bbs/board_mobile.php?dk_table=cyber_04_4");
				i.putExtra("title", "하늘나라우체국편지");
				startActivity(i);
			}
		});
        
        loginButton= (ImageButton)findViewById(R.id.loginButton);
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        
        if(appEx.isLogin()){
    		Log.d(TAG, "login state");
        	loginButton.setImageResource(R.drawable.logout_btn);
        } else {
        	Log.d(TAG, "login not state");
        }
        
        loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(appEx.isLogin()){
					new LogoutTask().execute();
				} else {
					Intent i = new Intent(thisContext, Login.class);
					startActivity(i);
				}
			}
		});
        
        new PageLoadTask().execute();
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	Log.d(TAG, "onResume");
    	if(appEx.isLogin()){
    		Log.d(TAG, "login state");
        	loginButton.setImageResource(R.drawable.logout_btn);
        }
    }
    
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == button1.getId()){
				Intent i = new Intent(thisContext, Introduce1.class);
				startActivity(i);
			} else if(v.getId() == button2.getId()){
				Intent i = new Intent(thisContext, UserSearch.class);
				startActivity(i);
			} else if(v.getId() == button3.getId()){
				Intent i = new Intent(thisContext, Introduce3.class);
				startActivity(i);
			} else if(v.getId() == button4.getId()){
				Intent i = new Intent(thisContext, Introduce4.class);
				startActivity(i);
			} else if(v.getId() == button5.getId()){
				Intent i = new Intent(thisContext, Introduce5.class);
				startActivity(i);
			} else if(v.getId() == button6.getId()){
				Intent i = new Intent(thisContext, Introduce6.class);
				startActivity(i);
			}
		}
	};
	
	private void logoutProcess(){
		try{
				
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), Constant.LOGOUT_URL, "GET", null, -1);
			
			Log.d(TAG, result);
    		
    		
		} catch(ClientProtocolException e) {
        	e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "서버 접속에 실패했습니다");
    		
    		
    	} catch(IOException e) {
    		e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "서버에 연결할 수 없습니다");
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		//Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(MyLecture.this, "", e.getMessage());
    	}
	}
	
	private class LogoutTask extends AsyncTask<String, Integer, Integer> {
 		
 		// 이곳에 포함된 code는 AsyncTask가 execute 되자 마자 UI 스레드에서 실행됨.
 		// 작업 시작을 UI에 표현하거나
 		// background 작업을 위한 ProgressBar를 보여 주는 등의 코드를 작성.
 		protected void onPreExecute(){
 			super.onPreExecute();
 		}
 		
 		// UI 스레드에서 AsynchTask객체.execute(...) 명령으로 실행되는 callback
 		@Override
 		protected Integer doInBackground(String... args) {

 			logoutProcess();
 			return 0;
 		}
 		
 		// onInBackground(...)가 완료되면 자동으로 실행되는 callback
 		// 이곳에서 onInBackground가 리턴한 정보를 UI위젯에 표시 하는 등의 작업을 수행함.
 		// (예제에서는 작업에 걸린 총 시간을 UI위젯 중 TextView에 표시함)
 		protected void onPostExecute(Integer result) {
 			loginButton.setImageResource(R.drawable.login_btn);
 		}
 		
 		
 		// AsyncTask.cancel(boolean) 메소드가 true 인자로
 		// 실행되면 호출되는 콜백.
 		// background 작업이 취소될때 꼭 해야될 작업은  여기에 구현.
 		@Override
 		protected void onCancelled() {
 			// TODO Auto-generated method stub
 			super.onCancelled();
 			
 		}
 	
 	}
	
	private void requestList(){
    	String url = "";
    	try{
			
    		url = "http://www.cyberyoungrak.or.kr/cms/bbs/rss.php?dk_table=cyber_04_4";
    		
			//ApplicationEx app = (ApplicationEx)this.getApplication();
			
			//String resultString = HttpDataHelper.getHttpJsonData(app.getHttpClient(), "http://www.jobkorea.co.kr", url, "GET", null, -1);
			
			//Log.d(TAG, resultString);
    		rssList = new ArrayList<Article>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream xml = new URL(url).openStream();
            Document doc = builder.parse(xml);
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("item");
            rssList.clear();
            Log.d(TAG, "requestList() list.getLength()="+list.getLength());
            for(int i=0;i<list.getLength();i++) {
            	Node node = list.item(i);
            	
            	if(node.getNodeType() == Node.ELEMENT_NODE) {
            		
            		Element element = (Element)node;
            		NodeList titleNode = element.getElementsByTagName("title");
            		Element titleElement = (Element)titleNode.item(0);
            		NodeList subTitleElement = titleElement.getChildNodes();
            		
            		NodeList linkNode = element.getElementsByTagName("link");
            		Element linkElement = (Element)linkNode.item(0);
            		NodeList subLink = linkElement.getChildNodes();
            		
            		NodeList descriptionNode = element.getElementsByTagName("description");
            		Element descriptionElement = (Element)descriptionNode.item(0);
            		NodeList subDescription = descriptionElement.getChildNodes();
            		
            		NodeList pubDateNode = element.getElementsByTagName("dc:date");
            		Element pubDateElement = (Element)pubDateNode.item(0);
            		NodeList subPubDate = pubDateElement.getChildNodes();
            		
            		Article rss = new Article();
            		rss.setTitle(subTitleElement.item(0).getNodeValue());
            		rss.setLink(subLink.item(0).getNodeValue());
            		rss.setDescription(subDescription.item(0).getNodeValue());
            		String gmtString = subPubDate.item(0).getNodeValue();
            		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            		Date d = formatter.parse(gmtString);
            		//Date d = new Date(Date.parse(gmtString));
            		
            		rss.setWriteDate(DateTimeUtil.getDateStringFromDate(d, "yyyy.MM.dd"));
            		rssList.add(rss);
            	}
            }

		} catch(ClientProtocolException e) {
        	e.printStackTrace();
    		
    		//DialogUtils.alert(thisContext, "", "서버 접속에 실패했습니다");
    		//mMessage = "서버 접속에 실패했습니다.";
    		//mHandler.post(showDialog);
    	} catch(IOException e) {
    		e.printStackTrace();
    		
    		//DialogUtils.alert(thisContext, "", "서버에 연결할 수 없습니다");
    		//mMessage = "서버에 연결할 수 없습니다.";
    		//mHandler.post(showDialog);
    	} catch(Exception e) {
    		e.printStackTrace();
    		//Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(MyLecture.this, "", e.getMessage());
    	}
    }
	
	
	private class PageLoadTask extends AsyncTask<String, Integer, Integer> {
  		
  		protected void onPreExecute(){
  			super.onPreExecute();
  			
  			if(pageLoading != null)
  				pageLoading.setVisibility(View.VISIBLE);
  		}
  		
  		@Override
  		protected Integer doInBackground(String... args) {
  			
  			requestList();
  			return 0;
  		}
  		
  		protected void onPostExecute(Integer result) {
  			if(pageLoading != null)
  				pageLoading.setVisibility(View.GONE);
  			//adapter.notifyDataSetChanged();
  			//adapter = new JobKoreaRssAdapter(thisContext, R.layout.jobkorea_rss_row, rssList);
  		    //mListView.setAdapter(adapter);
  			
  			if(rssList != null){
  				if(rssList.size()>0){
  					for(int i=0;i<rssList.size();i++){
  						Article a = rssList.get(i);
  						if(i==0){
  							letterTitle1.setText(a.getTitle());
  							letterDate1.setText(a.getWriteDate());
  						}
  						if(i==1){
  							letterTitle2.setText(a.getTitle());
  							letterDate2.setText(a.getWriteDate());
  						}
  						if(i==2){
  							letterTitle3.setText(a.getTitle());
  							letterDate3.setText(a.getWriteDate());
  						}
  						
  						if(i==3){
  							break;
  						}
  					}
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

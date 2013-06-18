package com.youngrak;

import java.util.Calendar;
import java.util.Date;

import com.youngrak.common.dialog.DialogUtils;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {
	protected Context thisContext;
	private static String TAG = "BaseActivity";
	protected ApplicationEx appEx;
	private String messageText = "";
	private int toastLength = Toast.LENGTH_SHORT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

        if(getParent() != null){
            thisContext = getParent();
        } else {
            thisContext = this;
        }
        
        
        appEx = (ApplicationEx)this.getApplication();
        
        /*
        if(!appEx.isLogin()){
        	startActivity(new Intent(thisContext, Login.class));
        	//finish();
        }
		*/
        
    }
	
	protected void setPageTitle(String title){
		View titleInclude = (View)findViewById(R.id.top_menubar);
		if(titleInclude != null){
			TextView titleView = (TextView)titleInclude.findViewById(R.id.titleText);
			titleView.setText(title);
		}
		ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
		if(backButton != null) {
			backButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onBackPressed();
					
				}
			});
		}
		/*
		Button helpButton = (Button)findViewById(R.id.rightButton);
		if(helpButton != null) {
			helpButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
				}
			});
		}
		*/
	}
	
	
	protected DialogInterface.OnClickListener dialogGoBackListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			onBackPressed();
		}
	};
	
	protected DialogInterface.OnClickListener dialogGoLoginListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			Intent i = new Intent(thisContext, Login.class);
			startActivity(i);
		}
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionMenu");
		//MenuItem item = menu.add(0, 1, 0, "홈");
		//item.setIcon(R.drawable.home_gray);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected");
		Log.d(TAG, "item.getItemId()= "+item.getItemId());
		/*
		switch (item.getItemId()) {
			case 1:
			Log.d(TAG, "writeMenu");
			
			return true;
		}
		*/
		Intent i = new Intent(thisContext, MainActivity.class);
		startActivity(i);
		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Log.d(TAG, "onPrepareOptionsMenu");
	    return true;
	}
	
	protected Handler thisHandler = new Handler();
	
	private Runnable showMessage = new Runnable() {

		@Override
		public void run() {
			
			DialogUtils.alert(thisContext, "", messageText);
		}
		
	};
	
	private Runnable showMessageGoBack = new Runnable() {

		@Override
		public void run() {
			
			DialogUtils.alert(thisContext, "", messageText, dialogGoBackListener);
		}
		
	};
	
	protected void handlerShowMessage(String msg){
		messageText = msg;
		
		thisHandler.post(showMessage);
	}
	
	protected void handlerShowMessageGoBack(String msg){
		messageText = msg;
		
		thisHandler.post(showMessageGoBack);
	}
	
	private Runnable showToastMessage = new Runnable(){
		@Override
		public void run(){
			Toast.makeText(thisContext, messageText
    				, toastLength).show();
		}
	};
	
	protected void handlerShowToastMessage(String msg, int toastLength){
		messageText = msg;
		this.toastLength = toastLength;
		
		thisHandler.post(showToastMessage);
	}
	
	@Override
    public void onBackPressed(){
		//Date d = new Date();
    	long backPressTime = System.currentTimeMillis();
    	
    	long diff = backPressTime - appEx.getRecentBackPressTime();
    	//2초 이내로 back 2번 이상 버튼을 누르면
    	//Log.d(TAG, "diff = "+diff);
		if( diff < 3000){
			int bc = appEx.addBackPressCount();
			Log.d(TAG, "bc = "+bc);
			if(bc >= 2){
				
				new AlertDialog.Builder(thisContext)
		    	.setIcon(R.drawable.ic_launcher)
		    	.setTitle("알림")
		    	.setMessage("프로그램을 종료 하시겠습니까?")
		    	.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						moveTaskToBack(true);
					}
				})
				.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						appEx.setRecentBackPressTime(System.currentTimeMillis());
						appEx.setBackPressCount(0);
						
					}
				}).show();
				
				
				
				//moveTaskToBack(true);
				//return;
			} else {
				super.onBackPressed();
			}
		} else {
			appEx.setRecentBackPressTime(Calendar.getInstance().getTimeInMillis());
			appEx.setBackPressCount(0);
			super.onBackPressed();
		}
		
		
    	//moveTaskToBack(true);
		
		
	}
	/*
	@Override
	protected void onRestart(){
		Intent a = new Intent(thisContext, SplashScreen.class);
		//a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(a);
		
        super.onRestart();

	}
	*/
}



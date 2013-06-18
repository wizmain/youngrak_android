package com.youngrak;

import com.youngrak.common.StringUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class Webview extends BaseActivity {
	WebView webview;
	ProgressBar pageLoading;
	String url;
	boolean isClinic;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        
        
        setContentView(R.layout.webview);
        
        
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), title);
        
        
        webview = (WebView)findViewById(R.id.webview);
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        
        if(!StringUtils.isEmptyOrBlank(getIntent().getStringExtra("url"))){
        	url = getIntent().getStringExtra("url");
        }
        
        webview.loadUrl(url);
        
        // ĳ�� ��� ����
        webview.clearCache(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setJavaScriptEnabled(true);
        //webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webview.getSettings().setUseWideViewPort(true);
        webview.requestFocus(View.FOCUS_DOWN);
        //webview.getSettings().setSupportMultipleWindows(true);
        //webview.setInitialScale(55);
        webview.setWebChromeClient(new EleWebChromeClient());
        webview.setWebViewClient(new EleWebViewClient());
        webview.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				return false;
			}
		});
	}
	
	private class EleWebChromeClient extends WebChromeClient {
    	@Override
    	public void onProgressChanged(WebView view, int newProgress) {
    		//WebViewActivity.this.setProgress(newProgress * 100);
    		pageLoading.setProgress(newProgress);
    	}
    	
    	//���������� �ڹٽ�ũ��Ʈ Confirm �� �۳����� ó���ϴ� ���
    	@Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(thisContext)
            .setTitle(getResources().getString(R.string.app_name))
            .setMessage(message)
            .setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    result.confirm();
                }
            })
            .setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    result.cancel();
                }
            })
            .create()
            .show();

            return true;
        }
    }
    
    private class EleWebViewClient extends WebViewClient {
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		//Log.d(TAG, "shouldOverrideUrlLoading");
    		if(url.startsWith("tel:")){
    			Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(url));
    			startActivity(i);
    			return true;
    		}
    		//backPressedCount = 0;// ��ũ�� Ŭ���ؼ� ���� �ڷΰ��� ��ư Ŭ�� Ƚ�� �ʱ�ȭ 
    		view.loadUrl(url);
    		return false;
    	}
    	
    	@Override
    	public void onLoadResource(WebView view, String url) {
    		// �� ������ ���ҽ����� �ε��ϸ鼭 ����ؼ� ȣ��ȴ�.
    		super.onLoadResource(view, url);
    	}
    	
    	@Override
    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
    		// ������ ��û�� ���۵� ��� ȣ��ȴ�.
    		
    		/*
    		loadingSpinner = new ProgressDialog(thisContext);
    		loadingSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		loadingSpinner.setTitle("Updating");
    		loadingSpinner.setMessage("������ �ε����Դϴ�");
    		loadingSpinner.setProgress(0);
    		loadingSpinner.setMax(100);
    		loadingSpinner.setButton("���", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.cancel();
    			}
    		});
    		loadingSpinner.show();
    		*/
    		pageLoading.setVisibility(View.VISIBLE);
    		
    		super.onPageStarted(view, url, favicon);
    	}
    	
    	@Override
    	public void onPageFinished(WebView view, String url) {
    		// ������ �ε��� ȣ��ȴ�.
    		/*
    		if (loadingSpinner.isShowing()) {
    			loadingSpinner.cancel();
    		}
    		*/
    		pageLoading.setVisibility(View.GONE);
    		
    		super.onPageFinished(view, url);
    		
    		//Log.d(TAG, "url = " + webview.getUrl());
    		
    		
    	}
    	
    	@Override
    	public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
    		// TODO Auto-generated method stub
    		super.doUpdateVisitedHistory(view, url, isReload);
    	}

    	@Override
    	public void onFormResubmission(WebView view, Message dontResend, Message resend) {
    		// TODO Auto-generated method stub
    		super.onFormResubmission(view, dontResend, resend);
    	}


    	@Override
    	public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
    		// TODO Auto-generated method stub
    		super.onReceivedHttpAuthRequest(view, handler, host, realm);
    	}

    	@Override
    	public void onScaleChanged(WebView view, float oldScale, float newScale) {
    		// TODO Auto-generated method stub
    		super.onScaleChanged(view, oldScale, newScale);
    	}

    	@Override
    	public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
    		// TODO Auto-generated method stub
    		super.onTooManyRedirects(view, cancelMsg, continueMsg);
    	}

    	@Override
    	public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
    		// TODO Auto-generated method stub
    		super.onUnhandledKeyEvent(view, event);
    	}

    	@Override
    	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
    		// Ű�� �����ε��Ѱ��ε� �ַ� ���������� ��,�� ������ �̵��ϰ� �Ѵ�.
    		// ����Ű�� ������ �Ǹ� �ڷ�, ������ Ű�� ������ ���� �Ѵ�.
    		/*
    		int keyCode = event.getKeyCode();
    		if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT) && webview.canGoBack()) {
    			webview.goBack();
    		return true;
    		}else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && webview.canGoForward()) {
    			webview.goForward();
    		    return true;
    		}
    		return false;
    		*/
    		return super.shouldOverrideKeyEvent(view, event);
    	}

    	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    		// �������� �ε��� ������ �߻������� ó�� 
    		Toast.makeText(thisContext, "�����߻� : " + description, Toast.LENGTH_SHORT).show();
    		pageLoading.setVisibility(View.GONE);
    		/*
    		if (loadingSpinner.isShowing()) {
    			loadingSpinner.cancel();
    		}
    		*/
    	}
    	/*
    	public boolean onTouchEvent(MotionEvent event) { 
    		if ( event.getAction() > MotionEvent.ACTION_POINTER_1_DOWN) { 
    			webview.getSettings().setBuiltInZoomControls(true); 
    			webview.getSettings().setSupportZoom(true); 
    		} else { 
    			webview.getSettings().setBuiltInZoomControls(false); 
    			webview.getSettings().setSupportZoom(false); 
    		} 
    		return true; 
    	}
		*/
    }
    
    /*
    // ������ �ڷΰ��� 
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
        	//Log.d(TAG, "backPressedCount = "+backPressedCount);
        	if(backPressedCount >= getResources().getInteger(R.integer.finish_backpress_count)){
        		
        		return super.onKeyDown(keyCode, event);
        	} else {
        		
        		webview.goBack();
        		
        		backPressedCount = backPressedCount + 1;
        		Log.d(TAG, "backPressedCount++"+backPressedCount);
        	}
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    */
	
	@Override
    public void onBackPressed(){
    	if(webview.canGoBack()){
    		webview.goBack();
    	} else {
    		super.onBackPressed();
    	}
    }

}

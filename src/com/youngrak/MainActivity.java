package com.youngrak;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	
	protected boolean _active = true;
	protected int _splashTime = 1000; // time to display the splash screen in ms
	Context thisContext = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ApplicationEx appEx = (ApplicationEx)this.getApplication();
        //appEx.getInterviewDBHelper().upgrade();
        thisContext = this;
        
        try {
        	PackageInfo i = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        	appEx.setVersion(i.versionName);
        } catch(NameNotFoundException e) {
        	appEx.setVersion("0.0.0");
        }
		
        /*
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
        
		Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                	
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(100);
                        if(_active) {
                            waited += 100;
                        }
                    }
                    
                	
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                	Log.d("MainActivity", "startActivity");
                    finish();
                    startActivity(new Intent(thisContext, Home.class));
                    
                    //stop();
                }
            }
        };
        
        
        //startActivity(new Intent(this, Home.class));
        splashTread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

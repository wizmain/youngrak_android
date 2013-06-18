package com.youngrak;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.youngrak.R;

public class TopMenuInitializer {
	
	private Context thisContext;
	private ImageButton backButton;
	private ImageButton rightButton;
	private TextView pageTitle;
	
	
	public TopMenuInitializer(Context context, View v, String title){
		
		thisContext = context;
		View pageTitleView = (View)v.findViewById(R.id.top_menubar);
        
		pageTitle = (TextView)pageTitleView.findViewById(R.id.titleText);
        backButton = (ImageButton)pageTitleView.findViewById(R.id.backButton);
        rightButton = (ImageButton)pageTitleView.findViewById(R.id.rightButton);
        
        pageTitle.setText(title);
        
    	backButton.setOnClickListener(buttonListener);
    	rightButton.setOnClickListener(buttonListener);
	}
	
	public void setTitle(String title){
		pageTitle.setText(title);
	}
	
	public void init(){
		
	}
	
	Button.OnClickListener buttonListener = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			
			if(v.getId() == backButton.getId() ){//&& currentMenuId != 1
				((Activity)thisContext).onBackPressed();
				
			} else if(v.getId() == rightButton.getId() ){//&& currentMenuId != 2
				Intent i = new Intent(thisContext, Home.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				((Activity)thisContext).startActivity(i);
				
			}
		}
	};
}

package com.youngrak;


import com.youngrak.common.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class TributeFlowerDialog extends Dialog implements OnClickListener {

	Context mContext;
	ImageButton button1, button2, button3, button4, button5;
	int selectedButton = -1;
	static TributeFlowerDialogListener mListener;
	
	public TributeFlowerDialog(Context context) {
		super(context);
		
		mContext = context;
		try{
			mListener = (TributeFlowerDialogListener)((Activity)context);
		} catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(((Activity)context).toString()
                    + " must implement NoticeDialogListener");
        };
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tribute_flower_dialog);
		
		button1 = (ImageButton)findViewById(R.id.button1);
		button2 = (ImageButton)findViewById(R.id.button2);
		button3 = (ImageButton)findViewById(R.id.button3);
		button4 = (ImageButton)findViewById(R.id.button4);
		button5 = (ImageButton)findViewById(R.id.button5);
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
	}
	
	public interface TributeFlowerDialogListener {
		public void onTributeFlowerDialogClick(TributeFlowerDialog dialog);
	}

	@Override
	public void onClick(View v){
		selectedButton = StringUtils.toInteger((String)v.getTag(), -1);
		mListener.onTributeFlowerDialogClick(this);
		dismiss();
	}

}

package com.youngrak.common.dialog;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import com.youngrak.R;

public class PromptListener implements android.content.DialogInterface.OnClickListener {
	
	private String promptReply = null;
	View promptDialogView = null;
	
	public PromptListener(View inDialogView){
		promptDialogView = inDialogView;
	}
	
	public void onClick(DialogInterface v, int buttonID) {
		if(buttonID == DialogInterface.BUTTON1){
			promptReply = getPromptText();
		} else {
			promptReply = null;
		}
	}
	
	private String getPromptText(){
		EditText et = (EditText)promptDialogView.findViewById(R.id.promptmessage);
		return et.getText().toString();
	}
	
	public String getPromptReply(){
		return promptReply;
	}
}
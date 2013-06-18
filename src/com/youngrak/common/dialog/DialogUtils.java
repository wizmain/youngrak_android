package com.youngrak.common.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import com.youngrak.R;

public class DialogUtils {
	
	public static void alert(Context context, String title, String message){
		
		//대화창 레이아웃 뷰 로딩
		//LayoutInflater li = LayoutInflater.from(context);
		//View view = li.inflate(R.layout.prompt_layout, null);
		
		//대화창 빌더 생성
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		alert.setMessage(message);
		//alert.setView(view);
		
		//버튼 리스너 추가
		EmptyListener pl = new EmptyListener();
		alert.setPositiveButton("확인", pl);
		alert.show();
	}
	
	public static void alert(Context context, String title, String message, DialogInterface.OnClickListener pl){
		
		//대화창 레이아웃 뷰 로딩
		//LayoutInflater li = LayoutInflater.from(context);
		//View view = li.inflate(R.layout.prompt_layout, null);
		
		//대화창 빌더 생성
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		alert.setMessage(message);
		//alert.setView(view);
		
		//버튼 리스너 추가
		alert.setPositiveButton("확인", pl);
		alert.show();
	}
	
	
	public static String prompt(Context context, String title, String message){
		
		//대화창 레이아웃 뷰 로딩
		LayoutInflater li = LayoutInflater.from(context);
		View view = li.inflate(R.layout.prompt_layout, null);
		
		//대화창 빌더 생성
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setView(view);
		
		//버튼 리스너 추가
		PromptListener pl = new PromptListener(view);
		alert.setPositiveButton("확인", pl);
		alert.setNegativeButton("취소", pl);
		alert.show();
		
		return pl.getPromptReply();
	}
}

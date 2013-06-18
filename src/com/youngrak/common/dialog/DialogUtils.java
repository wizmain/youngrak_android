package com.youngrak.common.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import com.youngrak.R;

public class DialogUtils {
	
	public static void alert(Context context, String title, String message){
		
		//��ȭâ ���̾ƿ� �� �ε�
		//LayoutInflater li = LayoutInflater.from(context);
		//View view = li.inflate(R.layout.prompt_layout, null);
		
		//��ȭâ ���� ����
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		alert.setMessage(message);
		//alert.setView(view);
		
		//��ư ������ �߰�
		EmptyListener pl = new EmptyListener();
		alert.setPositiveButton("Ȯ��", pl);
		alert.show();
	}
	
	public static void alert(Context context, String title, String message, DialogInterface.OnClickListener pl){
		
		//��ȭâ ���̾ƿ� �� �ε�
		//LayoutInflater li = LayoutInflater.from(context);
		//View view = li.inflate(R.layout.prompt_layout, null);
		
		//��ȭâ ���� ����
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		alert.setMessage(message);
		//alert.setView(view);
		
		//��ư ������ �߰�
		alert.setPositiveButton("Ȯ��", pl);
		alert.show();
	}
	
	
	public static String prompt(Context context, String title, String message){
		
		//��ȭâ ���̾ƿ� �� �ε�
		LayoutInflater li = LayoutInflater.from(context);
		View view = li.inflate(R.layout.prompt_layout, null);
		
		//��ȭâ ���� ����
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setView(view);
		
		//��ư ������ �߰�
		PromptListener pl = new PromptListener(view);
		alert.setPositiveButton("Ȯ��", pl);
		alert.setNegativeButton("���", pl);
		alert.show();
		
		return pl.getPromptReply();
	}
}

package com.youngrak.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import android.util.Base64;
import android.util.Log;

public class StringUtils {
	
	public final static String REGULAR_EMAIL_PATTERN = "[!#-9A-~]+@[a-z0-9-_]+.+[a-z0-9-_]+.+[a-z0-9-]";
	public final static String REGULAR_PW_PATTERN = "[a-zA-Z0-9]+";
	public final static String REGULAR_NUM_PATTERN = "[0-9]+";
	
	public static boolean isValidateCheck(String str, String pattern) {
		return Pattern.matches(pattern,str); 		
	}
	
	public static boolean isNumberic(String s){
		java.util.regex.Pattern pattern = Pattern.compile("[+-]?\\d+");
		return pattern.matcher(s).matches();
	}
	
	public static String stackTraceToString(Throwable ex) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(b);
		ex.printStackTrace(p);
		p.close();
		String stackTrace = b.toString();
		try {
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return convertHtmlBr(stackTrace);
	}

	/**
	 * \r을 <br /> 로 변환
	 * @param comment
	 * @return
	 */
	public static String convertHtmlBr(String comment) {
		if (comment == null)
			return "";
		int length = comment.length();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			String tmp = comment.substring(i, i + 1);
			if ("\r".compareTo(tmp) == 0) {
				tmp = comment.substring(++i, i + 1);
				if ("\n".compareTo(tmp) == 0)
					buffer.append("<br>\r");
				else
					buffer.append("\r");
			}
			buffer.append(tmp);
		}
		return buffer.toString();
	}
	
	/**
	 * 스트링을 숫자로 변환
	 * @param number 변환할 글자
	 * @param def 숫자 변환 실패시 반환할 기본값
	 * @return 변환된 숫자
	 */
	public static int toInteger(String number, int def){
		
		try
		{
			return Integer.parseInt(number); 
		}catch(Exception e){
			return def;
		}
		
	}
	
	/**
	 * 스트링을 숫자로 변환
	 * @param number 변환할 글자
	 * @param def 숫자 변환 실패시 반환할 기본값
	 * @return 변환된 숫자
	 */
	public static long toLong(String number, long def){
		
		try
		{
			return Long.parseLong(number); 
		}catch(Exception e){
			return def;
		}
		
	}
	
	public static boolean isEmptyOrBlank(String str){
		if(str == null) return true;
		if(str.equals("")) return true;
		if(str.trim().equals("")) return true;
		
		return false;
	}
	
	public static String isEmptyOrBlank(String str, String def){
		if(isEmptyOrBlank(str)){
			return def;
		} else {
			return str;
		}
	}
	
	public static String[] tokenizer(String str, String splitStr){
		if(splitStr.length() > 1) {
	    	try {
	    		return str.split(splitStr);
	    	}catch(Exception e) {
	    		return null;
	    	}
    	} else {
    	
	    	StringTokenizer tokens = null;
	    	String temp[] = null;

    		tokens = new StringTokenizer(str, splitStr);
    		temp = new String[tokens.countTokens()];
    	
    		int i = 0;
    		String tok = "";
    		
    		while (tokens.hasMoreTokens()) {
	    		tok = tokens.nextToken();
	    		temp[i] = tok;
	    		i++;
	        }
	    	return temp;
    	}
	}
	
	public static String strcut(String str, int len) {

		String suffix = "..";

		byte[] by = str.getBytes();

		if( by.length <= len ) return str;

		int count = 0;
		
		try {
			for(int i = 0; i < len; i++) {
				if((by[i] & 0x80) == 0x80) count++;
			}
	
			if((by[len - 1] & 0x80) == 0x80 && (count % 2) == 1) len--;
				return new String(by, 0, len)+suffix;
		}
		catch(java.lang.ArrayIndexOutOfBoundsException e)
		{
			System.out.println(e);
			return "";
		}

	}
	
	public static String toPhoneFormat(String phoneString){
		if(isEmptyOrBlank(phoneString)){
			return "";
		}
		String cellPhone = null;
		if(phoneString.length()==9){
			cellPhone = phoneString.substring(0, 2)
			+ "-" + phoneString.substring(2, 5)
			+ "-" + phoneString.substring(5, 9);
		} else if(phoneString.length() == 10 ){
			cellPhone = phoneString.substring(0, 3)
			+ "-" + phoneString.substring(3, 6) 
			+ "-" + phoneString.substring(6, 10);
		} else if(phoneString.length() == 11 ){
			cellPhone = phoneString.substring(0, 3) 
			+ "-" + phoneString.substring(3, 7) 
			+ "-" + phoneString.substring(7, 11);
		} else if(phoneString.length() > 11){
			String[] aCellPhone = phoneString.split("-");
			if(aCellPhone.length == 3){
				cellPhone = aCellPhone[0] + "-" + aCellPhone[1] + "-" + aCellPhone[2];
			}
		}
		return cellPhone;
	}
	
	public static String toPhoneFormat(String phoneString, String def){
		String phoneFormat = toPhoneFormat(phoneString);
		if(isEmptyOrBlank(phoneFormat)){
			return def;
		} else {
			return phoneFormat;
		}
	}
	
	public static String getFileSize(long fileSize)
	{
		String gubn[] = {"Byte", "KB", "MB" } ;
		String returnSize = new String ();
		int gubnKey = 0;
		double changeSize = 0;
		//long fileSize = 0;
		try{
			//fileSize =  Long.parseLong(size);
			for( int x=0 ; (fileSize / (double)1024 ) >0 ; x++, fileSize/= (double) 1024 ){
				gubnKey = x;
				changeSize = fileSize;
			}
			returnSize = changeSize + gubn[gubnKey];
		}catch ( Exception ex){ returnSize = "0.0 Byte"; }
		return returnSize;
	}
	
	
	/**
	 * 경로에서 파일명만 분리
	 * @param path
	 * @return
	 */
	public static String getFileNameWithoutExt(String path){
		int dot = path.lastIndexOf('.');
	    int sep = path.lastIndexOf(File.separator);
	    return path.substring(sep + 1, dot);
	}
	
	public static String getFileName(String path){
		int sep = path.lastIndexOf(File.separator);
	    return path.substring(sep + 1);
	}
	
	/**
	 * 파일명에서 확장자만 분리
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName){
		
		int dot = fileName.lastIndexOf('.');
	    return fileName.substring(dot + 1);
	}
	
	public static String leftPad(int n, int padCount){
		String nString = Integer.toString(n);
		int cnt = padCount-nString.length();
		for(int i=0;i<cnt;i++){
			nString = "0"+nString;
		}
		return nString;
	}
	
	public static String rightPad(int n, int padCount){
		String nString = Integer.toString(n);
		int cnt = padCount-nString.length();
		for(int i=0;i<cnt;i++){
			nString = nString + "0";
		}
		
		return nString;
	}
	
	public static String getMD5Hash(String s){
		MessageDigest m = null;
		String hash = null;
		try{
			m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(),0,s.length());
			hash = new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		
		return hash;
	}
	
	public static String formatWon(int p) {
		return String.format("%,d", p);
	}
	
	/**
     * Base64 인코딩
     */
    public static String getBase64encode(String content){
        return Base64.encodeToString(content.getBytes(), 0);
    }
     
    /**
     * Base64 디코딩
     */
    public static String getBase64decode(String content){
        //return new String(Base64.decode(content, 0));
    	try {
			return new String(Base64.decode(content, Base64.DEFAULT), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
}

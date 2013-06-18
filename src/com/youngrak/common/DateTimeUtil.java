package com.youngrak.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 날짜 관련 유틸
 * 
 * @author 
 */
public class DateTimeUtil {
	
	public final static long DIFF_DAY = 24*60*60*1000;
	
	public static String getMMHHString(long time){
		if(time <= 0){
			return "00:00";
		}
		
		long t = time / 1000;
		
		int m = (int)(t/60);
		int s = (int)(t % 60 );
		String mString = Integer.toString(m);
		String sString = Integer.toString(s);
		if(m < 10){
			mString = "0"+mString;
		}
		if(s < 10){
			sString = "0"+sString;
		}
		
		return mString + ":"+sString;
		
	}
	
	public static String getTimeStringFromSec(long milisecond){
		
		if(milisecond < 1){
			return "00:00";
		}
		String rtn = "";
		int hour = 0, min = 0, second = 0;
		String hString = "", mString = "00", sString = "00";
		long sec = milisecond / 1000;
		hour = (int)(sec/3600);
		min = (int)((sec%3600)/60);
		second = (int) (sec%60);
		
		if(hour < 10){
			hString = "0"+hour;
		} else {
			hString = Integer.toString(hour);
		}
		
		if(min < 10){
			mString = "0"+min; 
		} else {
			mString = Integer.toString(min);
		}
			
		if(second < 10){
			sString = "0"+second;
		} else {
			sString = Integer.toString(second);
		}
		
		rtn = mString + ":" + sString;
		if(!hString.equals("00")){
			rtn = hString + ":" + rtn;
		}
		
		return rtn;
	}

    public static String getDateTimeByPattern(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                pattern, Locale.KOREA);
        String dateString = formatter.format(new Date());
        
        return dateString;
    }

    public static String getDateStringFromDate(Date date, String pattern){
        SimpleDateFormat formatter = new SimpleDateFormat(
                pattern, Locale.KOREA);
        return formatter.format(date);
    }
    
    public static String getDateTimeByPattern(Timestamp date, String pattern, String def){
    	if(date == null)
    		return def;
    	else {
    		SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
    		return formatter.format(date);
    	}
    }
    
    public static String parseDateString(String dateTime, String pattern){
		if ( dateTime != null ){
			String year = dateTime.substring(0, 4);
			String month = dateTime.substring(4, 6);
			String day = dateTime.substring(6, 8);
		
			return year + pattern + month + pattern + day;
		} else {
			return "";
		}
    }
    
    public static Date getDateFromString(String dateString, String pattern) throws ParseException{
    	SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREAN);
    	return formatter.parse(dateString);
    }
    
    public static Date getDateFromString(String dateString, String pattern, Date def){
    	SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREAN);
    	try{
    		return formatter.parse(dateString);
    	} catch (ParseException e){
    		return def;
    	}
    }
    
    public static int compareDate(Date dt1, Date dt2, String dateFormat) throws ParseException{
    	
    	SimpleDateFormat formatter = new SimpleDateFormat(dateFormat,Locale.KOREAN);
        String dateString1 = formatter.format(dt1);
        String dateString2 = formatter.format(dt2);
        
        Date nd1 = formatter.parse(dateString1);
    	Date nd2 = formatter.parse(dateString2);
    	
    	return nd1.compareTo(nd2);
    }
    
    public static int compareTimestamp(Timestamp ts1, Timestamp ts2, String dateFormat) throws ParseException{
    	
    	Date d1 = new Date();
    	Date d2 = new Date();
    	d1.setTime(ts1.getTime());
    	d2.setTime(ts2.getTime());
    	
    	return compareDate(d1, d2, dateFormat);
    }
    
    public static int compareDate(long time1, long time2, String dateFormat) throws ParseException{
    	Date d1 = new Date();
    	Date d2 = new Date();
    	d1.setTime(time1);
    	d2.setTime(time2);
    	
    	return compareDate(d1, d2, dateFormat);
    }

    public static String weekdayToKor(int weekday){
        String weekdayKor = "";
        switch(weekday){
            case 1 :
                weekdayKor = "일";
                break;
            case 2 :
                weekdayKor = "월";
                break;
            case 3 :
                weekdayKor = "화";
                break;
            case 4 :
                weekdayKor = "수";
                break;
            case 5 :
                weekdayKor = "목";
                break;
            case 6 :
                weekdayKor = "금";
                break;
            case 7 :
                weekdayKor = "토";
                break;
        }

        return weekdayKor;
    }
}

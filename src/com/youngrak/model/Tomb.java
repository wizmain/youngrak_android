package com.youngrak.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Tomb implements Serializable{
	
	String areaType;
	String buryDate;
	String buryNo;
	String deadSex;
	String deadName;
	String payName;
	String deadDate;
	String deadID;
	String sType;
	String checkType;
	
	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public String getBuryDate() {
		return buryDate;
	}

	public void setBuryDate(String buryDate) {
		this.buryDate = buryDate;
	}

	public String getBuryNo() {
		return buryNo;
	}

	public void setBuryNo(String buryNo) {
		this.buryNo = buryNo;
	}

	public String getDeadSex() {
		return deadSex;
	}

	public void setDeadSex(String deadSex) {
		this.deadSex = deadSex;
	}

	public String getDeadName() {
		return deadName;
	}

	public void setDeadName(String deadName) {
		this.deadName = deadName;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}
	
	public String getDeadDate() {
		return deadDate;
	}

	public void setDeadDate(String deadDate) {
		this.deadDate = deadDate;
	}
	
	public String getDeadID() {
		return deadID;
	}

	public void setDeadID(String deadID) {
		this.deadID = deadID;
	}
	
	public String getSType() {
		return sType;
	}

	public void setSType(String sType) {
		this.sType = sType;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public static Tomb bindJSONObject(JSONObject data){
		Tomb tomb = null;
		try {
			
			tomb = new Tomb();
			
			if(!data.isNull("area_type"))
				tomb.setAreaType(data.getString("area_type"));
			if(!data.isNull("bury_date"))//
				tomb.setBuryDate(data.getString("bury_date"));
			if(!data.isNull("bury_no"))//
				tomb.setBuryNo(data.getString("bury_no"));
			if(!data.isNull("dead_sex"))//
				tomb.setDeadSex(data.getString("dead_sex"));
			if(!data.isNull("dead_name"))//
				tomb.setDeadName(data.getString("dead_name"));
			if(!data.isNull("dead_date"))//
				tomb.setDeadDate(data.getString("dead_date"));
			if(!data.isNull("pay_name"))//
				tomb.setPayName(data.getString("pay_name"));
			if(!data.isNull("dead_id"))//
				tomb.setDeadID(data.getString("dead_id"));
			if(!data.isNull("s_type"))//
				tomb.setSType(data.getString("s_type"));
			if(!data.isNull("check_type"))//
				tomb.setCheckType(data.getString("check_type"));
			if(!data.isNull("dead_id"))//
				tomb.setCheckType(data.getString("dead_id"));
			/*
			if(!data.isNull("dead_sex")){
				String dateString = data.getString("s_date");
				if(!StringUtils.isEmptyOrBlank(dateString)){
					Date d = DateTimeUtil.getDateFromString(dateString, "yyyyMMddHHmmss");
					tomb.setStartDate(d.getTime());
				}
			}
			*/
			
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		//} catch (ParseException e) {
			
		//	e.printStackTrace();
		}
		
		return tomb;
	}
}

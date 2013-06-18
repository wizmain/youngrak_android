package com.youngrak.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Article implements Serializable {
	String cm1ID;
	String cm2ID;
	String title;
	String writeDate;
	String name;
	String link;
	String description;
	
	
	public String getCm1ID() {
		return cm1ID;
	}
	public void setCm1ID(String cm1id) {
		cm1ID = cm1id;
	}
	public String getCm2ID() {
		return cm2ID;
	}
	public void setCm2ID(String cm2id) {
		cm2ID = cm2id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public static Article bindJSONObject(JSONObject data){
		Article article = null;
		try {
			
			article = new Article();
			
			if(!data.isNull("cm1_id2"))
				article.setCm1ID(data.getString("cm1_id2"));
			if(!data.isNull("cm2_id"))//
				article.setCm2ID(data.getString("cm2_id"));
			if(!data.isNull("cm2_title"))//
				article.setTitle(data.getString("cm2_title"));
			if(!data.isNull("cm2_date"))//
				article.setWriteDate(data.getString("cm2_date"));
			if(!data.isNull("mb_name"))//
				article.setName(data.getString("mb_name"));
			if(!data.isNull("cm2_contents"))//
				article.setDescription(data.getString("cm2_contents"));
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		//} catch (ParseException e) {
			
		//	e.printStackTrace();
		}
		
		return article;
	}
}

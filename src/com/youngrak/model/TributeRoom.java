package com.youngrak.model;

import org.json.JSONException;
import org.json.JSONObject;

public class TributeRoom {
	String image;
	String deadName;
	String deadDate;
	String cmCode;
	String cmID;
	String makerName;
	String cm2Title;
	String cm2Contents;
	String cm1ID2;
	String cm2ID;
	String cm1Image;
	String cm1Image2;
	String cm3Image;
	String cm1Name;
	String id;
	String albumName;
	String memID;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDeadName() {
		return deadName;
	}
	public void setDeadName(String deadName) {
		this.deadName = deadName;
	}
	public String getDeadDate() {
		return deadDate;
	}
	public void setDeadDate(String deadDate) {
		this.deadDate = deadDate;
	}
	public String getCmCode() {
		return cmCode;
	}
	public void setCmCode(String cmCode) {
		this.cmCode = cmCode;
	}
	public String getCmID() {
		return cmID;
	}
	public void setCmID(String cmID) {
		this.cmID = cmID;
	}
	public String getMakerName() {
		return makerName;
	}
	public void setMakerName(String makerName) {
		this.makerName = makerName;
	}
	public String getCm2Title() {
		return cm2Title;
	}
	public void setCm2Title(String cm2Title) {
		this.cm2Title = cm2Title;
	}
	public String getCm2Contents() {
		return cm2Contents;
	}
	public void setCm2Contents(String cm2Contents) {
		this.cm2Contents = cm2Contents;
	}
	public String getCm1ID2() {
		return cm1ID2;
	}
	public void setCm1ID2(String cm1id2) {
		cm1ID2 = cm1id2;
	}
	public String getCm2ID() {
		return cm2ID;
	}
	public void setCm2ID(String cm2id) {
		cm2ID = cm2id;
	}
	public String getCm1Image() {
		return cm1Image;
	}
	public void setCm1Image(String cm1Image) {
		this.cm1Image = cm1Image;
	}
	public String getCm1Image2() {
		return cm1Image2;
	}
	public void setCm1Image2(String cm1Image2) {
		this.cm1Image2 = cm1Image2;
	}
	public String getCm3Image() {
		return cm3Image;
	}
	public void setCm3Image(String cm3Image) {
		this.cm3Image = cm3Image;
	}
	public String getCm1Name() {
		return cm1Name;
	}
	public void setCm1Name(String cm1Name) {
		this.cm1Name = cm1Name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getMemID() {
		return memID;
	}
	public void setMemID(String memID) {
		this.memID = memID;
	}
	public static TributeRoom bindJSONObject(JSONObject data){
		TributeRoom tributeRoom = null;
		try {
			
			tributeRoom = new TributeRoom();
			
			if(!data.isNull("image"))
				tributeRoom.setImage(data.getString("image"));
			if(!data.isNull("dead_name"))//
				tributeRoom.setDeadName(data.getString("dead_name"));
			if(!data.isNull("dead_date"))//
				tributeRoom.setDeadDate(data.getString("dead_date"));
			if(!data.isNull("cm1_code"))//
				tributeRoom.setCmCode(data.getString("cm1_code"));
			if(!data.isNull("cm1_id"))//
				tributeRoom.setCmID(data.getString("cm1_id"));
			if(!data.isNull("maker_name"))
				tributeRoom.setMakerName(data.getString("maker_name"));
			if(!data.isNull("cm1_id2"))
				tributeRoom.setCm1ID2(data.getString("cm1_id2"));
			if(!data.isNull("cm2_id"))
				tributeRoom.setCm2ID(data.getString("cm2_id"));
			if(!data.isNull("cm2_title"))
				tributeRoom.setCm2Title(data.getString("cm2_title"));
			if(!data.isNull("cm2_contents"))
				tributeRoom.setCm2Contents(data.getString("cm2_contents"));
			if(!data.isNull("cm1_img"))
				tributeRoom.setCm1Image(data.getString("cm1_img"));
			if(!data.isNull("cm1_img2"))
				tributeRoom.setCm1Image2(data.getString("cm1_img2"));
			if(!data.isNull("cm3_img"))
				tributeRoom.setCm3Image(data.getString("cm3_img"));
			if(!data.isNull("cm1_name"))
				tributeRoom.setCm1Name(data.getString("cm1_name"));
			if(!data.isNull("id_no"))
				tributeRoom.setId(data.getString("id_no"));
			if(!data.isNull("album_name"))
				tributeRoom.setAlbumName(data.getString("album_name"));
			if(!data.isNull(("mem_id")))
				tributeRoom.setMemID(data.getString("mem_id"));
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		//} catch (ParseException e) {
			
		//	e.printStackTrace();
		}
		
		return tributeRoom;
	}
}

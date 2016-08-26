package com.simicart.theme.ztheme.home.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class ZThemeSpotEntity extends SimiEntity {

	protected String mID;
	protected String mName;
	protected String mImage;
	protected String mKey;

	private String id = "spot_id";
	private String name = "spot_name";
	private String image = "spot_image";
	private String key = "spot_key";

	@Override
	public void parse() {

		mID = getData(id);

		mName = getData(name);

		mImage = getData(image);

		mKey = getData(key);

	}

	public String getID() {
		return mID;
	}

	public void setID(String mID) {
		this.mID = mID;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getImage() {
		return mImage;
	}

	public void setImage(String mImage) {
		this.mImage = mImage;
	}

	public String getKey() {
		return mKey;
	}

	public void setKey(String mKey) {
		this.mKey = mKey;
	}

}

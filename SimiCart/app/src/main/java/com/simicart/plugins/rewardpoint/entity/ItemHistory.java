package com.simicart.plugins.rewardpoint.entity;

import com.simicart.core.base.model.entity.SimiEntity;

import org.json.JSONObject;

public class ItemHistory extends SimiEntity {

	private String title;
	private String pointLabel;
	private String createTime;
	private String expirationTime;

	private String title_key = "title";
	private String pointLabel_key = "point_label";
	private String createTime_key = "created_time";
	private String expirationTime_key = "expiration_date";

	@Override
	public void parse() {
		title = getData(title_key);

		pointLabel = getData(pointLabel_key);

		createTime = getData(createTime_key);

		expirationTime = getData(expirationTime_key);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPointLabel() {
		return pointLabel;
	}

	public void setPointLabel(String pointLabel) {
		this.pointLabel = pointLabel;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

}

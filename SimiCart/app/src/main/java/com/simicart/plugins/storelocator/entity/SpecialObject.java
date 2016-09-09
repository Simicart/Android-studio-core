package com.simicart.plugins.storelocator.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class SpecialObject extends SimiEntity{

	private String id;
	private String date;
	private String time_open;
	private String time_close;

	private String id_key = "id";
	private String date_key = "date";
	private String time_open_key = "time_open";
	private String time_close_key = "time_close";

	@Override
	public void parse() {

		id = getData(id_key);

		date = getData(date_key);

		time_open = getData(time_open_key);

		time_close = getData(time_close_key);

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime_open() {
		return time_open;
	}

	public void setTime_open(String time_open) {
		this.time_open = time_open;
	}

	public String getTime_close() {
		return time_close;
	}

	public void setTime_close(String time_close) {
		this.time_close = time_close;
	}

}

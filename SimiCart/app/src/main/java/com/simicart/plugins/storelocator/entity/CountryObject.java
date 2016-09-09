package com.simicart.plugins.storelocator.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class CountryObject extends SimiEntity {

	private String country_code;
	private String country_name;

	private String country_code_key = "country_code";
	private String country_name_key = "country_name";

	@Override
	public void parse() {
		country_code = getData(country_code_key);

		country_name = getData(country_name_key);
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

}

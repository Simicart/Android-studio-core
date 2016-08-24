package com.simicart.core.catalog.product.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class ProductList extends SimiEntity {
	private ArrayList<Product> mSpotProduct;
	private String mTitle;

	protected String TITLE = "title";

	@Override
	public void parse() {
		mTitle = getData(TITLE);
		parseListSpot();
	}

	protected void parseListSpot(){
		try {
			mSpotProduct = new ArrayList<>();
			JSONArray productList = new JSONArray(getData(Constants.DATA));
			if (null != productList && productList.length() > 0) {
				for (int i = 0; i < productList.length(); i++) {
					Product product = new Product();
					JSONObject json = productList.getJSONObject(i);
					if (null != json) {
						product.setJSONObject(json);
						mSpotProduct.add(product);
					}
				}
			}
		} catch (JSONException e) {
		}
	}

	public ArrayList<Product> getSpotProduct() {
		return mSpotProduct;
	}

	public void setSpotProduct(ArrayList<Product> spotProduct) {
		mSpotProduct = spotProduct;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}
}

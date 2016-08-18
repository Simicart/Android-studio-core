package com.simicart.core.catalog.category.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Constants;

import java.util.ArrayList;

public class ListProductModel extends SimiModel {

	protected String mID = "-1";
	protected String mQty;
	protected ArrayList<Product> listProducts;

	public void setCategoryID(String id) {
		mID = id;
	}

	public String getQty() {
		return mQty;
	}

	@Override
	protected void setUrlAction() {
		if (mID.equals("-1")) {
			mUrlAction = Constants.GET_ALL_PRODUCTS;
		} else {
			mUrlAction = Constants.GET_CATEGORY_PRODUCTS;
		}

	}

	@Override
	protected void parseData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			if (null == collection) {
				collection = new SimiCollection();
				collection.setJSON(mJSON);
			}
			listProducts = new ArrayList<>();
			for (int i = 0; i < list.length(); i++) {
				Product product = new Product();
				product.setJSONObject(list.getJSONObject(i));
				product.parse();
				listProducts.add(product);
				collection.addEntity(product);
			}

		} catch (JSONException e) {

		}
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}

	public ArrayList<Product> getListProducts() {
		return listProducts;
	}
}

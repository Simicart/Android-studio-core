package com.simicart.core.catalog.category.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;

public class ListCategoryModel extends SimiModel {

	protected String mID = "-1";
	protected String mQty;

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
			Log.d("Json return:", this.mJSON.getJSONArray("data") + "");
			if (null == collection) {
				collection = new SimiCollection();
				collection.setJSON(mJSON);
			}
			collection.getCollection().clear();
			for (int i = 0; i < list.length(); i++) {
				Product product = new Product();
				product.setJSONObject(list.getJSONObject(i));
				collection.addEntity(product);
			}

		} catch (JSONException e) {

		}
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}

}

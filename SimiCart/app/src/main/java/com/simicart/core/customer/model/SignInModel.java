package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInModel extends SimiModel {

	protected String mUserName;
	protected String mCartQty;

	public String getName() {
		return mUserName;
	}

	public String getCartQty() {
		return mCartQty;
	}

	@Override
	protected void parseData() {
		// TODO Auto-generated method stub
		super.parseData();
		try {
			JSONArray arr = mJSON.getJSONArray(Constants.DATA);
			JSONObject json = arr.getJSONObject(0);
			mUserName = json.getString(Constants.USER_NAME);
			mCartQty = json.getString(Constants.CART_QTY);
		} catch (JSONException e) {
		}

	}

	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.SIGN_IN;
	}

}

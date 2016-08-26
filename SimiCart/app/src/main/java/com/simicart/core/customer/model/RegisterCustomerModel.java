package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.RegisterCustomer;

import org.json.JSONArray;
import org.json.JSONException;

public class RegisterCustomerModel extends SimiModel{
	@Override
	protected void parseData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			for (int i = 0; i < list.length(); i++) {
				RegisterCustomer register = new RegisterCustomer();
				register.setJSONObject(list.getJSONObject(i));
				collection.addEntity(register);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.REGISTER;
	}
}

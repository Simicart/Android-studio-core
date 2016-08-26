package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.OrderHisDetail;

import org.json.JSONArray;
import org.json.JSONException;

public class OrderHistoryDetailModel extends SimiModel {

	@Override
	protected void parseData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			for (int i = 0; i < list.length(); i++) {
				OrderHisDetail orderHis = new OrderHisDetail();
				orderHis.parse(list.getJSONObject(i));
				collection.addEntity(orderHis);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.GET_ORDER_DETAIL;
	}

}

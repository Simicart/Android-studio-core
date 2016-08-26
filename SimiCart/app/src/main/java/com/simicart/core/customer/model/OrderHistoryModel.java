package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.OrderHistory;

import org.json.JSONArray;
import org.json.JSONException;

public class OrderHistoryModel extends SimiModel {

	@Override
	protected void parseData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			if (null == collection) {
				collection = new SimiCollection();
			}
			for (int i = 0; i < list.length(); i++) {
				OrderHistory orderHis = new OrderHistory();
				orderHis.parse(list.getJSONObject(i));
				collection.addEntity(orderHis);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.GET_ORDER_HISTORY;
	}

}

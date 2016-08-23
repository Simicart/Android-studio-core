package com.simicart.core.checkout.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Constants;

import java.util.ArrayList;

public class CartModel extends SimiModel {

	protected int mQty;
	protected TotalPrice mTotalPrice;
	protected ArrayList<Cart> listCarts;

	public int getQty() {
		return mQty;
	}

	@Override
	protected void parseData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			collection.setJSON(mJSON);
			mQty = 0;
			listCarts = new ArrayList<>();
			for (int i = 0; i < list.length(); i++) {
				Cart cart = new Cart();
				cart.parse(list.getJSONObject(i));
				mQty += cart.getQty();
				collection.addEntity(cart);
				listCarts.add(cart);
			}

			try {
				if (mJSON.has(Constants.OTHER)) {
					JSONObject jsonPrice = mJSON.getJSONObject(Constants.OTHER);
					if (null != jsonPrice) {
						collection.setJSONOther(jsonPrice);
						mTotalPrice = new TotalPrice();
						mTotalPrice.setJSONObject(jsonPrice);
					}
				}
			} catch (JSONException e) {
				mTotalPrice = null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void setUrlAction() {
		mUrlAction = Constants.GET_CART;
	}

	public TotalPrice getTotalPrice() {
		return mTotalPrice;
	}

	public ArrayList<Cart> getListCarts() {
		return listCarts;
	}

	@Override
	protected void setEnableCache() {
		enableCache = false;
	}

}

package com.simicart.plugins.wishlist.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.plugins.wishlist.common.WishListConstants;
import com.simicart.plugins.wishlist.entity.ItemWishList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyWishListModel extends SimiModel {

    protected String mShareMessage;
    private int wishlist_qty;

    public String getShareMessage() {
        return mShareMessage;
    }

    public int getWishlist_qty() {
        return wishlist_qty;
    }

    @Override
    protected void parseData() {
        try {
            collection = new SimiCollection();
            if (mJSON.has(Constants.DATA)) {
                JSONArray array = mJSON.getJSONArray(Constants.DATA);
                if (null != array && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonItem = array.getJSONObject(i);
                        ItemWishList item = new ItemWishList();
                        item.parse(jsonItem);
                        collection.addEntity(item);
                    }
                }
            }
            if (mJSON.has(WishListConstants.WISHLIST_INFO)) {
                JSONArray array = mJSON
                        .getJSONArray(WishListConstants.WISHLIST_INFO);
                if (null != array && array.length() > 0) {
                    JSONObject jsonItem = array.getJSONObject(0);

                    Log.e("MyWishListModel parserData ", jsonItem.toString());

                    if (jsonItem.has(WishListConstants.WISHLIST_ITEMS_QTY)) {
                        wishlist_qty = jsonItem
                                .getInt(WishListConstants.WISHLIST_ITEMS_QTY);
                    }
                    if (jsonItem.has(WishListConstants.SHARING_MESSAGE)) {
                        mShareMessage = jsonItem
                                .getString(WishListConstants.SHARING_MESSAGE);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "appwishlist/api/get_wishlist_products";
    }
}

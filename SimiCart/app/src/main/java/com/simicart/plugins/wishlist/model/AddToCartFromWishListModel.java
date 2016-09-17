package com.simicart.plugins.wishlist.model;

import com.simicart.core.config.Constants;
import com.simicart.plugins.wishlist.common.WishListConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddToCartFromWishListModel extends MyWishListModel {

    protected int cart_qty = 0;

    public int getCartQty() {
        return cart_qty;
    }


    @Override
    protected void setUrlAction() {
        mUrlAction = "appwishlist/api/add_wishlist_product_to_cart";
    }

    @Override
    protected void parseData() {
        // TODO Auto-generated method stub
        super.parseData();

        if (mJSON.has(WishListConstants.WISHLIST_INFO)) {
            JSONArray array;
            try {
                array = mJSON
                        .getJSONArray(WishListConstants.WISHLIST_INFO);
                if (null != array && array.length() > 0) {
                    JSONObject jsonItem = array.getJSONObject(0);
                    if (jsonItem.has(Constants.CART_QTY)) {
                        cart_qty = jsonItem.getInt(Constants.CART_QTY);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }


}

package com.simicart.plugins.wishlist.model;


public class RemoveWishListModel extends MyWishListModel {

    @Override
    protected void setUrlAction() {
        mUrlAction = "appwishlist/api/remove_product_from_wishlist";
    }
}

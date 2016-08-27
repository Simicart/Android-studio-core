package com.simicart.plugins.wishlist.model;


public class AddWishListModel extends MyWishListModel {

	@Override
	protected void setUrlAction() {
		mUrlAction = "appwishlist/api/add_product_to_wishlist";
	}

}

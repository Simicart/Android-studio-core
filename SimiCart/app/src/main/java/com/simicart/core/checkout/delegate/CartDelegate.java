package com.simicart.core.checkout.delegate;

import android.view.View;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.TotalPrice;

import java.util.ArrayList;

public interface CartDelegate extends SimiDelegate {

	public void onUpdateTotalPrice(TotalPrice totalPrice);

	public void showPopupCheckout();

	public void dismissPopupCheckout();

	public void setMessage(String message);

	public void visibleAllView();

	void setCheckoutWebView(String url);

	public void showListProductsView(ArrayList<Cart> listCarts);

}

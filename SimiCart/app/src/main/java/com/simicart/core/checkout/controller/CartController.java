package com.simicart.core.checkout.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.delegate.CartDelegate;
import com.simicart.core.checkout.model.CartModel;
import com.simicart.core.config.Constants;

import org.json.JSONObject;

public class CartController extends SimiController {

    protected CartDelegate mDelegate;

    // protected boolean isLoadAgain = true;

    public CartController() {

    }

    @Override
    public void onStart() {
        request();
    }

    @Override
    public void onResume() {
        request();
    }

    private void request() {
        mModel = new CartModel();
        mDelegate.showLoading();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
//					mDelegate.setMessage(message);
                mDelegate.updateView(mModel.getCollection());
                mDelegate.onUpdateTotalPrice(((CartModel) mModel)
                        .getTotalPrice());
                int carQty = ((CartModel) mModel).getQty();
                SimiManager.getIntance().onUpdateCartQty(
                        String.valueOf(carQty));

                String url = getUrl(mModel.getCollection().getJSON());
                mDelegate.setCheckoutWebView(url);
            }
        });
        mModel.request();
    }

    private String getUrl(JSONObject mJSON) {
        String mOrderWebview = "";
        try {
            if (mJSON != null && mJSON.has(Constants.OTHER)) {
                JSONObject jsonPrice = mJSON.getJSONObject(Constants.OTHER);
                if (jsonPrice.has("order_by_webview_url")) {
                    mOrderWebview = jsonPrice.getString("order_by_webview_url");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return mOrderWebview;
    }

    public void setDelegate(CartDelegate delegate) {
        mDelegate = delegate;
    }

}

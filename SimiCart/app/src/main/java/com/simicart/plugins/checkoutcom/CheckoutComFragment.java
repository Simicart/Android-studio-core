package com.simicart.plugins.checkoutcom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.payment.PaymentFragment;
import com.simicart.core.base.payment.WebviewPaymentCallBack;
import com.simicart.core.base.payment.WebviewPaymentComponent;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.HashMap;

/**
 * Created by frank on 30/08/2016.
 */
public class CheckoutComFragment extends PaymentFragment {

    protected String LIVE_URL = "https://secure.checkout.com/hpayment-tokenretry/pay.aspx?";


    protected LinearLayout llPayment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_payment");
        rootView = inflater.inflate(idView, null);
        llPayment = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_payment"));
        initLoadingView();
        String data = "";
        if (null != mOrderInforEntity) {
            data = mOrderInforEntity.getData("params");
        }
        String url = LIVE_URL + data;
        String urlBack = mPaymentMethodEntity.getData("url_back");

        HashMap<String, Object> hm = new HashMap<>();
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.URL, url);
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_SUCCESS, urlBack);

        WebviewPaymentComponent paymentComponent = new WebviewPaymentComponent(hm, new WebviewPaymentCallBack() {
            @Override
            public boolean onSuccess(String url) {
                processSuccess(url);
                return true;
            }

            @Override
            public boolean onFail(String url) {
                return false;
            }

            @Override
            public boolean onError(String url) {
                return false;
            }

            @Override
            public boolean onReview(String url) {
                return false;
            }
        });

        View paymentView = paymentComponent.createView();
        llPayment.addView(paymentView);

        return rootView;
    }

    protected void processSuccess(String url) {
        String[] array = url.split("\\?");
        String path = array[1];

        String[] array1 = path.split("&responsecode=");
        path = array1[0];
        String[] array2 = path.split("&result=");
        String result = array2[1];

        path = array[1];
        array1 = path.split("&authcode=");
        path = array1[0];
        array2 = path.split("&tranid=");
        String orderId = array2[1];
        if (Utils.validateString(orderId) && Utils.validateString(result)) {
            updatePaymentMethod(orderId, result);
        }
    }

    protected void updatePaymentMethod(String orderId, String result) {

        String urlAction = mPaymentMethodEntity.getData("url_action");
        String invoiceNumber = mOrderInforEntity.getData("invoice_number");

        showLoading();

        UpdatePaymentModel mModel = new UpdatePaymentModel();
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                dimissLoading();
                String msg = error.getMessage();
                if (Utils.validateString(msg)) {
                    SimiNotify.getInstance().showToast(msg);
                }
            }
        });

        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                dimissLoading();
                SimiManager.getIntance().backToHomeFragment();
            }
        });

        mModel.addBody("invoice_number", invoiceNumber);
        mModel.addBody("transaction_id", orderId);
        if (result.toLowerCase().equals("successful")) {
            mModel.addBody("payment_status", "1");
        } else {
            mModel.addBody("payment_status", "0");
        }
        mModel.setUrlAction(urlAction);
        mModel.request();
    }

    @Override
    protected void showLoading() {
        for (int i = 0; i < llPayment.getChildCount(); i++) {
            llPayment.getChildAt(i).setVisibility(View.GONE);
        }
        llPayment.addView(mLoadingView);
    }

    @Override
    protected void dimissLoading() {
        llPayment.removeView(mLoadingView);
        for (int i = 0; i < llPayment.getChildCount(); i++) {
            View view = llPayment.getChildAt(i);
            view.setVisibility(View.VISIBLE);
        }
    }
}

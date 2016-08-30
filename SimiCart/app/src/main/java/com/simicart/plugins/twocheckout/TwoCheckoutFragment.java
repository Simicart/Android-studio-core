package com.simicart.plugins.twocheckout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
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
public class TwoCheckoutFragment extends PaymentFragment {

    String LIVE_URL = "https://www.2checkout.com/checkout/purchase?";
    String SANBOX_URL = "https://sandbox.2checkout.com/checkout/purchase?";

    public static TwoCheckoutFragment newInstance(SimiData data) {
        TwoCheckoutFragment fragment = new TwoCheckoutFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected LinearLayout llPayment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_payment");
        rootView = inflater.inflate(idView, null);
        llPayment = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_payment"));
        initLoadingView();
        String url = LIVE_URL;
        String demo = mPaymentMethodEntity.getData("is_sandbox");
        if (!demo.equals("1")) {
            url = SANBOX_URL;
        }

        String data = mOrderInforEntity.getData("params");
        String fullUrl = url + data;
        String urlBack = mPaymentMethodEntity.getData("url_back");

        HashMap<String, Object> hm = new HashMap<>();
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.URL, fullUrl);
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

            @Override
            public boolean onOther(String url) {
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
        String[] array1 = path.split("&order_number=");
        path = array1[1];
        String[] array2 = path.split("&");
        String orderId = array2[0];
        if (Utils.validateString(orderId)) {
            updatePayment(orderId);
        }
    }

    protected void updatePayment(String orderID) {
        showLoading();
        UpdatePaymentMethod updatePaymentMethod = new UpdatePaymentMethod();
        updatePaymentMethod.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                dimissLoading();
                String msg = error.getMessage();
                if (Utils.validateString(msg)) {
                    SimiNotify.getInstance().showToast(msg);
                }
            }
        });

        updatePaymentMethod.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                dimissLoading();
                SimiManager.getIntance().backToHomeFragment();
            }
        });

        String invoice_number = mOrderInforEntity.getData("invoice_number");
        String urlAction = mPaymentMethodEntity.getData("url_action");
        updatePaymentMethod.setUrlAction(urlAction);
        updatePaymentMethod.addBody("invoice_number", invoice_number);
        updatePaymentMethod.addBody("transaction_id", orderID);
        updatePaymentMethod.addBody("payment_status", "1");
        updatePaymentMethod.request();

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

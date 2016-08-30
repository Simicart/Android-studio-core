package com.simicart.plugins.payuindian;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
public class PayUIndianFragment extends PaymentFragment {

    public static final String SUCCESS = "simipayuindia/api/success/";
    public static final String FAIL = "simipayuindia/api/failure/";
    public static final String CANCEL = "simipayuindia/api/canceled/";
    public static final String ERROR = "checkout/cart/";

    public static final String MES_SUCCESS = "Complete order Successfully. Thank your for purchase";
    public static final String MES_FAIL = "Failure: Your order has been canceled";
    public static final String MES_ERROR = "Have some errors, please try again";

    protected LinearLayout llPayment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int idView = Rconfig.getInstance().layout("core_fragment_payment");
        rootView = inflater.inflate(idView, null);
        llPayment = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_payment"));

        String url = mOrderInforEntity.getData("url_action");
        if (Utils.validateString(url)) {
            HashMap<String, Object> hm = new HashMap<>();

            hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.URL, url);
            hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_SUCCESS, SUCCESS);
            hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_FAIL, FAIL);
            hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_ERROR, ERROR);
            hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_SUCCESS, MES_SUCCESS);
            hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_FAIL, MES_FAIL);
            hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_ERROR, MES_ERROR);

            //_payment_options

            WebviewPaymentComponent paymentComponent = new WebviewPaymentComponent(hm, new WebviewPaymentCallBack() {
                @Override
                public boolean onSuccess(String url) {
                    return false;
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

        }

        return rootView;
    }
}

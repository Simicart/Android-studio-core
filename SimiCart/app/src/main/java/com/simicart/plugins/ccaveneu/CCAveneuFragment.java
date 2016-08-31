package com.simicart.plugins.ccaveneu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.payment.PaymentFragment;
import com.simicart.core.base.payment.WebviewPaymentCallBack;
import com.simicart.core.base.payment.WebviewPaymentComponent;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.HashMap;

/**
 * Created by frank on 8/28/16.
 */
public class CCAveneuFragment extends PaymentFragment {

    public static final String SUCCESS = "onepage/success";
    public static final String REVIEW = "onepage/review";
    public static final String FAIL = "onepage/failure";
    public static final String ERROR = "simiavenue/api/index";

    public static final String MES_SUCCESS = "Complete order Successfully. Thank your for purchase";
    public static final String MES_REVIEW = "The order changes to reviewed";
    public static final String MES_FAIL = "Failure: Your order has been canceled";
    public static final String MES_ERROR = "Have some errors, please try again";

    public static CCAveneuFragment newInstance(SimiData data) {
        CCAveneuFragment fragment = new CCAveneuFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected LinearLayout llPayment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_payment");
        rootView = inflater.inflate(idView, null);
        llPayment = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_payment"));

        String url = null;
        if (null != mOrderInforEntity) {
            url = mOrderInforEntity.getData("params");
        }
        Log.e("CCAveneuFragment","-----> URL " + url);
        if (Utils.validateString(url)) {

            HashMap<String, Object> data = new HashMap<>();
            data.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.URL, url);
            data.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_SUCCESS, MES_SUCCESS);
            data.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_ERROR, MES_ERROR);
            data.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_FAIL, MES_FAIL);
            data.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_SUCCESS, SUCCESS);
            data.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_FAIL, FAIL);
            data.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_ERROR, ERROR);
            data.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_REVIEW, REVIEW);
            data.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_REVIEW, MES_REVIEW);

            WebviewPaymentComponent paymentComponent = new WebviewPaymentComponent(data, new WebviewPaymentCallBack() {
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

                @Override
                public boolean onOther(String url) {
                    return false;
                }
            });

            View paymentView = paymentComponent.createView();
            llPayment.addView(paymentView);
        }
        return rootView;
    }
}

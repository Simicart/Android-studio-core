package com.simicart.core.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.payment.WebviewPaymentComponent;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.HashMap;

/**
 * Created by frank on 06/09/2016.
 */
public class WebviewFragment extends SimiFragment {

    public static WebviewFragment newInstance(SimiData data) {
        WebviewFragment fragment = new WebviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_payment");
        rootView = inflater.inflate(idView, null);

        LinearLayout llParent = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_payment"));

        String url = null;
        if (mHashMapData.containsKey(KeyData.WEBVIEW_PAGE.URL)) {
            url = (String) mHashMapData.get(KeyData.WEBVIEW_PAGE.URL);
        }

        if (Utils.validateString(url)) {
            HashMap<String, Object> data = new HashMap<>();
            data.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.URL, url);
            WebviewPaymentComponent paymentComponent = new WebviewPaymentComponent(data, null);
            View webView = paymentComponent.createView();
            llParent.addView(webView);
        }

        return rootView;
    }
}

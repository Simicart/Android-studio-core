package com.simicart.plugins.ccaveneu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.payment.WebviewPaymentCallBack;
import com.simicart.core.base.payment.WebviewPaymentComponent;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 8/28/16.
 */
public class CCAveneuFragment extends SimiFragment {

    public static CCAveneuFragment newInstance(SimiData data) {
        CCAveneuFragment fragment = new CCAveneuFragment();
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
        WebviewPaymentComponent paymentComponent = new WebviewPaymentComponent(mHashMapData, new WebviewPaymentCallBack() {
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
        });
        return rootView;
    }
}

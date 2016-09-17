package com.simicart.plugins.custompayment;

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
import com.simicart.core.checkout.entity.OrderInforEntity;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.HashMap;

/**
 * Created by frank on 30/08/2016.
 */
public class CustomPaymentFragment extends SimiFragment {

    protected CustomPaymentEntity mCustomPaymentEntity;
    protected OrderInforEntity mOrderInforEntity;
    protected String mInvoiceNumber;
    protected String mUrlAction;
    protected LinearLayout llPayment;

    public static CustomPaymentFragment newIntance(SimiData data) {
        CustomPaymentFragment fragment = new CustomPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_payment");
        rootView = inflater.inflate(idView, null);
        llPayment = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_payment"));
        parseData();

        HashMap<String, Object> hm = new HashMap<>();
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.URL, mUrlAction);
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_SUCCESS, mCustomPaymentEntity.getUrlSuccess());
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_FAIL, mCustomPaymentEntity.getUrlFail());
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_ERROR, mCustomPaymentEntity.getUrlError());
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_REVIEW, mCustomPaymentEntity.getUrlCancel());
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_SUCCESS, mCustomPaymentEntity.getMessageSuccess());
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_FAIL, mCustomPaymentEntity.getMessageFail());
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_ERROR, mCustomPaymentEntity.getMessageError());
        hm.put(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_REVIEW, mCustomPaymentEntity.getMessageCancel());

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

            @Override
            public boolean onOther(String url) {
                return false;
            }
        });

        View paymentView = paymentComponent.createView();
        llPayment.addView(paymentView);

        return rootView;
    }

    private void parseData() {
        if (mHashMapData.containsKey("order_infor_entity")) {
            mOrderInforEntity = (OrderInforEntity) mHashMapData.get("order_infor_entity");
        }

        if (mHashMapData.containsKey("custom_payment_entity")) {
            mCustomPaymentEntity = (CustomPaymentEntity) mHashMapData.get("custom_payment_entity");
        }

        if (mOrderInforEntity.hasKey("url_action")) {
            mUrlAction = mOrderInforEntity.getData("url_action");
        } else {
            String titleUrlAction = mCustomPaymentEntity.getTitleUrlAction();
            if (Utils.validateString(titleUrlAction) && mOrderInforEntity.hasKey(titleUrlAction)) {
                mUrlAction = mOrderInforEntity.getData(titleUrlAction);
            }
        }

        mInvoiceNumber = mOrderInforEntity.getData("invoice_number");

    }

}

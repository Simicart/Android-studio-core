package com.simicart.core.base.payment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.checkout.entity.OrderInforEntity;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.checkout.entity.ReviewOrderEntity;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 29/08/2016.
 */
public class PaymentFragment extends SimiFragment {

    protected OrderInforEntity mOrderInforEntity;
    protected ReviewOrderEntity mReviewOrderEntity;
    protected PaymentMethodEntity mPaymentMethodEntity;
    protected String mPaymentMethod;
    protected View mLoadingView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseData();
    }

    protected void parseData() {
        if (mHashMapData.containsKey("payment_method_entity")) {
            mPaymentMethodEntity = (PaymentMethodEntity) mHashMapData.get("payment_method_entity");
            mPaymentMethod = mPaymentMethodEntity.getPaymentMethod();
        }

        if (mHashMapData.containsKey("review_order_entity")) {
            mReviewOrderEntity = (ReviewOrderEntity) mHashMapData.get("review_order_entity");
        }

        if (mHashMapData.containsKey("order_infor_entity")) {
            mOrderInforEntity = (OrderInforEntity) mHashMapData.get("order_infor_entity");
        }

    }

    protected void initLoadingView() {
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLoadingView = inflater.inflate(
                Rconfig.getInstance().layout("core_base_loading"), null,
                false);
    }

    protected void showLoading() {

    }

    protected void dimissLoading() {

    }


}

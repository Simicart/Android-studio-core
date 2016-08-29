package com.simicart.core.base.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.checkout.entity.OrderInforEntity;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.checkout.entity.ReviewOrderEntity;

/**
 * Created by frank on 29/08/2016.
 */
public class PaymentFragment extends SimiFragment {

    protected OrderInforEntity mOrderInforEntity;
    protected ReviewOrderEntity mReviewOrderEntity;
    protected PaymentMethodEntity mPaymentMethodEntity;
    protected String mPaymentMethod;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseData();
    }

    protected void parseData() {
        if (mHashMapData.containsKey("payment_method")) {
            mPaymentMethodEntity = (PaymentMethodEntity) mHashMapData.get("payment_method");
            mPaymentMethod = mPaymentMethodEntity.getPaymentMethod();
        }

        if (mHashMapData.containsKey("review_order_entity")) {
            mReviewOrderEntity = (ReviewOrderEntity) mHashMapData.get("review_order_entity");
        }

        if (mHashMapData.containsKey("order_infor_entity")) {
            mOrderInforEntity = (OrderInforEntity) mHashMapData.get("order_infor_entity");
        }

    }

}

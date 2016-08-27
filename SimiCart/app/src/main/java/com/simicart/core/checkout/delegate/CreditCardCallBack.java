package com.simicart.core.checkout.delegate;

import com.simicart.core.checkout.entity.PaymentMethodEntity;

/**
 * Created by frank on 7/5/16.
 */
public interface CreditCardCallBack {

    public void onSaveCreditCard(PaymentMethodEntity paymentMethodEntity);
}

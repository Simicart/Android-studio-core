package com.simicart.core.checkout.delegate;

import com.simicart.core.checkout.entity.PaymentMethodEntity;

/**
 * Created by frank on 11/05/2016.
 */
public interface PaymentMethodCallBack {

    public void onSelectItem(PaymentMethodEntity payment);

    public void onEditAction(PaymentMethodEntity payment);

}

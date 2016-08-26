package com.simicart.core.checkout.delegate;

import com.simicart.core.checkout.entity.CreditCard;

/**
 * Created by frank on 7/5/16.
 */
public interface CreditCardCallBack {

    public void onSaveCreditCard(CreditCard cardEntity);
}

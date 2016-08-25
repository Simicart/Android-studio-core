package com.simicart.core.checkout.delegate;


import com.simicart.core.checkout.entity.ShippingMethodEntity;

/**
 * Created by frank on 6/29/16.
 */
public interface ShippingMethodCallBack {

    public void onSelect(ShippingMethodEntity entity);

}

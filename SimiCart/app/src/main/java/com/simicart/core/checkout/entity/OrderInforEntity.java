package com.simicart.core.checkout.entity;

import com.simicart.core.base.model.entity.SimiEntity;

/**
 * Created by frank on 26/08/2016.
 */
public class OrderInforEntity extends SimiEntity {

    protected boolean isShowNotification;


    public boolean isShowNotification() {
        return isShowNotification;
    }

    public void setShowNotification(boolean showNotification) {
        isShowNotification = showNotification;
    }
}

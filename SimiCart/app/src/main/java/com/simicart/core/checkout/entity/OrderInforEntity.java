package com.simicart.core.checkout.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.notification.NotificationEntity;

import org.json.JSONObject;

/**
 * Created by frank on 26/08/2016.
 */
public class OrderInforEntity extends SimiEntity {

    protected boolean isShowNotification;
    protected String mMessage;
    protected NotificationEntity mNotificationEntity;
    protected String invoiceNumber;

    @Override
    public void parse() {

        invoiceNumber = getData("invoice_number");

        if (hasKey("notification")) {
            JSONObject jsNotification = getJSONObjectWithKey(mJSON, "notification");
            if (null != jsNotification) {
                setShowNotification(true);
                mNotificationEntity = new NotificationEntity();
                mNotificationEntity.parse(jsNotification);
            }
        }
    }

    public boolean isShowNotification() {
        return isShowNotification;
    }

    public void setShowNotification(boolean showNotification) {
        isShowNotification = showNotification;
    }

    public NotificationEntity getNotificationEntity() {
        return mNotificationEntity;
    }

    public void setNotificationEntity(NotificationEntity mNotificationEntity) {
        this.mNotificationEntity = mNotificationEntity;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}

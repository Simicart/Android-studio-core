package com.simicart.core.checkout.entity;

import com.simicart.core.base.model.entity.SimiEntity;

/**
 * Created by frank on 26/08/2016.
 */
public class OrderInforEntity extends SimiEntity {

    protected boolean isShowNotification;
    protected String invoiceNumber;

    @Override
    public void parse() {
        invoiceNumber = getData("invoice_number");
    }

    public boolean isShowNotification() {
        return isShowNotification;
    }

    public void setShowNotification(boolean showNotification) {
        isShowNotification = showNotification;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}

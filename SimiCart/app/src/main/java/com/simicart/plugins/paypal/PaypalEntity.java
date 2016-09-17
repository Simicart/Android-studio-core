package com.simicart.plugins.paypal;

import java.io.Serializable;

/**
 * Created by frank on 9/12/16.
 */
public class PaypalEntity implements Serializable {

    private String mTotalPrice;
    private String mClientID;
    private boolean isSandbox;
    private String mBNCode;
    private String mInvoiceNumber;

    public String getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        mTotalPrice = totalPrice;
    }

    public String getClientID() {
        return mClientID;
    }

    public void setClientID(String clientID) {
        mClientID = clientID;
    }

    public boolean isSandbox() {
        return isSandbox;
    }

    public void setSandbox(boolean sandbox) {
        isSandbox = sandbox;
    }

    public String getBNCode() {
        return mBNCode;
    }

    public void setBNCode(String BNCode) {
        mBNCode = BNCode;
    }

    public String getInvoiceNumber() {
        return mInvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        mInvoiceNumber = invoiceNumber;
    }
}

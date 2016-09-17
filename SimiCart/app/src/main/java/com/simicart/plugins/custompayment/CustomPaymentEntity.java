package com.simicart.plugins.custompayment;

import com.simicart.core.base.model.entity.SimiEntity;

/**
 * Created by frank on 30/08/2016.
 */
public class CustomPaymentEntity extends SimiEntity {
    protected final String URL_CHECK = "url_check";
    protected final String TITLE_URL_ACTION = "title_url_action";
    protected final String PAYMENTMETHOD = "paymentmethod";
    protected final String URL_REDIRECT = "url_redirect";
    protected final String URL_SUCCESS = "url_success";
    protected final String URL_FAIL = "url_fail";
    protected final String URL_CANCEL = "url_cancel";
    protected final String URL_ERROR = "url_error";
    protected final String MESSAGE_SUCCESS = "message_success";
    protected final String MESSAGE_FAIL = "message_fail";
    protected final String MESSAGE_CANCEL = "message_cancel";
    protected final String MESSAGE_ERROR = "message_error";
    protected final String ISCHECKURL = "ischeckurl";
    protected String mPaymentMethod;
    protected String mUrlRedirect;
    protected String mUrlSuccess;
    protected String mUrlFail;
    protected String mUrlCancel;
    protected String mUrlError;
    protected String mMessageSuccess;
    protected String mMessageFail;
    protected String mMessageCancel;
    protected String mMessageError;
    protected String mCheckUrl;
    protected String mUrlCheck;
    protected String mTitleUrlAction;

    @Override
    public void parse() {

    }

    public String getMessageCancel() {
        return mMessageCancel;
    }

    public void setMessageCancel(String mMessageCancel) {
        this.mMessageCancel = mMessageCancel;
    }

    public String getMessageError() {
        return mMessageError;
    }

    public void setMessageError(String mMessageError) {
        this.mMessageError = mMessageError;
    }

    public String getMessageFail() {
        return mMessageFail;
    }

    public void setMessageFail(String mMessageFail) {
        this.mMessageFail = mMessageFail;
    }

    public String getMessageSuccess() {
        return mMessageSuccess;
    }

    public void setMessageSuccess(String mMessageSuccess) {
        this.mMessageSuccess = mMessageSuccess;
    }

    public String getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(String mPaymentMethod) {
        this.mPaymentMethod = mPaymentMethod;
    }

    public String getTitleUrlAction() {
        return mTitleUrlAction;
    }

    public void setTitleUrlAction(String mTitleUrlAction) {
        this.mTitleUrlAction = mTitleUrlAction;
    }

    public String getUrlCancel() {
        return mUrlCancel;
    }

    public void setUrlCancel(String mUrlCancel) {
        this.mUrlCancel = mUrlCancel;
    }

    public String getUrlCheck() {
        return mUrlCheck;
    }

    public void setUrlCheck(String mUrlCheck) {
        this.mUrlCheck = mUrlCheck;
    }

    public String getUrlError() {
        return mUrlError;
    }

    public void setUrlError(String mUrlError) {
        this.mUrlError = mUrlError;
    }

    public String getUrlFail() {
        return mUrlFail;
    }

    public void setUrlFail(String mUrlFail) {
        this.mUrlFail = mUrlFail;
    }

    public String getUrlRedirect() {
        return mUrlRedirect;
    }

    public void setUrlRedirect(String mUrlRedirect) {
        this.mUrlRedirect = mUrlRedirect;
    }

    public String getUrlSuccess() {
        return mUrlSuccess;
    }

    public void setUrlSuccess(String mUrlSuccess) {
        this.mUrlSuccess = mUrlSuccess;
    }
}

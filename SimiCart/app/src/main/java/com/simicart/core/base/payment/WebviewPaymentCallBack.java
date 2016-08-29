package com.simicart.core.base.payment;

/**
 * Created by frank on 8/28/16.
 */
public interface WebviewPaymentCallBack {

    public boolean onSuccess(String url);

    public boolean onFail(String url);

    public boolean onError(String url);

}

package com.simicart.core.checkout.delegate;

/**
 * Created by MSI on 31/08/2016.
 */
public interface TermConditionCallBack {

    public void onAgree(boolean isChecked);

    public void onOpenTermCondition();

}

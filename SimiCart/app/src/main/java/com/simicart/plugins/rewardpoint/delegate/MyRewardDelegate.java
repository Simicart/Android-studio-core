package com.simicart.plugins.rewardpoint.delegate;

import com.simicart.core.base.delegate.SimiDelegate;

import org.json.JSONObject;

/**
 * Created by Martial on 8/30/2016.
 */
public interface MyRewardDelegate extends SimiDelegate {

    public void showView(JSONObject data);

    public int isNotification();

    public int expireNotification();

    public String getPassbookLogo();

    public String getLoyaltyRedeem();

    public String getPassbookText();

    public String getPassbookBackground();

    public String getPassbookForeground();

    public String getPassbookBarcode();

}

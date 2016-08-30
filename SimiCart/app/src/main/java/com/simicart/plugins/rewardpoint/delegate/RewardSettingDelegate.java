package com.simicart.plugins.rewardpoint.delegate;

import com.simicart.core.base.delegate.SimiDelegate;

/**
 * Created by Martial on 8/29/2016.
 */
public interface RewardSettingDelegate extends SimiDelegate {

    public boolean isPointUpdate();

    public boolean isPointTransaction();

}

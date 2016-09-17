package com.simicart.plugins.rewardpoint.controller;

import android.view.View;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.common.KeyData;
import com.simicart.plugins.rewardpoint.delegate.MyRewardDelegate;
import com.simicart.plugins.rewardpoint.fragment.RewardCardFragment;
import com.simicart.plugins.rewardpoint.fragment.RewardHistoryFragment;
import com.simicart.plugins.rewardpoint.fragment.RewardSettingFragment;
import com.simicart.plugins.rewardpoint.model.ModelRewardPoint;

import java.util.HashMap;

/**
 * Created by Martial on 8/30/2016.
 */
public class MyRewardController extends SimiController {

    protected View.OnClickListener onClickHistory;
    protected View.OnClickListener onClickSetting;
    protected View.OnClickListener onClickCard;

    protected MyRewardDelegate mDelegate;

    public void setDelegate(MyRewardDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onStart() {
        handleEvent();
        requestMyReward();
    }

    @Override
    public void onResume() {
        mDelegate.showView(mModel.getDataJSON());
    }

    protected void requestMyReward() {
        mDelegate.showLoading();
        mModel = new ModelRewardPoint();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                try {
                    mDelegate.showView(mModel.getDataJSON());
                } catch (Exception e) {
                }
            }
        });
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissLoading();
                SimiNotify.getInstance().showNotify(error.getMessage());
            }
        });
        mModel.request();
    }

    protected void handleEvent() {
        onClickHistory = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardHistoryFragment rewardHistory = new RewardHistoryFragment();
                SimiManager.getIntance()
                        .replacePopupFragment(rewardHistory);
            }
        };
        onClickSetting = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hmData = new HashMap<String, Object>();
                hmData.put(KeyData.REWARD_POINT.EXPIRE_NOTIFICATION, mDelegate.expireNotification());
                hmData.put(KeyData.REWARD_POINT.IS_NOTIFICATION, mDelegate.isNotification());
                RewardSettingFragment fragment = RewardSettingFragment
                        .newInstance(new SimiData(hmData));
                SimiManager.getIntance().replacePopupFragment(fragment);
            }
        };
        onClickCard = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hmData = new HashMap<String, Object>();
                hmData.put(KeyData.REWARD_POINT.LOYALTY_REDEEM, mDelegate.getLoyaltyRedeem());
                hmData.put(KeyData.REWARD_POINT.PASSBOOK_BACKGROUND, mDelegate.getPassbookBackground());
                hmData.put(KeyData.REWARD_POINT.PASSBOOK_BARCODE, mDelegate.getPassbookBarcode());
                hmData.put(KeyData.REWARD_POINT.PASSBOOK_FOREGROUND, mDelegate.getPassbookForeground());
                hmData.put(KeyData.REWARD_POINT.PASSBOOK_LOGO, mDelegate.getPassbookLogo());
                hmData.put(KeyData.REWARD_POINT.PASSBOOK_TEXT, mDelegate.getPassbookText());
                RewardCardFragment rewardCard = RewardCardFragment
                        .newInstance(new SimiData(hmData));
                SimiManager.getIntance().replacePopupFragment(rewardCard);
            }
        };
    }

    public View.OnClickListener getOnClickCard() {
        return onClickCard;
    }

    public View.OnClickListener getOnClickHistory() {
        return onClickHistory;
    }

    public View.OnClickListener getOnClickSetting() {
        return onClickSetting;
    }
}

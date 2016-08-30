package com.simicart.plugins.rewardpoint.controller;

import android.view.View;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.plugins.rewardpoint.delegate.RewardSettingDelegate;
import com.simicart.plugins.rewardpoint.fragment.MyRewardFragment;
import com.simicart.plugins.rewardpoint.model.ModelRewardSetting;

/**
 * Created by Martial on 8/29/2016.
 */
public class RewardSettingController extends SimiController {

    protected RewardSettingDelegate mDelegate;
    protected View.OnClickListener onSaveClick;

    public void setDelegate(RewardSettingDelegate delegate) {
        mDelegate = delegate;
    }

    public View.OnClickListener getOnSaveClick() {
        return onSaveClick;
    }

    @Override
    public void onStart() {
        onSaveClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean point_check_update = mDelegate.isPointUpdate();
                boolean point_check_transaction = mDelegate.isPointTransaction();
                int point_update = 0;
                int point_transaction = 0;
                if (point_check_update == true) {
                    point_update = 1;
                } else {
                    point_update = 0;
                }
                if (point_check_transaction == true) {
                    point_transaction = 1;
                } else {
                    point_transaction = 0;
                }
                mDelegate.showDialogLoading();
                mModel = new ModelRewardSetting();
                mModel.setSuccessListener(new ModelSuccessCallBack() {
                    @Override
                    public void onSuccess(SimiCollection collection) {
                        mDelegate.dismissDialogLoading();
                        MyRewardFragment rewardPoint = new MyRewardFragment();
                        SimiManager.getIntance().replacePopupFragment(rewardPoint);
                    }
                });
                mModel.setFailListener(new ModelFailCallBack() {
                    @Override
                    public void onFail(SimiError error) {
                        mDelegate.dismissDialogLoading();
                        SimiNotify.getInstance().showNotify(error.getMessage());
                    }
                });
                mModel.addBody("expire_notification", String.valueOf(point_transaction));
                mModel.addBody("is_notification", String.valueOf(point_update));
                mModel.request();
            }
        };
    }

    @Override
    public void onResume() {

    }

}

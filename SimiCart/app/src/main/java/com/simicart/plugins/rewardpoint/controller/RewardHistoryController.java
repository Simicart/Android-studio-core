package com.simicart.plugins.rewardpoint.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.plugins.rewardpoint.model.ModelRewardHistory;

/**
 * Created by Martial on 8/29/2016.
 */
public class RewardHistoryController extends SimiController {

    protected SimiDelegate mDelegate;

    public void setDelegate(SimiDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onStart() {
        mDelegate.showLoading();
        mModel = new ModelRewardHistory();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.updateView(mModel.getCollection());
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

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }
}

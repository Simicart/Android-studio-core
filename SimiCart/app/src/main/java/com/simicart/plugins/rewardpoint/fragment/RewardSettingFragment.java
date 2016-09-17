package com.simicart.plugins.rewardpoint.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.block.RewardSettingBlock;
import com.simicart.plugins.rewardpoint.controller.RewardSettingController;

public class RewardSettingFragment extends SimiFragment {

    protected RewardSettingBlock mBlock;
    protected RewardSettingController mController;
    protected boolean is_notification;
    protected boolean expire_notification;

    public static RewardSettingFragment newInstance(SimiData data) {

        RewardSettingFragment fragment = new RewardSettingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(
                Rconfig.getInstance().layout(
                        "plugins_rewardpoint_settingfragment"), container,
                false);

        if (getArguments() != null) {
            int isNotification = (int) getValueWithKey(KeyData.REWARD_POINT.IS_NOTIFICATION);
            int expireNotification = (int) getValueWithKey(KeyData.REWARD_POINT.EXPIRE_NOTIFICATION);
            if (isNotification == 1) {
                this.is_notification = true;
            } else {
                this.is_notification = false;
            }
            if (expireNotification == 1) {
                this.expire_notification = true;
            } else {
                this.expire_notification = false;
            }
        }

        mBlock = new RewardSettingBlock(rootView, getActivity());
        mBlock.setIsNotification(is_notification);
        mBlock.setExpireNotification(expire_notification);
        mBlock.initView();
        mController = new RewardSettingController();
        mController.setDelegate(mBlock);
        mController.onStart();
        mBlock.onButtonSaveClick(mController.getOnSaveClick());

        return rootView;
    }

}

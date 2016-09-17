package com.simicart.plugins.rewardpoint.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.block.MyRewardBlock;
import com.simicart.plugins.rewardpoint.controller.MyRewardController;

public class MyRewardFragment extends SimiFragment {

    protected MyRewardBlock mBlock;
    protected MyRewardController mController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(
                Rconfig.getInstance().layout(
                        "plugins_rewardpoint_rewardpointfragment"), container,
                false);

        mBlock = new MyRewardBlock(rootView, getActivity());
        mBlock.initView();
        if (null == mController) {
            mController = new MyRewardController();
            mController.setDelegate(mBlock);
            mController.onStart();
        } else {
            mController.setDelegate(mBlock);
            mController.onResume();
        }
        mBlock.onClickHistory(mController.getOnClickHistory());
        mBlock.onClickSetting(mController.getOnClickSetting());
        mBlock.onClickCard(mController.getOnClickCard());

        return rootView;
    }

}

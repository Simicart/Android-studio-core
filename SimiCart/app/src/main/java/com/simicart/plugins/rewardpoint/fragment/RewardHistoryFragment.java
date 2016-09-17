package com.simicart.plugins.rewardpoint.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.block.RewardHistoryBlock;
import com.simicart.plugins.rewardpoint.controller.RewardHistoryController;

public class RewardHistoryFragment extends SimiFragment {

    protected RewardHistoryBlock mBlock;
    protected RewardHistoryController mController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(
                Rconfig.getInstance().layout("plugins_rewardpoint_historyfragment"), container, false);

        mBlock = new RewardHistoryBlock(rootView, getActivity());
        mBlock.initView();
        if (null == mController) {
            mController = new RewardHistoryController();
            mController.setDelegate(mBlock);
            mController.onStart();
        } else {
            mController.setDelegate(mBlock);
            mController.onResume();
        }

        return rootView;
    }

}

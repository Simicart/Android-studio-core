package com.simicart.plugins.rewardpoint.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.adapter.AdapterListviewHistory;
import com.simicart.plugins.rewardpoint.block.RewardHistoryBlock;
import com.simicart.plugins.rewardpoint.controller.RewardHistoryController;
import com.simicart.plugins.rewardpoint.entity.ItemHistory;
import com.simicart.plugins.rewardpoint.model.ModelRewardHistory;

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
		if(null == mController) {
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

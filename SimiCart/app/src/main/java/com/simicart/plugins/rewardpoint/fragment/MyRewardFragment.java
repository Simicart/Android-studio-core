package com.simicart.plugins.rewardpoint.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.fragment.MyAccountFragment;
import com.simicart.core.customer.fragment.SignInFragment;
import com.simicart.plugins.rewardpoint.block.MyRewardBlock;
import com.simicart.plugins.rewardpoint.controller.MyRewardController;
import com.simicart.plugins.rewardpoint.entity.ItemPointData;
import com.simicart.plugins.rewardpoint.model.ModelRewardPoint;

import java.util.HashMap;

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
		if(null == mController) {
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

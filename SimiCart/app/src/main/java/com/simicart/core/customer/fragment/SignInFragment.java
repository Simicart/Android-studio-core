package com.simicart.core.customer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.SignInBlock;
import com.simicart.core.customer.controller.SignInController;

import java.util.HashMap;

public class SignInFragment extends SimiFragment {

	protected SignInBlock mBlock;
	protected SignInController mController;
	protected boolean isCheckout = false;// sign in into checkout
	protected String mEmail;
	protected String mPassword;
	protected int requestCode;
	protected int resultCode;
	protected Intent data;

	public static SignInFragment newInstance(SimiData data) {
		SignInFragment fragment = new SignInFragment();
		Bundle bundle= new Bundle();
		bundle.putParcelable(KEY_DATA, data);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		if (DataLocal.isLanguageRTL) {
			view = inflater.inflate(
					Rconfig.getInstance().layout("rtl_core_sign_in_layout"),
					container, false);
		}else {
			view = inflater.inflate(
					Rconfig.getInstance().layout("core_fragment_signin"), container,
					false);
		}
		Context context = getActivity();

		if(mData != null) {
			mEmail = (String) getValueWithKey("email");
			mPassword = (String) getValueWithKey("password");
			isCheckout = (boolean) getValueWithKey("is_checkout");
		}

		mBlock = new SignInBlock(view, context);
		if( mEmail != null){
			mBlock.setEmail(mEmail);
		}
		if(mPassword != null){
			mBlock.setPassword(mPassword);
		}
		mBlock.initView();

		if (null == mController) {
			mController = new SignInController();
			mController.setDelegate(mBlock);
			mController.setCheckout(isCheckout);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}

		mBlock.setCreateAccClicker(mController.getCreateAccClicker());
		mBlock.setForgotPassClicker(mController.getForgotPassClicker());
		mBlock.setSignInClicker(mController.getSignInClicker());
		mBlock.setRememberPassClicker(mController.getOnCheckBox());

		return view;
	}

	public SignInController getController() {
		return mController;
	}

	public boolean isCheckout() {
		return isCheckout;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		HashMap<String, Object> hmData = new HashMap<>();
		hmData.put(KeyData.FACEBOOK.REQUEST_CODE, requestCode);
		hmData.put(KeyData.FACEBOOK.RESULT_CODE, resultCode);
		hmData.put(KeyData.FACEBOOK.INTENT, data);
		hmData.put(Constants.METHOD, "onActivityResult");
		SimiEvent.dispatchEvent(KeyEvent.FACEBOOK_EVENT.FACEBOOK_LOGIN_FRAGMENT, hmData);
	}
}

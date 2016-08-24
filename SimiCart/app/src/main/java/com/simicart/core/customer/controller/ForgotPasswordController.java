package com.simicart.core.customer.controller;

import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.customer.delegate.ForgotPasswordDelegate;
import com.simicart.core.customer.model.ForgotPasswordModel;

public class ForgotPasswordController extends SimiController {

	protected ForgotPasswordDelegate mDelegate;
	protected OnClickListener mClicker;

	public void setDelegate(ForgotPasswordDelegate delegate) {
		mDelegate = delegate;
	}

	public OnClickListener getClicker() {
		return mClicker;
	}

	@Override
	public void onStart() {
		mClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {

				onSend(v);
			}
		};

	}

	protected void onSend(View v) {
		Utils.hideKeyboard(v);
		String email = mDelegate.getEmail();
		mDelegate.showDialogLoading();
		mModel = new ForgotPasswordModel();
		mModel.setSuccessListener(new ModelSuccessCallBack() {
			@Override
			public void onSuccess(SimiCollection collection) {
				mDelegate.dismissDialogLoading();
				SimiManager.getIntance().backPreviousFragment();
			}
		});
		mModel.setFailListener(new ModelFailCallBack() {
			@Override
			public void onFail(SimiError error) {
				mDelegate.dismissDialogLoading();
				SimiNotify.getInstance().showNotify(error.getMessage());
			}
		});
		mModel.addBody("user_email", email);
		mModel.request();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

}

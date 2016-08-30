package com.simicart.plugins.download.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.plugins.download.delegate.DownloadDelegate;
import com.simicart.plugins.download.model.DownloadModel;

public class DownloadController extends SimiController{
	protected DownloadDelegate mDelegate;
	
	public void setDelegate(DownloadDelegate mDelegate) {
		this.mDelegate = mDelegate;
	}
	
	@Override
	public void onStart() {
		requestGetDownLoad();
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}
	
	protected void requestGetDownLoad(){
		mDelegate.showLoading();
		mModel = new DownloadModel();
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
				mDelegate.getMessage(error.getMessage());
			}
		});
		mModel.request();
	}

}

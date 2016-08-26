package com.simicart.plugins.locator.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.plugins.locator.delegate.StoreLocatorMapDelegate;
import com.simicart.plugins.locator.entity.StoreObject;
import com.simicart.plugins.locator.model.ModelLocator;

import java.util.ArrayList;

public class StoreLocatorMapController extends SimiController {
	
	protected StoreLocatorMapDelegate mDelegate;
	
	public void setDelegate(StoreLocatorMapDelegate delegate) {
		mDelegate = delegate;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		//requestGetStore();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		//mDelegate.updateView(mModel.getCollection());
	}
	
	protected void requestGetStore() {
//		mDelegate.showDialogLoading();
		mModel = new ModelLocator();
		mModel.setSuccessListener(new ModelSuccessCallBack() {
			@Override
			public void onSuccess(SimiCollection collection) {
				mDelegate.updateView(mModel.getCollection());
			}
		});
		mModel.setFailListener(new ModelFailCallBack() {
			@Override
			public void onFail(SimiError error) {
				SimiNotify.getInstance().showNotify(SimiTranslator.getInstance().translate(
						"No store match with your searching"));
			}
		});
		mModel.addBody("limit", "5000");
		mModel.addBody("offset", "0");
		mModel.request();
	}
	
	public void addMarker(ArrayList<StoreObject> listStores) {
		mDelegate.addMarkerToMap(listStores);
	}

}

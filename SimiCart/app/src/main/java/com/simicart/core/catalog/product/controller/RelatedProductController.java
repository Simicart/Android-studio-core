package com.simicart.core.catalog.product.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.model.RelatedProductModel;

public class RelatedProductController extends SimiController {

	protected SimiDelegate mDelegate;
	protected String mID;

	public void setDelegate(SimiDelegate delegate) {
		mDelegate = delegate;
	}

	public void setProductId(String id) {
		mID = id;
	}

	@Override
	public void onStart() {
		mDelegate.showLoading();
		mModel.setSuccessListener(new ModelSuccessCallBack() {
			@Override
			public void onSuccess(SimiCollection collection) {
				mDelegate.dismissLoading();
				mDelegate.updateView(mModel.getCollection());
			}
		});
		mModel = new RelatedProductModel();
		mModel.addBody("product_id", mID);
		mModel.addBody("limit", "15");
		mModel.addBody("width", "300");
		mModel.addBody("height", "300");
		mModel.request();
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}

}

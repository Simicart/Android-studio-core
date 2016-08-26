package com.simicart.core.home.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.home.delegate.CategoryHomeDelegate;
import com.simicart.core.home.model.CategoryHomeModel;

import java.util.ArrayList;

public class CategoryHomeController extends SimiController {

	protected CategoryHomeDelegate mDelegate;
	protected ArrayList<Category> listCategory = new ArrayList<Category>();

	public CategoryHomeController() {
	}

	public void setDelegate(CategoryHomeDelegate delegate) {
		this.mDelegate = delegate;
	}

	public CategoryHomeDelegate getDelegate() {
		return mDelegate;
	}

	@Override
	public void onStart() {
		// mDelegate.showLoading();
		mModel.setSuccessListener(new ModelSuccessCallBack() {
			@Override
			public void onSuccess(SimiCollection collection) {
				mDelegate.updateView(mModel.getCollection());
			}
		});
		mModel = new CategoryHomeModel();
		mModel.request();
	}

	@Override
	public void onResume() {
		mDelegate.updateView(mModel.getCollection());
	}

}

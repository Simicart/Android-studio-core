package com.simicart.core.catalog.product.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.catalog.product.fragment.ProductDetailImageFragment;

import java.util.HashMap;

public class ProductDetailChildeAdapter extends FragmentPagerAdapter {
	protected String[] images = new String[] {};

	private int mCount;
	protected ProductDetailParentController mParentController;

	public void setDelegate(ProductDetailParentController delegate) {
		mParentController = delegate;
	}

	public ProductDetailChildeAdapter(FragmentManager fmChild, String[] Images) {
		super(fmChild);
		this.images = Images;
		this.mCount = images.length;
	}

	@Override
	public Fragment getItem(int position) {
		if (images.length > 0 && images[position] != null) {
			String url = images[position];
			HashMap<String,Object> hm = new HashMap<>();
			hm.put("url",url);
			SimiData data = new SimiData(hm);
			ProductDetailImageFragment fragment = ProductDetailImageFragment.newInstance(data);
			fragment.setDelegate(mParentController);
			return fragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return mCount;
	}

}

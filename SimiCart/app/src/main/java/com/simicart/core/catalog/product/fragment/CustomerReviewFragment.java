package com.simicart.core.catalog.product.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.product.block.CustomerReviewBlock;
import com.simicart.core.catalog.product.controller.CustomerReviewController;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

public class CustomerReviewFragment extends SimiFragment {

    protected ArrayList<Integer> mRatingStar;
    protected String mID;
    protected CustomerReviewBlock mBlock = null;
    protected CustomerReviewController mController = null;
    protected Product mProduct;

    public static CustomerReviewFragment newInstance(SimiData data) {
        CustomerReviewFragment fragment = new CustomerReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                Rconfig.getInstance().layout("core_information_customer_review_layout"),
                container, false);
        Context context = getActivity();

        if (mData != null) {
            mProduct = (Product) getValueWithKey(Constants.KeyData.PRODUCT);
            if (mProduct != null) {
                mID = mProduct.getId();
                mRatingStar = mProduct.getStar();
            }
        }

        mBlock = new CustomerReviewBlock(view, context);
        mBlock.setProduct(mProduct);

        // event
//		CacheBlock cacheBlock = new CacheBlock();
//		cacheBlock.setBlock(mBlock);
//		EventBlock event = new EventBlock();
//		event.dispatchEvent(
//				"com.simicart.core.catalog.product.block.CustomerReviewBlock",
//				view, mContext, cacheBlock);
//		mBlock = (CustomerReviewBlock) cacheBlock.getBlock();

        mBlock.initView();
        if (mController == null) {
            mController = new CustomerReviewController();
            mController.setProductId(mID);
            mController.setDelegate(mBlock);
            mController.setRatingStar(mRatingStar);
            mController.onStart();
        } else {
            mController.setDelegate(mBlock);
            mController.onResume();
        }

        mBlock.setonScroll(mController.getScroller());

        return view;

    }


}

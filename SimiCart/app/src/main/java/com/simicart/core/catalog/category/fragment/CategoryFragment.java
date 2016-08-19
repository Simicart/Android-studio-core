package com.simicart.core.catalog.category.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.category.block.CategoryBlock;
import com.simicart.core.catalog.category.block.CategoryDetailBlock;
import com.simicart.core.catalog.category.controller.CategoryController;
import com.simicart.core.catalog.category.controller.CategoryDetailController;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class CategoryFragment extends SimiFragment {

    protected String mCategoryID;
    protected String mCategoryName;
    protected CategoryController mCategoryController;
    protected CategoryBlock mCategoryBlock;

    public static CategoryFragment newInstance(SimiData data) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setScreenName("Category Screen");
        rootView = inflater.inflate(
                Rconfig.getInstance().layout("core_category_layout"),
                container, false);
        Context context = getActivity();
        if (mData != null) {
            mCategoryID = (String) getValueWithKey("category_id");
            mCategoryName = (String) getValueWithKey("category_name");
        }

        // list categories
        mCategoryBlock = new CategoryBlock(rootView, context);
        mCategoryBlock.setCategoryName(mCategoryName);
        mCategoryBlock.initView();
        if (null == mCategoryController) {
            // controller request List Category,show loading
            mCategoryController = new CategoryController();
            mCategoryController.setDelegate(mCategoryBlock);
            mCategoryController.setCategoryID(mCategoryID);
            mCategoryController.onStart();
        } else {
            mCategoryController.setDelegate(mCategoryBlock);
            mCategoryController.onResume();
        }

        // save
        //DataPreferences.saveCateID(mCategoryID, mCategoryName);

        return rootView;
    }

}

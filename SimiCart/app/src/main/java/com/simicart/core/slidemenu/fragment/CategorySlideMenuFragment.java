package com.simicart.core.slidemenu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.category.block.CategoryBlock;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.block.CategorySlideMenuBlock;
import com.simicart.core.slidemenu.controller.CategorySlideMenuController;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategorySlideMenuFragment extends SimiFragment {

    public static CategorySlideMenuFragment instance;

    protected CategorySlideMenuBlock mBlock;
    protected CategorySlideMenuController mController;

    protected String mCategoryID;
    protected String mCategoryName;
    protected SlideMenuFragment mNavigationDrawerFragment;

    public static CategorySlideMenuFragment newInstance(SimiData data) {
        if(instance == null) {
            instance = new CategorySlideMenuFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_DATA, data);
            instance.setArguments(bundle);
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(Rconfig.getInstance().layout("core_fragment_cat_slide_menu"), container, false);
        rootView.setBackgroundColor(AppColorConfig.getInstance().getMenuBackground());

        if(mData != null) {
            mCategoryID = (String) getValueWithKey("category_id");
            mCategoryName = (String) getValueWithKey("category_name");
            mNavigationDrawerFragment = (SlideMenuFragment) getValueWithKey("navigation_drawer");
        }

        mBlock = new CategorySlideMenuBlock(rootView, getActivity());
        mBlock.setCategoryName(mCategoryName);
        mBlock.initView();
        if(mController == null) {
            mController = new CategorySlideMenuController();
            mController.setCategoryName(mCategoryName);
            mController.setCategoryID(mCategoryID);
            mController.setDelegate(mBlock);
            mController.onStart();
        } else {
            mController.setCategoryID(mCategoryID);
            mController.setDelegate(mBlock);
            mController.onResume();
        }
        mBlock.onBackClick(mController.getOnBackClick());

        SimiManager.getIntance().setCategorySlideMenuController(mController);

        return rootView;
    }
}

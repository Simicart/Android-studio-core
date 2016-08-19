package com.simicart.core.catalog.categorydetail.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.categorydetail.block.CategoryDetailBlock;
import com.simicart.core.catalog.categorydetail.controller.CategoryDetailController;
import com.simicart.core.config.Rconfig;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategoryDetailFragment extends SimiFragment {

    public static String ALL = "all";
    public static String SEARCH = "search";
    public static String CATE = "cate";
    public static String CUSTOM = "custom";

    public static CategoryDetailFragment newInstance(SimiData data) {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected CategoryDetailController mController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_category_detail");
        rootView = inflater.inflate(idView, null, false);

        CategoryDetailBlock block = new CategoryDetailBlock(rootView, getActivity());
        block.initView();

        if (null == mController) {
            mController = new CategoryDetailController();
            mController.setData(mHashMapData);
            mController.setDelegate(block);
            mController.onStart();

        } else {

            mController.onResume();
        }


        return rootView;
    }
}

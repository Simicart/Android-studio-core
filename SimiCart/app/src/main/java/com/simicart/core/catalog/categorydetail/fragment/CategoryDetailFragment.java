package com.simicart.core.catalog.categorydetail.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.categorydetail.block.CategoryDetailBlock;
import com.simicart.core.catalog.categorydetail.controller.CategoryDetailController;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.component.SearchComponent;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategoryDetailFragment extends SimiFragment {

    public static String ALL = "all";
    public static String SEARCH = "search";
    public static String CATE = "cate";
    public static String CUSTOM = "custom";

    protected String tagView;
    protected CategoryDetailBlock mBlock;
    protected CategoryDetailController mController;

    public static CategoryDetailFragment newInstance(SimiData data) {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_category_detail");
        rootView = inflater.inflate(idView, null, false);

        mBlock = new CategoryDetailBlock(rootView, getActivity());
        if (!Utils.validateString(tagView)) {
            if (!DataLocal.isTablet) {
                tagView = Constants.TAG_LISTVIEW;
            } else {
                tagView = Constants.TAG_GRIDVIEW;
            }
        }
        mBlock.setTagView(tagView);
        mBlock.initView();

        if (null == mController) {
            mController = new CategoryDetailController();
            mController.setData(mHashMapData);
            mController.setDelegate(mBlock);
            mController.onStart();

        } else {
            mController.setDelegate(mBlock);
            mController.onResume();
        }
        if (!DataLocal.isTablet) {
            mBlock.onChangeViewClick(mController.getOnChangeViewClick());
            // initial search
            SearchComponent searchComponent = new SearchComponent();
            View searchView = searchComponent.createView();
            LinearLayout llSearch = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_search"));
            llSearch.addView(searchView);
        }
        mBlock.onListScroll(mController.getOnListScroll());
        mBlock.setSortListener(mController.getSortListener());
        mBlock.setFilterListener(mController.getFilterListener());

        return rootView;
    }
}

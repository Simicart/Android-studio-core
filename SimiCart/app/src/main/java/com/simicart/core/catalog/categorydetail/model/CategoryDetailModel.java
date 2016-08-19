package com.simicart.core.catalog.categorydetail.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.catalog.categorydetail.fragment.CategoryDetailFragment;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategoryDetailModel extends SimiModel {


    protected String mType;
    protected String mCustomUrl;

    public CategoryDetailModel(String type) {
        mType = type;
    }


    @Override
    protected void setUrlAction() {
        if (mType.equals(CategoryDetailFragment.CATE)) {
            mUrlAction = "connector/catalog/get_category_products";
        } else if (mType.equals(CategoryDetailFragment.SEARCH)) {
            mUrlAction = "connector/catalog/search_products";
        } else if (mType.equals(CategoryDetailFragment.ALL)) {
            mUrlAction = "connector/catalog/get_all_products";
        } else {
            mUrlAction = mCustomUrl;
        }
    }

    @Override
    protected void parseData() {
        super.parseData();
        Log.e("CategoryDetailModel ","DATA " + mJSON.toString());
    }

    public void setCustomUrl(String customUrl) {
        mCustomUrl = customUrl;
    }
}

package com.simicart.core.catalog.category.controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.delegate.SortDelegate;
import com.simicart.core.catalog.category.entity.Sort;
import com.simicart.core.catalog.listproducts.fragment.ProductListFragment;
import com.simicart.core.catalog.listproducts.fragment.SearchListFragment;
import com.simicart.core.common.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

public class SortController extends SimiController {

    ArrayList<Sort> mListSort;
    protected String mCategoryID;
    protected String mCategoryName;
    protected String mSortType;
    protected String mQuery;
    protected SortDelegate mDelegate;
    protected OnItemClickListener mItemClicker;
    protected OnItemClickListener mCateItemClicker;
    protected JSONObject jsonFilter;
    protected String tag_sort;
    private String url_search = "";
    private String key = "";

    public void setKey(String key) {
        this.key = key;
    }

    public void setUrl_search(String url_search) {
        this.url_search = url_search;
    }

    public void setJSONFilter(JSONObject json) {
        jsonFilter = json;
    }

    public void setTag_sort(String tag_sort) {
        this.tag_sort = tag_sort;
    }

    public void setDelegate(SortDelegate delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public void onStart() {
        mDelegate.setSort_option(mSortType);
        mDelegate.setListSort(mListSort);

        mItemClicker = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SimiFragment fragment = null;
                if (Utils.validateString(mQuery)) {
                    fragment = SearchListFragment
                            .newInstance(mQuery, mCategoryID, mCategoryName, mListSort.get(position).getId() + "", jsonFilter, tag_sort);
                } else {
                    fragment = ProductListFragment
                            .newInstance(mCategoryID, mCategoryName, mListSort.get(position).getId() + "", jsonFilter, tag_sort);
                }
                SimiManager.getIntance().removeDialog();
                SimiManager.getIntance().replaceFragment(fragment);
                mDelegate.setSort_option(mListSort.get(position).getTitle());
            }
        }

        ;
    }

    @Override
    public void onResume() {
    }

    public OnItemClickListener getItemClicker() {
        return mItemClicker;
    }

    public void setCategoryID(String mCategoryID) {
        this.mCategoryID = mCategoryID;
    }

    public void setCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public void setSortType(String mSortType) {
        this.mSortType = mSortType;
    }

    public void setQuery(String mQuery) {
        this.mQuery = mQuery;
    }

    public void setListSort(ArrayList<Sort> listSort) {
        this.mListSort = listSort;
    }
}

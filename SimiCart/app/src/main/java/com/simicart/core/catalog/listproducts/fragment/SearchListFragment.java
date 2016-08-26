package com.simicart.core.catalog.listproducts.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.catalog.filter.FilterEvent;
import com.simicart.core.catalog.listproducts.block.ProductListBlock;
import com.simicart.core.catalog.listproducts.controller.SearchListController;
import com.simicart.core.catalog.listproducts.entity.TagSearch;
import com.simicart.core.catalog.listproducts.model.ConstantsSearch;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.block.SearchHomeBlock;

import org.json.JSONObject;

/**
 * Created by Simi on 11-Apr-16.
 */
public class SearchListFragment extends ProductListFragment {

    protected String mQuery = "None";
    protected SearchListController mSearchListController;

    public static SearchListFragment newInstance(String query, String catId, String catName, String sortId,
                                                 JSONObject jsonFilter, String tagView) {
        SearchListFragment fragment = new SearchListFragment();
   //     fragment.setTargetFragment(fragment, ConfigCheckout.TARGET_LISTPRODUCT);
        Bundle bundle = new Bundle();
//        setData(Constants.KeyData.QUERY, query, Constants.KeyData.TYPE_STRING,
//                bundle);
//        setData(Constants.KeyData.ID, catId, Constants.KeyData.TYPE_STRING, bundle);
//        setData(Constants.KeyData.NAME, catName, Constants.KeyData.TYPE_STRING,
//                bundle);
//        setData(Constants.KeyData.TAG, tagView, Constants.KeyData.TYPE_STRING,
//                bundle);
//        setData(Constants.KeyData.SORT_ID, sortId,
//                Constants.KeyData.TYPE_STRING, bundle);
//        if (jsonFilter != null) {
//            setData(Constants.KeyData.JSON_FILTER, jsonFilter.toString(),
//                    Constants.KeyData.TYPE_JSONOBJECT, bundle);
//        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(
                Rconfig.getInstance().layout("core_product_list_layout"),
                container, false);
        Context context = getActivity();

        // data
        if (getArguments() != null) {
//            mQuery = (String) getData(Constants.KeyData.QUERY,
//                    Constants.KeyData.TYPE_STRING, getArguments());
//            mSortID = (String) getData(Constants.KeyData.SORT_ID,
//                    Constants.KeyData.TYPE_STRING, getArguments());
//            mCatName = (String) getData(Constants.KeyData.NAME,
//                    Constants.KeyData.TYPE_STRING, getArguments());
//            mCatID = (String) getData(Constants.KeyData.ID,
//                    Constants.KeyData.TYPE_STRING, getArguments());
//            setScreenName("Search Screen");//tracking Screen
//            String json = (String) getData(Constants.KeyData.JSON_FILTER,
//                    Constants.KeyData.TYPE_JSONOBJECT, getArguments());
//            try {
//                jsonFilter = new JSONObject(json);
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            tagView = (String) getData(Constants.KeyData.TAG,
//                    Constants.KeyData.TYPE_STRING, getArguments());
            setTagView(tagView);
        }
        setListParam(ConstantsSearch.PARAM_QUERY, mQuery);
        setListParam(ConstantsSearch.PARAM_CATEGORY_ID, mCatID);
        setListParam(ConstantsSearch.PARAM_CATEGORY_NAME, mCatName);
        setListParam(ConstantsSearch.PARAM_SORT_OPTION, mSortID);

        // init search
        View searchView = rootView.findViewById(Rconfig.getInstance().id(
                "rlt_search"));
        if (DataLocal.isTablet) {
            searchView.setVisibility(View.GONE);
        } else {
            mSearchHomeBlock = new SearchHomeBlock(searchView, context);
            mSearchHomeBlock.setTag(TagSearch.TAG_LISTVIEW);
            mSearchHomeBlock.setCateID(mCatID);
            mSearchHomeBlock.setCatName(mCatName);
            mSearchHomeBlock.setQuery(mQuery);
            mSearchHomeBlock.initView();
        }

        mProductListBlock = new ProductListBlock(rootView, context);
        mProductListBlock.setCatName(mCatName);
        mProductListBlock.setCatID(mCatID);
        mProductListBlock.setTagView(this.tagView);
        mProductListBlock.initView();
        if (mSearchListController == null) {
            mSearchListController = new SearchListController();
            filterEvent = new FilterEvent(mSearchListController);
            mProductListBlock.setFilterEvent(filterEvent);
            mSearchListController.setQuery(mQuery);
            mSearchListController.setCatID(mCatID);
            mSearchListController.setCatName(mCatName);
            mSearchListController.setTagView(tagView);
            mSearchListController.setSortID(mSortID);
            mSearchListController.setJsonFilter(jsonFilter);
            mSearchListController.setList_param(list_param);
            mSearchListController.setDelegate(mProductListBlock);
            mSearchListController.onStart();
        } else {
            filterEvent = new FilterEvent(mSearchListController);
            mProductListBlock.setFilterEvent(filterEvent);
//            mSearchListController.setQuery(mQuery);
//            mSearchListController.setCatID(mCatID);
//            mSearchListController.setCatName(mCatName);
//            mSearchListController.setTagView(tagView);
//            mSearchListController.setSortID(mSortID);
//            mSearchListController.setJsonFilter(jsonFilter);
//            mSearchListController.setList_param(list_param);
            mSearchListController.setDelegate(mProductListBlock);
            mSearchListController.onResume();
        }

        mProductListBlock.setOnTourchChangeView(mSearchListController
                .getmOnTouchChangeViewData());
        mProductListBlock.setOnTourchToFilter(mSearchListController
                .getmOnTouchToFilter());
        mProductListBlock.setOnTourchToSort(mSearchListController.getmOnTouchToSort());
        mProductListBlock.setScrollListProduct(mSearchListController.getOnScrolListProduct());
        mProductListBlock.setOnTouchListenerGridview(mSearchListController
                .getmOnTouchGridview());

        return rootView;
    }
}

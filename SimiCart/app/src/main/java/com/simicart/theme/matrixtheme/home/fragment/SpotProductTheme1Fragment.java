package com.simicart.theme.matrixtheme.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.catalog.filter.FilterEvent;
import com.simicart.core.catalog.listproducts.block.ProductListBlock;
import com.simicart.core.catalog.listproducts.entity.TagSearch;
import com.simicart.core.catalog.listproducts.fragment.ProductListFragment;
import com.simicart.core.catalog.listproducts.model.ConstantsSearch;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.block.SearchHomeBlock;
import com.simicart.theme.matrixtheme.home.controller.SpotProductListTheme1Controller;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Simi on 14-Apr-16.
 */
public class SpotProductTheme1Fragment extends ProductListFragment {

    protected String mKey = "None";

    protected SpotProductListTheme1Controller mSpotProductListTheme1Controller;

    public static SpotProductTheme1Fragment newInstance(String key, String spotName, String sortId,
                                                        JSONObject jsonFilter, String tagView) {
        SpotProductTheme1Fragment fragment = new SpotProductTheme1Fragment();
        fragment.setTargetFragment(fragment, ConfigCheckout.TARGET_LISTPRODUCT);
        Bundle bundle = new Bundle();
        setData(Constants.KeyData.KEY, key, Constants.KeyData.TYPE_STRING,
                bundle);
        setData(Constants.KeyData.CAT_NAME, spotName, Constants.KeyData.TYPE_STRING,
                bundle);
        setData(Constants.KeyData.TAG, tagView, Constants.KeyData.TYPE_STRING,
                bundle);
        setData(Constants.KeyData.SORT_ID, sortId,
                Constants.KeyData.TYPE_STRING, bundle);
        if (jsonFilter != null) {
            setData(Constants.KeyData.JSON_FILTER, jsonFilter.toString(),
                    Constants.KeyData.TYPE_JSONOBJECT, bundle);
        }
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
            mKey = (String) getData(Constants.KeyData.KEY,
                    Constants.KeyData.TYPE_STRING, getArguments());
            mCatName = (String) getData(Constants.KeyData.CAT_NAME,
                    Constants.KeyData.TYPE_STRING, getArguments());
            mSortID = (String) getData(Constants.KeyData.SORT_ID,
                    Constants.KeyData.TYPE_STRING, getArguments());
            setScreenName("List Spot Product:" + mKey);//tracking Screen
            String json = (String) getData(Constants.KeyData.JSON_FILTER,
                    Constants.KeyData.TYPE_JSONOBJECT, getArguments());
            try {
                jsonFilter = new JSONObject(json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            tagView = (String) getData(Constants.KeyData.TAG,
                    Constants.KeyData.TYPE_STRING, getArguments());
            setTagView(tagView);
        }
        setListParam(ConstantsSearch.PARAM_KEY, mKey);
        setListParam(ConstantsSearch.PARAM_SORT_OPTION, mSortID);

        // init search
        View searchView = rootView.findViewById(Rconfig.getInstance().id(
                "rlt_search"));
        if (DataLocal.isTablet) {
            searchView.setVisibility(View.GONE);
        } else {
            mSearchHomeBlock = new SearchHomeBlock(searchView, context);
            mSearchHomeBlock.setTag(TagSearch.TAG_LISTVIEW);
            mSearchHomeBlock.setCatName(mCatName);
            mSearchHomeBlock.initView();
        }

        mProductListBlock = new ProductListBlock(rootView, context);
        mProductListBlock.setTagView(this.tagView);
        mProductListBlock.initView();
        if (mSpotProductListTheme1Controller == null) {
            mSpotProductListTheme1Controller = new SpotProductListTheme1Controller();
            filterEvent = new FilterEvent(mSpotProductListTheme1Controller);
            mProductListBlock.setFilterEvent(filterEvent);
            mSpotProductListTheme1Controller.setKey(mKey);
            mSpotProductListTheme1Controller.setTagView(tagView);
            mSpotProductListTheme1Controller.setSortID(mSortID);
            mSpotProductListTheme1Controller.setJsonFilter(jsonFilter);
            mSpotProductListTheme1Controller.setList_param(list_param);
            mSpotProductListTheme1Controller.setDelegate(mProductListBlock);
            mSpotProductListTheme1Controller.onStart();
        } else {
            filterEvent = new FilterEvent(mSpotProductListTheme1Controller);
            mProductListBlock.setFilterEvent(filterEvent);
//            mSpotProductListTheme1Controller.setKey(mKey);
//            mSpotProductListTheme1Controller.setTagView(tagView);
//            mSpotProductListTheme1Controller.setSortID(mSortID);
//            mSpotProductListTheme1Controller.setJsonFilter(jsonFilter);
//            mSpotProductListTheme1Controller.setList_param(list_param);
            mSpotProductListTheme1Controller.setDelegate(mProductListBlock);
            mSpotProductListTheme1Controller.onResume();
        }

        mProductListBlock.setOnTourchChangeView(mSpotProductListTheme1Controller
                .getmOnTouchChangeViewData());
        mProductListBlock.setOnTourchToFilter(mSpotProductListTheme1Controller
                .getmOnTouchToFilter());
        mProductListBlock.setOnTourchToSort(mSpotProductListTheme1Controller.getmOnTouchToSort());
        mProductListBlock.setScrollListProduct(mSpotProductListTheme1Controller.getOnScrolListProduct());
        mProductListBlock.setOnTouchListenerGridview(mSpotProductListTheme1Controller
                .getmOnTouchGridview());

        return rootView;
    }
}

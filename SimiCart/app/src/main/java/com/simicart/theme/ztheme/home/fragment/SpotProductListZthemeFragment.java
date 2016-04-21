package com.simicart.theme.ztheme.home.fragment;

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
import com.simicart.theme.ztheme.home.controller.SpotProductListZthemeController;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Simi on 14-Apr-16.
 */
public class SpotProductListZthemeFragment extends ProductListFragment {
    protected String mKey = "None";

    protected SpotProductListZthemeController mSpotProductListZthemeController;

    public static SpotProductListZthemeFragment newInstance(String key, String spotName, String sortId,
                                                            JSONObject jsonFilter, String tagView) {
        SpotProductListZthemeFragment fragment = new SpotProductListZthemeFragment();
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
        if (mSpotProductListZthemeController == null) {
            mSpotProductListZthemeController = new SpotProductListZthemeController();
            filterEvent = new FilterEvent(mSpotProductListZthemeController);
            mProductListBlock.setFilterEvent(filterEvent);
            mSpotProductListZthemeController.setKey(mKey);
            mSpotProductListZthemeController.setTagView(tagView);
            mSpotProductListZthemeController.setSortID(mSortID);
            mSpotProductListZthemeController.setJsonFilter(jsonFilter);
            mSpotProductListZthemeController.setList_param(list_param);
            mSpotProductListZthemeController.setDelegate(mProductListBlock);
            mSpotProductListZthemeController.onStart();
        } else {
            filterEvent = new FilterEvent(mSpotProductListZthemeController);
            mProductListBlock.setFilterEvent(filterEvent);
//            mSpotProductListZthemeController.setKey(mKey);
//            mSpotProductListZthemeController.setTagView(tagView);
//            mSpotProductListZthemeController.setSortID(mSortID);
//            mSpotProductListZthemeController.setJsonFilter(jsonFilter);
//            mSpotProductListZthemeController.setList_param(list_param);
            mSpotProductListZthemeController.setDelegate(mProductListBlock);
            mSpotProductListZthemeController.onResume();
        }

        mProductListBlock.setOnTourchChangeView(mSpotProductListZthemeController
                .getmOnTouchChangeViewData());
        mProductListBlock.setOnTourchToFilter(mSpotProductListZthemeController
                .getmOnTouchToFilter());
        mProductListBlock.setOnTourchToSort(mSpotProductListZthemeController.getmOnTouchToSort());
        mProductListBlock.setScrollListProduct(mSpotProductListZthemeController.getOnScrolListProduct());
        mProductListBlock.setOnTouchListenerGridview(mSpotProductListZthemeController
                .getmOnTouchGridview());

        return rootView;
    }
}

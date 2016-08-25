package com.simicart.core.catalog.listproducts.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.filter.FilterEvent;
import com.simicart.core.catalog.listproducts.block.ProductListBlock;
import com.simicart.core.catalog.listproducts.controller.ProductListController;
import com.simicart.core.catalog.listproducts.entity.TagSearch;
import com.simicart.core.catalog.listproducts.model.ConstantsSearch;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.block.SearchHomeBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simi on 12-Apr-16.
 */
public class ProductListFragment extends SimiFragment {

    protected String mCatID = "-1";
    protected String mCatName = "";
    protected String mSortID = "None";
    protected String tagView = "";
    protected JSONObject jsonFilter;
    protected FilterEvent filterEvent = null;
    protected Map<String, String> list_param = new HashMap<>();

    protected SearchHomeBlock mSearchHomeBlock;

    protected ProductListBlock mProductListBlock;
    protected ProductListController mProductListController;

    public static ProductListFragment newInstance(String catId, String catName, String sortId,
                                                  JSONObject jsonFilter, String tagView) {
        ProductListFragment fragment = new ProductListFragment();
       // fragment.setTargetFragment(fragment, ConfigCheckout.TARGET_LISTPRODUCT);
        Bundle bundle = new Bundle();
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
//            mSortID = (String) getData(Constants.KeyData.SORT_ID,
//                    Constants.KeyData.TYPE_STRING, getArguments());
//            mCatName = (String) getData(Constants.KeyData.NAME,
//                    Constants.KeyData.TYPE_STRING, getArguments());
//            mCatID = (String) getData(Constants.KeyData.ID,
//                    Constants.KeyData.TYPE_STRING, getArguments());
//            setScreenName("List Category ID:" + mCatID);//tracking Screen
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
            mSearchHomeBlock.setQuery("");
            mSearchHomeBlock.initView();
        }

        mProductListBlock = new ProductListBlock(rootView, context);
        mProductListBlock.setCatName(mCatName);
        mProductListBlock.setCatID(mCatID);
        mProductListBlock.setTagView(this.tagView);
        mProductListBlock.initView();
        if (mProductListController == null) {
            mProductListController = new ProductListController();
            filterEvent = new FilterEvent(mProductListController);
            mProductListBlock.setFilterEvent(filterEvent);
            mProductListController.setCatID(mCatID);
            mProductListController.setCatName(mCatName);
            mProductListController.setTagView(tagView);
            mProductListController.setSortID(mSortID);
            mProductListController.setJsonFilter(jsonFilter);
            mProductListController.setList_param(list_param);
            mProductListController.setDelegate(mProductListBlock);
            mProductListController.onStart();
        } else {
            filterEvent = new FilterEvent(mProductListController);
            mProductListBlock.setFilterEvent(filterEvent);
//            mProductListController.setCatID(mCatID);
//            mProductListController.setCatName(mCatName);
//            mProductListController.setTagView(tagView);
//            mProductListController.setSortID(mSortID);
//            mProductListController.setJsonFilter(jsonFilter);
//            mProductListController.setList_param(list_param);
            mProductListController.setDelegate(mProductListBlock);
            mProductListController.onResume();
        }

        mProductListBlock.setOnTourchChangeView(mProductListController
                .getmOnTouchChangeViewData());
        mProductListBlock.setOnTourchToFilter(mProductListController
                .getmOnTouchToFilter());
        mProductListBlock.setOnTourchToSort(mProductListController.getmOnTouchToSort());
        mProductListBlock.setScrollListProduct(mProductListController.getOnScrolListProduct());
        mProductListBlock.setOnTouchListenerGridview(mProductListController
                .getmOnTouchGridview());


        return rootView;
    }

    public void setTagView(String tag) {
        if (DataLocal.isTablet) {
            tagView = TagSearch.TAG_GRIDVIEW;
        } else {
            if (tag == null || (!tag.equals(TagSearch.TAG_GRIDVIEW) && !tag.equals(TagSearch.TAG_GRIDVIEW))) {
                tagView = TagSearch.TAG_LISTVIEW;
//                if (Config.getInstance().getDefaultList() != null
//                        && Config.getInstance().getDefaultList().equals("1")) {
//                    // neu gia tri 1: Gridview
//                    tagView = TagSearch.TAG_GRIDVIEW;
//                }
            } else {
                tagView = tag;
            }
        }
    }
    // set param


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSearchHomeBlock.onDestroy();
    }

    public void setListParam(String key, String value) {
        list_param.put(key, value);
    }

}

package com.simicart.theme.matrixtheme.home.controller;

import com.simicart.core.catalog.listproducts.controller.ProductListController;
import com.simicart.core.catalog.listproducts.model.ConstantsSearch;
import com.simicart.core.catalog.listproducts.model.ProductListCategoryModel;
import com.simicart.core.config.DataLocal;
import com.simicart.theme.matrixtheme.home.model.SpotProductListTheme1Model;

/**
 * Created by Simi on 14-Apr-16.
 */
public class SpotProductListTheme1Controller extends ProductListController {
    String mKey = "";

    public void setKey(String key) {
        this.mKey = key;
    }

    @Override
    protected void requestProducts() {
        //limit offset
        if (mCurrentOffset == 0) {
            mDelegate.showLoading();
        }
        if (DataLocal.isTablet) {
            limit = 16;
        } else {
            limit = 8;
        }

        if (mModel == null) {
            mModel = new SpotProductListTheme1Model();
        }

        String param_offset = getValueListParam(ConstantsSearch.PARAM_OFFSET);
        if (param_offset != null && !param_offset.equals("")) {
            mModel.addParam(ConstantsSearch.PARAM_OFFSET,
                    String.valueOf(param_offset));
        } else {
            mModel.addParam(ConstantsSearch.PARAM_OFFSET,
                    String.valueOf(mCurrentOffset));
        }
        String param_limit = getValueListParam(ConstantsSearch.PARAM_LIMIT);
        if (param_limit != null && !param_limit.equals("")) {
            mModel.addParam(ConstantsSearch.PARAM_LIMIT,
                    String.valueOf(param_limit));
        } else {
            mModel.addParam(ConstantsSearch.PARAM_LIMIT, String.valueOf(limit));
        }
        String param_key = getValueListParam(ConstantsSearch.PARAM_KEY);
        if (param_key != null && !param_key.equals("")) {
            mModel.addParam(ConstantsSearch.PARAM_KEY, param_key);
        }
        String param_sort_option = getValueListParam(ConstantsSearch.PARAM_SORT_OPTION);
        if (param_sort_option != null && !param_sort_option.equals("")) {
            mModel.addParam(ConstantsSearch.PARAM_SORT_OPTION,
                    param_sort_option);
        } else {
            mModel.addParam(ConstantsSearch.PARAM_SORT_OPTION, mSortID);
        }
        mModel.addParam(ConstantsSearch.PARAM_WIDTH, "300");
        mModel.addParam(ConstantsSearch.PARAM_HEIGHT, "300");
        if (null != jsonFilter) {
            mModel.addParam("filter", jsonFilter);
        } else {
            mModel.addParam("filter", "");
        }
        mModel.setDelegate(new ModelDelegate() {

            @Override
            public void callBack(String message, boolean isSuccess) {
                mDelegate.dismissLoading();
                mDelegate.removeFooterView();
                if (isSuccess) {
                    resultNumber = message;
                    mDelegate.setQty(resultNumber);
                    listProduct = ((ProductListCategoryModel) mModel).getListProduct();
                    listProductIds = ((ProductListCategoryModel) mModel).getListProductIds();
                    mDelegate.updateView(mModel.getCollection());
                    isOnscroll = true;
                }
            }
        });
        mModel.request();
    }
}

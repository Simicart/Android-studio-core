package com.simicart.core.catalog.listproducts.controller;

import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.listproducts.model.ConstantsSearch;
import com.simicart.core.catalog.listproducts.model.ProductListCategoryModel;
import com.simicart.core.catalog.listproducts.model.SearchListModel;
import com.simicart.core.config.DataLocal;

/**
 * Created by Simi on 11-Apr-16.
 */
public class SearchListController extends ProductListController {

    String mQuery = "";

    public void setQuery(String mQuery) {
        this.mQuery = mQuery;
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
            mModel = new SearchListModel();
        }

        String param_categoryid = getValueListParam(ConstantsSearch.PARAM_CATEGORY_ID);
        if (param_categoryid != null && !param_categoryid.equals("")
                && !param_categoryid.equals("-1")) {
            mModel.addBody(ConstantsSearch.PARAM_CATEGORY_ID, param_categoryid);
        }
        String param_offset = getValueListParam(ConstantsSearch.PARAM_OFFSET);
        if (param_offset != null && !param_offset.equals("")) {
            mModel.addBody(ConstantsSearch.PARAM_OFFSET,
                    String.valueOf(param_offset));
        } else {
            mModel.addBody(ConstantsSearch.PARAM_OFFSET,
                    String.valueOf(mCurrentOffset));
        }
        String param_limit = getValueListParam(ConstantsSearch.PARAM_LIMIT);
        if (param_limit != null && !param_limit.equals("")) {
            mModel.addBody(ConstantsSearch.PARAM_LIMIT,
                    String.valueOf(param_limit));
        } else {
            mModel.addBody(ConstantsSearch.PARAM_LIMIT, String.valueOf(limit));
        }
        String param_query = getValueListParam(ConstantsSearch.PARAM_QUERY);
        if (param_query != null && !param_query.equals("")) {
            mModel.addBody(ConstantsSearch.PARAM_QUERY, param_query);
        }
        String param_sort_option = getValueListParam(ConstantsSearch.PARAM_SORT_OPTION);
        if (param_sort_option != null && !param_sort_option.equals("")) {
            mModel.addBody(ConstantsSearch.PARAM_SORT_OPTION,
                    param_sort_option);
        } else {
            mModel.addBody(ConstantsSearch.PARAM_SORT_OPTION, mSortID);
        }
        mModel.addBody(ConstantsSearch.PARAM_WIDTH, "300");
        mModel.addBody(ConstantsSearch.PARAM_HEIGHT, "300");
        if (null != jsonFilter) {
            mModel.addBody("filter", jsonFilter);
        } else {
            mModel.addBody("filter", "");
        }
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.removeFooterView();
//                    resultNumber = message;
                mDelegate.setQty(resultNumber);
                listProduct = ((ProductListCategoryModel) mModel).getListProduct();
                listProductIds = ((ProductListCategoryModel) mModel).getListProductIds();
                mDelegate.updateView(mModel.getCollection());
                isOnscroll = true;
            }
        });
        mModel.request();
    }
}

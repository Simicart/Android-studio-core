package com.simicart.theme.ztheme.home.controller;

import com.simicart.core.catalog.listproducts.controller.ProductListController;
import com.simicart.core.catalog.listproducts.model.ConstantsSearch;
import com.simicart.core.catalog.listproducts.model.ProductListCategoryModel;
import com.simicart.core.config.DataLocal;
import com.simicart.theme.ztheme.home.model.SpotProductZThemeModel;

/**
 * Created by Simi on 14-Apr-16.
 */
public class SpotProductListZthemeController extends ProductListController {
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
            mModel = new SpotProductZThemeModel();
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
        String param_key = getValueListParam(ConstantsSearch.PARAM_KEY);
        if (param_key != null && !param_key.equals("")) {
            mModel.addBody(ConstantsSearch.PARAM_KEY, param_key);
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
//        mModel.setDelegate(new ModelDelegate() {
//
//            @Override
//            public void callBack(String message, boolean isSuccess) {
//                mDelegate.dismissLoading();
//                mDelegate.removeFooterView();
//                if (isSuccess) {
//                    resultNumber = message;
//                    mDelegate.setQty(resultNumber);
//                    listProduct = ((ProductListCategoryModel) mModel).getListProduct();
//                    listProductIds = ((ProductListCategoryModel) mModel).getListProductIds();
//                    mDelegate.updateView(mModel.getCollection());
//                    isOnscroll = true;
//                }
//            }
//        });
        mModel.request();
    }
}

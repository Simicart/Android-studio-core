package com.simicart.core.catalog.listproducts.model;


import com.simicart.core.config.Constants;

/**
 * Created by Simi on 14-Apr-16.
 */
public class AllProductModel extends ProductListCategoryModel {

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.GET_ALL_PRODUCTS;
    }
}

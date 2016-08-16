package com.simicart.core.catalog.listproducts.model;

import com.simicart.core.config.Constants;

/**
 * Created by Simi on 11-Apr-16.
 */
public class SearchListModel extends ProductListCategoryModel {

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.SEARCH_PRODUCTS;
    }

}

package com.simicart.theme.ztheme.home.model;

import com.simicart.core.catalog.listproducts.model.ProductListCategoryModel;

public class SpotProductZThemeModel extends ProductListCategoryModel {

    @Override
    protected void setUrlAction() {
        url_action = "ztheme/api/get_spot_products";
    }

}

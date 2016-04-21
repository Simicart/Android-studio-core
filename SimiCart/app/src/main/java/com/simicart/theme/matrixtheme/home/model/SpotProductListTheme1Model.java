package com.simicart.theme.matrixtheme.home.model;

import com.simicart.core.catalog.listproducts.model.ProductListCategoryModel;

/**
 * Created by Simi on 14-Apr-16.
 */
public class SpotProductListTheme1Model extends ProductListCategoryModel {
    @Override
    protected void setUrlAction() {
        this.url_action = "themeone/api/get_spot_products";
    }
}

package com.simicart.core.catalog.product.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;

public class ProductModel extends SimiModel {

    @Override
    protected void setUrlAction() {
        this.mUrlAction = Constants.GET_PRODUCT_DETAIL;
    }

    @Override
    public void parseData() {
        try {
            JSONArray list = this.getDataJSON().getJSONArray("data");
            collection = new SimiCollection();
            collection.setJSON(mJSON);
            if (null != list && list.length() > 0) {
                Product products = new Product();
                products.setJSONObject(list.getJSONObject(0));
                collection.addEntity(products);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void setEnableCache() {
        this.enableCache = true;
    }
}

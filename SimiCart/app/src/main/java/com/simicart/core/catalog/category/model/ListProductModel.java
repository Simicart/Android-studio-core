package com.simicart.core.catalog.category.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListProductModel extends SimiModel {

    protected String mID = "-1";
    protected String mQty;
    protected ArrayList<Product> listProducts;
    protected ArrayList<String> mListID;

    public void setCategoryID(String id) {
        mID = id;
    }

    public String getQty() {
        return mQty;
    }

    @Override
    protected void setUrlAction() {
        if (!Utils.validateString(mID)) {
            mUrlAction = Constants.GET_ALL_PRODUCTS;
        } else {
            mUrlAction = Constants.GET_CATEGORY_PRODUCTS;
        }

    }

    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            if (null == collection) {
                collection = new SimiCollection();
                collection.setJSON(mJSON);
            }
            listProducts = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                Product product = new Product();
                product.setJSONObject(list.getJSONObject(i));
                product.parse();
                listProducts.add(product);
                collection.addEntity(product);
            }

            if (mJSON.has("other")) {
                JSONArray arrayOTher = mJSON.getJSONArray("other");
                if (null != arrayOTher && arrayOTher.length() > 0) {
                    JSONObject jsonOther = arrayOTher.getJSONObject(0);
                    JSONArray array = jsonOther.getJSONArray("product_id_array");
                    if (null != array && array.length() > 0) {
                        parseListID(array);
                    }
                }
            }

        } catch (JSONException e) {

        }
    }

    protected void parseListID(JSONArray array) throws JSONException {
        mListID = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String id = array.getString(i);
            mListID.add(id);
        }
    }


    @Override
    protected void setEnableCache() {
        this.enableCache = true;
    }

    public ArrayList<Product> getListProducts() {
        return listProducts;
    }

    public ArrayList<String> getListID() {
        return mListID;
    }
}

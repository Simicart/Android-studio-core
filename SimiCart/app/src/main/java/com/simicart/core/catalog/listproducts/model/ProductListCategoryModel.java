package com.simicart.core.catalog.listproducts.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Simi on 07-Apr-16.
 */
public class ProductListCategoryModel extends SimiModel {

    private ArrayList<String> listProductIds = new ArrayList<>();
    private ArrayList<Product> listProduct = new ArrayList<>();

    @Override
    protected void paserData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            if (null == collection) {
                collection = new SimiCollection();
            }
            collection.setJSON(mJSON);

            //parse listID
            boolean hasListIDs = parseListProductId(mJSON);
            for (int i = 0; i < list.length(); i++) {
                Product product = new Product();
                if (!hasListIDs) {
                    listProductIds.add(product.getId());
                }
                product.setJSONObject(list.getJSONObject(i));
                listProduct.add(product);
                collection.addEntity(product);
            }
        } catch (JSONException e) {
            Log.e("ModelSearchBase ", "paserData Exception " + e.getMessage());
        }
    }

    public ArrayList<Product> getListProduct() {
        return listProduct;
    }

    public ArrayList<String> getListProductIds() {
        return listProductIds;
    }

    private boolean parseListProductId(JSONObject json) {
        if (json != null && json.has("other")) {
            try {
                JSONArray arrayId = json.getJSONArray("other");
                for (int i = 0; i < arrayId.length(); i++) {
                    JSONObject object_other = (JSONObject) arrayId.get(i);
                    if (object_other != null
                            && object_other.has("product_id_array")) {
                        JSONArray array = object_other
                                .getJSONArray("product_id_array");
                        if (array.length() > 0) {
                            listProductIds.clear();
                            for (int j = 0; j < array.length(); j++) {
                                String id = array.getString(j);
                                if (Utils.validateString(id)) {
                                    listProductIds.add(id);
                                }
                            }
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                Log.e("Exception:", e.getMessage());
            }
        }
        return false;
    }

    @Override
    protected void setUrlAction() {
        url_action = Constants.GET_CATEGORY_PRODUCTS;
    }

    @Override
    protected void setShowNotifi() {
        isShowNotify = false;
    }
}

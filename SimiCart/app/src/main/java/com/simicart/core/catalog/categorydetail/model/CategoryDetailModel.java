package com.simicart.core.catalog.categorydetail.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.categorydetail.entity.LayerEntity;
import com.simicart.core.catalog.categorydetail.fragment.CategoryDetailFragment;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategoryDetailModel extends SimiModel {

    private ArrayList<String> listProductIds = new ArrayList<>();
    private ArrayList<Product> listProduct = new ArrayList<>();
    protected LayerEntity mLayerEntity;
    protected String mType;
    protected String mCustomUrl;
    protected int resultNumber;

    public CategoryDetailModel(String type) {
        mType = type;
    }

    @Override
    protected void setUrlAction() {
        if (mType.equals(ValueData.CATEGORY_DETAIL.CATE)) {
            mUrlAction = "connector/catalog/get_category_products";
        } else if (mType.equals(ValueData.CATEGORY_DETAIL.SEARCH)) {
            mUrlAction = "connector/catalog/search_products";
        } else if (mType.equals(ValueData.CATEGORY_DETAIL.ALL)) {
            mUrlAction = "connector/catalog/get_all_products";
        } else {
            mUrlAction = mCustomUrl;
        }
    }

    @Override
    protected void parseData() {
        try {
            if (mJSON.has("message")) {
                JSONArray messArr = mJSON.getJSONArray("message");
                String total = messArr.getString(0);
                if (Utils.validateString(total)) {
                    try {
                        resultNumber = Integer.parseInt(total);
                    } catch (Exception e) {

                    }
                }
            }

            JSONArray list = this.mJSON.getJSONArray("data");

            if (null == collection) {
                collection = new SimiCollection();
            }
            if (null != list && list.length() > 0) {
                for (int i = 0; i < list.length(); i++) {
                    Product product = new Product();
                    product.parse(list.getJSONObject(i));
                    collection.addEntity(product);
                }
            }

            if (mJSON.has("layerednavigation")) {
                JSONObject jsLayer = mJSON.getJSONObject("layerednavigation");
                Log.e("CategoryDetailModel", "LAYTER " + jsLayer.toString());
                mLayerEntity = new LayerEntity();
                mLayerEntity.parse(jsLayer);
            }

        } catch (JSONException e) {
            Log.e("CategoryDetailModel ", "paserData Exception " + e.getMessage());
        }
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

    public LayerEntity getLayerEntity() {
        return mLayerEntity;
    }


    public void setCustomUrl(String customUrl) {
        mCustomUrl = customUrl;
    }

    public int getResultNumber() {
        return resultNumber;
    }
}

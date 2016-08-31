package com.simicart.core.catalog.category.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CategoryModel extends SimiModel {

    protected ArrayList<Category> listCategories;
    protected String mID;

    public void setCategoryID(String id) {
        mID = id;
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.GET_CATEGORY_LIST;
    }

    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            collection = new SimiCollection();
            // if (Config.newInstance().isShow_link_all_product()) {
            // Category shop_category = new Category();
            // shop_category.setCategoryId(mID);
            // shop_category.setCategoryName(Config.newInstance().getText(
            // "All products"));
            // shop_category.setChild(false);
            // collection.addEntity(shop_category);
            // }
            listCategories = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                Category category = new Category();
                category.setJSONObject(list.getJSONObject(i));
                category.parse();
                listCategories.add(category);
                collection.addEntity(category);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setEnableCache() {
        this.enableCache = true;
    }

    public ArrayList<Category> getListCategory() {
        return listCategories;
    }

}

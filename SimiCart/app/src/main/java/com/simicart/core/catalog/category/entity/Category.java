package com.simicart.core.catalog.category.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Category extends SimiEntity {
    protected String mID;
    protected String mName;
    protected String mImage;
    protected String hasChild;
	protected ArrayList<Category> listChildCategory;

    @Override
    public void parse() {
        hasChild = getData("has child");
        mImage = getData(Constants.CATEGORY_IMAGE);
        mName = getData(Constants.CATEGORY_NAME);
        mID = getData(Constants.CATEGORY_ID);

		if(hasKey("child_cat")) {
			listChildCategory = new ArrayList<>();
			try {
				JSONArray childCatArr = mJSON.getJSONArray("child_cat");
				for(int i=0;i<childCatArr.length();i++) {
					JSONObject childCatObj = childCatArr.getJSONObject(i);
					Category childCat = new Category();
					childCat.setJSONObject(childCatObj);
					childCat.parse();
					listChildCategory.add(childCat);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    }

    public boolean hasChild() {
        return Utils.TRUE(hasChild);
    }

    public void setChild(boolean has_Child) {
        hasChild = String.valueOf(has_Child);
    }

    public String getCategoryImage() {

        return this.mImage;
    }

    public void setCategoryImage(String category_image) {
        this.mImage = category_image;
    }

    public String getCategoryName() {
        return this.mName;
    }

    public void setCategoryName(String category_name) {
        this.mName = category_name;
    }

    public String getCategoryId() {
        return mID;
    }

    public void setCategoryId(String category_id) {

        this.mID = category_id;
    }

	public ArrayList<Category> getListChildCategory() {
		return listChildCategory;
	}

	public void setListChildCategory(ArrayList<Category> listChildCategory) {
		this.listChildCategory = listChildCategory;
	}
}

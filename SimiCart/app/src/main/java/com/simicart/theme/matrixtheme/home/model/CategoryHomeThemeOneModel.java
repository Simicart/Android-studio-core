package com.simicart.theme.matrixtheme.home.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.category.entity.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryHomeThemeOneModel extends SimiModel {

	@Override
	protected void parseData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");

			if (null == collection) {
				collection = new SimiCollection();
			}
			for (int i = 0; i < list.length(); i++) {
				JSONObject data = list.getJSONObject(i);
				Category category = new Category();
				category.parse(data);
				collection.addEntity(category);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setUrlAction() {
		mUrlAction = "themeone/api/get_order_categories";
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}
}

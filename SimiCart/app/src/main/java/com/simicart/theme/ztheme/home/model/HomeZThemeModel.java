package com.simicart.theme.ztheme.home.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.theme.ztheme.home.entity.ZThemeCatalogEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeZThemeModel extends SimiModel {

    @Override
    protected void parseData() {
        try {
            JSONArray array = this.mJSON.getJSONArray("data");
            collection = new SimiCollection();
            if (null != array && array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    ZThemeCatalogEntity ZThemeCatalogEntity = new ZThemeCatalogEntity();
                    ZThemeCatalogEntity.setJSONObject(object);
                    collection.addEntity(ZThemeCatalogEntity);
                }
            }
        } catch (JSONException e) {
        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "ztheme/api/get_banners_and_spot";
    }

    @Override
    protected void setEnableCache() {
        this.enableCache = true;
    }
}

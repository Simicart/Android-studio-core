package com.simicart.plugins.storelocator.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetTagSearchModel extends SimiModel {

    protected ArrayList<String> listTags;

    public ArrayList<String> getTags() {
        return listTags;
    }

    @Override
    protected void parseData() {
        // TODO Auto-generated method stub
        Log.e("GetTagList", mJSON.toString());
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            collection = new SimiCollection();
            listTags = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                JSONObject object = list.getJSONObject(i);
                if(object.has("value")) {
                    String tag = object.getString("value");
                    listTags.add(tag);
                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void setUrlAction() {
        // TODO Auto-generated method stub
        mUrlAction = "storelocator/api/get_tag_list";
    }

}

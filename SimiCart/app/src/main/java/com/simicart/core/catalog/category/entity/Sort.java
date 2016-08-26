package com.simicart.core.catalog.category.entity;

import com.simicart.core.config.Constants;

import org.json.JSONException;
import org.json.JSONObject;


public class Sort {
    private int mID;
    private String mTitle;

    protected String TITLE = "title";

    public boolean parse(JSONObject json) {
        try {
            if (json.has(Constants.ID)) {
                mID = json.getInt(Constants.ID);
            }
            if (json.has(TITLE)) {
                mTitle = json.getString(TITLE);
            }

            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    public int getId() {
        return mID;
    }

    public void setId(int id) {
        this.mID = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

}

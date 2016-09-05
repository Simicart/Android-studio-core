package com.simicart.core.catalog.categorydetail.entity;

import com.simicart.core.base.model.entity.SimiEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MSI on 31/08/2016.
 */
public class LayerEntity extends SimiEntity {

    protected ArrayList<FilterEntity> mListFilter;
    protected ArrayList<FilterState> mListState;

    protected String LAYER_FILTER = "layer_filter";
    protected String LAYER_STATE = "layer_state";

    @Override
    public void parse() {
        try {
            parseState();
            parseFilter();
        } catch (Exception e) {

        }
    }

    protected void parseState() throws JSONException {
        mListState = new ArrayList<>();
        if (hasKey(LAYER_STATE)) {
            JSONArray array_state = mJSON
                    .getJSONArray(LAYER_STATE);
            if (null != array_state && array_state.length() > 0) {
                for (int i = 0; i < array_state.length(); i++) {
                    JSONObject object = array_state
                            .getJSONObject(i);
                    FilterState state = new FilterState();
                    state.parse(object);
                    mListState.add(state);
                }
            }
        }
    }

    protected void parseFilter() throws JSONException {
        mListFilter = new ArrayList<>();
        if (hasKey(LAYER_FILTER)) {
            JSONArray array_filter = mJSON
                    .getJSONArray(LAYER_FILTER);
            if (null != array_filter && array_filter.length() > 0) {
                for (int i = 0; i < array_filter.length(); i++) {
                    JSONObject object = array_filter
                            .getJSONObject(i);
                    FilterEntity entity = new FilterEntity();
                    entity.setJSONObject(object);
                    mListFilter.add(entity);
                }
            }
        }
    }

    public ArrayList<FilterEntity> getListFilter() {
        return mListFilter;
    }

    public void setListFilter(ArrayList<FilterEntity> mListFilter) {
        this.mListFilter = mListFilter;
    }

    public ArrayList<FilterState> getListState() {
        return mListState;
    }

    public void setListState(ArrayList<FilterState> mListState) {
        this.mListState = mListState;
    }
}

package com.simicart.plugins.klarna.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.plugins.klarna.fragment.KlarnaFragment;

import org.json.JSONArray;
import org.json.JSONException;

public class KlarnaModel extends SimiModel {

    protected JSONArray jsDataKlarna;

    public JSONArray getDataKlarna() {
        return jsDataKlarna;
    }

    @Override
    protected void parseData() {
        try {
            jsDataKlarna = this.mJSON.getJSONArray("data");
        } catch (JSONException e) {

        }
    }


    @Override
    protected void setUrlAction() {
        mUrlAction = KlarnaFragment.URL_GETPARAMS_KLARNA;
    }

}

package com.simicart.core.base.network.response;


import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SimiResponse {
    protected String mStatus = "";
    protected String mMessage;
    protected String mData;
    protected JSONObject mJSON;
    protected SimiError mError;

    public boolean parse(String json) {
        mData = json;
        return parse();

    }

    public boolean parse() {
        if (null == mData) {
            return false;
        }
        mJSON = null;
        try {
            mJSON = new JSONObject(mData);
            return parse(mJSON);
        } catch (JSONException e) {
            return false;
        }
    }

    public boolean parse(JSONObject json) {
        mJSON = json;
        try {
            if (mJSON.has(Constants.STATUS)) {
                mStatus = mJSON.getString(Constants.STATUS);
            }
            if (mJSON.has(Constants.MESSAGE)) {
                JSONArray mes = mJSON.getJSONArray(Constants.MESSAGE);
                mMessage = mes.getString(0);
                mError = new SimiError();
                mError.setMessage(mMessage);
            }


            if ( equal(mStatus, "FAIL")) {
                mError = new SimiError();
                mError.setMessage(mMessage);
                return false;
            }


            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    protected boolean equal(String s1, String s2) {


        s1 = s1.toUpperCase();
        s2 = s2.toUpperCase();

        if (s1.equals(s2)) {
            return true;
        }

        return false;
    }

    public void setSimiError(SimiError error) {
        mError = error;
    }

    public SimiError getSimiError() {
        return mError;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getMessage() {
        if (mMessage == null) {
//            mMessage = Config.getInstance().getText(
//                    "Some errors occurred. Please try again later");
        }
        return mMessage;
    }

    public void setData(String data) {
        mData = data;
    }

    public void setDataJSON(JSONObject json) {
        mJSON = json;
    }

    public JSONObject getDataJSON() {
        return mJSON;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}

package com.simicart.core.base.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.simicart.core.base.model.entity.SimiData;

import java.util.HashMap;

public class SimiFragment extends Fragment {

    protected View rootView;
    protected SimiData mData;
    protected HashMap<String, Object> mHashMapData;
    protected String screenName = "";

    protected static final String KEY_DATA = "data";

    public static SimiFragment newInstance(SimiData data) {
        SimiFragment fragment = new SimiFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected void getDataFromBundle() {
        Bundle bundle = getArguments();
        mData = bundle.getParcelable("data");
        mHashMapData = mData.getData();
    }

    public void setScreenName(String screenName) {

    }

    public String getScreenName() {
        return screenName;
    }

    public HashMap<String, Object> getData() {
        if (null != mData) {
            return mData.getData();
        }
        return null;
    }

    public Object getValueWithKey(String key) {
        if (null != mHashMapData) {
            mHashMapData = getData();
        }
        if (null == mHashMapData) {
            return null;
        }
        return mHashMapData.get(key);
    }

}

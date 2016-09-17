package com.simicart.core.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.ValueData;

import java.util.HashMap;

public class SimiFragment extends Fragment {

    protected static final String KEY_DATA = "data";
    protected View rootView;
    protected SimiData mData;
    protected HashMap<String, Object> mHashMapData;
    protected String screenName = "";

    public static SimiFragment newInstance(SimiData data) {
        SimiFragment fragment = new SimiFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromBundle();
    }

    protected void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            mData = bundle.getParcelable("data");
            if (mData != null) {
                mHashMapData = mData.getData();
            }
        }
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
        dispatchEventAnalytics(screenName);
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

    protected void dispatchEventAnalytics(String screenName) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(KeyData.ANALYTICS.SEND_TYPE, ValueData.ANALYTICS.SCREEN_TYPE);
        hm.put(KeyData.ANALYTICS.NAME_SCREEN, screenName);
        SimiEvent.dispatchEvent("com.simicart.analytics.sendaction", hm);
    }

}

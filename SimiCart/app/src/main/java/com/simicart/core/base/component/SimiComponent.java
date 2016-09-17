package com.simicart.core.base.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 17/08/2016.
 */
public class SimiComponent {
    protected View rootView;
    protected String mIDComponent;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public SimiComponent() {
        mContext = SimiManager.getIntance().getCurrentActivity();
        mInflater = LayoutInflater.from(mContext);
    }

    public View createView() {
        return rootView;
    }

    public View findView(String id) {
        int idView = Rconfig.getInstance().id(id);
        return rootView.findViewById(idView);
    }

    protected View findLayout(String id) {
        int idView = Rconfig.getInstance().layout(id);
        return mInflater.inflate(idView, null);
    }

    public String getIDComponent() {
        return mIDComponent;
    }

    public void setIDComponent(String IDComponent) {
        mIDComponent = IDComponent;
    }

    public boolean isCompleteRequired() {
        return true;
    }
}

package com.simicart.core.base.component;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 31/08/2016.
 */
public class SimiPopup {

    protected ProgressDialog ppFilter;
    protected Context mContext;
    protected LayoutInflater mInflater;


    public SimiPopup() {
        mContext = SimiManager.getIntance().getCurrentActivity();
        mInflater = LayoutInflater.from(mContext);
    }

    public  void createPopup() {
        ppFilter = ProgressDialog.show(mContext, null, null, true, false);
        View contentView = createView();
        ppFilter.setContentView(contentView);
        ppFilter.setCanceledOnTouchOutside(true);
    }

    public View createView() {
        return null;
    }

    public void show() {
        ppFilter.show();
    }

    public void dismiss() {
        if (ppFilter.isShowing()) {
            ppFilter.dismiss();
        }
    }

    protected View findLayout(String id) {
        int idView = Rconfig.getInstance().layout(id);
        return mInflater.inflate(idView, null);
    }

    public View findView(View parentView, String id) {
        int idView = Rconfig.getInstance().id(id);
        return parentView.findViewById(idView);
    }

}

package com.simicart.core.catalog.categorydetail.component;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 23/08/2016.
 */
public class SortComponent {
    protected PopupWindow mPopup;
    protected Context mContext;
    protected View rootView;
    protected View mAtView;

    public SortComponent(View atView) {
        mAtView = atView;
        mContext = SimiManager.getIntance().getCurrentActivity();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        rootView = inflater.inflate(Rconfig.getInstance().layout("core_component_sort"), null);
    }

    public void showPopup() {
        if (null == mPopup) {
            mPopup = new PopupWindow(rootView, 400, 400, true);
            mPopup.setAnimationStyle(android.R.style.Animation_Dialog);
            // mPopup.setContentView(rootView);
            Log.e("SortComponent", "Show Popup  001 ");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mPopup.showAtLocation(mAtView, Gravity.CENTER_HORIZONTAL, 10, 10);
                Log.e("SortComponent", "Show Popup  002 ");
            }
        }
    }


}

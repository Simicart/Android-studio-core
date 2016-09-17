package com.simicart.core.base.component;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.component.callback.MenuRowCallBack;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;

/**
 * Created by Martial on 8/31/2016.
 */
public class SimiMultiMenuRowComponent extends SimiComponent {

    protected ImageView ivExtend, ivIcon;
    protected TextView tvLabel, tvCurrent;
    protected MenuRowCallBack mCallBack;

    protected String icon;
    protected String label;
    protected String current;

    public SimiMultiMenuRowComponent() {
        super();
    }

    @Override
    public View createView() {
        if (AppStoreConfig.getInstance().isRTL()) {
            rootView = findLayout("rtl_core_component_multi_menu_row");
        } else {
            rootView = findLayout("core_component_multi_menu_row");
        }

        ivIcon = (ImageView) findView("iv_icon");
        if (Utils.validateString(icon)) {
            Drawable icIcon = AppColorConfig.getInstance().getIcon(icon, AppColorConfig.getInstance().getContentColor());
            ivIcon.setImageDrawable(icIcon);
        }

        tvCurrent = (TextView) findView("tv_current");
        tvCurrent.setText(current);
        tvCurrent.setTextColor(AppColorConfig.getInstance().getContentColor());

        tvLabel = (TextView) findView("tv_label");
        tvLabel.setText(label);
        tvLabel.setTextColor(AppColorConfig.getInstance().getContentColor());

        ivExtend = (ImageView) findView("iv_extend");
        Drawable icExtend = null;
        if (AppStoreConfig.getInstance().isRTL()) {
            icExtend = AppColorConfig.getInstance().getIcon("core_back", AppColorConfig.getInstance().getContentColor());
        } else {
            icExtend = AppColorConfig.getInstance().getIcon("ic_extend", AppColorConfig.getInstance().getContentColor());
        }
        ivExtend.setImageDrawable(icExtend);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onClick();
                }
            }
        });

        return rootView;
    }

    public void setmCallBack(MenuRowCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setCurrent(String current) {
        this.current = current;
        if (tvCurrent != null) {
            tvCurrent.setText(current);
        }
    }

}

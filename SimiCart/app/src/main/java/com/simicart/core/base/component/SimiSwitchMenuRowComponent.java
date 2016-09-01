package com.simicart.core.base.component;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.component.callback.SwitchMenuCallBack;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;

/**
 * Created by Martial on 8/31/2016.
 */
public class SimiSwitchMenuRowComponent extends SimiComponent {

    protected ImageView ivIcon;
    protected SwitchCompat swExtend;
    protected TextView tvLabel;
    protected SwitchMenuCallBack mCallBack;

    protected String icon;
    protected String label;

    protected boolean isEnable = true;

    public SimiSwitchMenuRowComponent() {
        super();
    }

    @Override
    public View createView() {
        rootView = findLayout("core_component_switch_menu_row");

        ivIcon = (ImageView) findView("iv_icon");
        if(Utils.validateString(icon)) {
            Drawable icIcon = AppColorConfig.getInstance().getIcon(icon, AppColorConfig.getInstance().getContentColor());
            ivIcon.setImageDrawable(icIcon);
        }

        tvLabel = (TextView) findView("tv_label");
        tvLabel.setText(label);
        tvLabel.setTextColor(AppColorConfig.getInstance().getContentColor());

        swExtend = (SwitchCompat) findView("sw_extend");
        swExtend.setChecked(isEnable);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEnable == true) {
                    isEnable = false;
                } else {
                    isEnable = true;
                }
                swExtend.setChecked(isEnable);
                if(mCallBack != null) {
                    mCallBack.onClick(isEnable);
                }
            }
        });

        return rootView;
    }

    public void setmCallBack(SwitchMenuCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

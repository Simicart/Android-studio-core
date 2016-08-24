package com.simicart.core.base.component;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.component.callback.MenuRowCallBack;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;

/**
 * Created by Martial on 8/24/2016.
 */
public class SimiMenuRowComponent extends SimiComponent {

    protected ImageView ivExtend, ivIcon;
    protected TextView tvLabel;
    protected MenuRowCallBack mCallBack;

    protected String icon;
    protected String label;

    public SimiMenuRowComponent() {
        super();
    }

    @Override
    public View createView() {
        rootView = findLayout("core_component_menu_row");

        ivIcon = (ImageView) findView("iv_icon");
        ivIcon.setImageResource(Rconfig.getInstance().drawable(icon));

        tvLabel = (TextView) findView("tv_label");
        tvLabel.setText(label);
        tvLabel.setTextColor(AppColorConfig.getInstance().getContentColor());

        ivExtend = (ImageView) findView("iv_extend");
        Drawable icon = AppColorConfig.getInstance().getIcon("ic_extend", AppColorConfig.getInstance().getContentColor());
        ivExtend.setImageDrawable(icon);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallBack != null) {
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
}

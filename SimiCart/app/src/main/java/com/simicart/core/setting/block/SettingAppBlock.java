package com.simicart.core.setting.block;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Rconfig;
import com.simicart.core.setting.delegate.SettingAppDelegate;

public class SettingAppBlock extends SimiBlock implements SettingAppDelegate {

    protected LinearLayout llSetting;

    public SettingAppBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        llSetting = (LinearLayout) mView.findViewById(Rconfig.getInstance().id("ll_setting"));
    }

    @Override
    public void drawView(SimiCollection collection) {

    }

    @Override
    public void addItemRow(View view) {
        if (view != null) {
            llSetting.addView(view);
        }
    }
}

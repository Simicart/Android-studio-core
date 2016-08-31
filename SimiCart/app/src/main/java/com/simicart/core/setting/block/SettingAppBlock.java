package com.simicart.core.setting.block;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.material.LayoutRipple;
import com.simicart.core.setting.delegate.SettingAppDelegate;
import com.simicart.core.setting.entity.CurrencyEntity;
import com.simicart.core.store.entity.Stores;

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
		if(view != null) {
			llSetting.addView(view);
		}
	}
}

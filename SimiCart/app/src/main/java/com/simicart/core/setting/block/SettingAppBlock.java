package com.simicart.core.setting.block;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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

	protected TextView tv_language;
	protected LayoutRipple rl_language;
	protected TextView tv_language_selected;
	protected TextView tv_currency;
	protected LayoutRipple rl_currency;
	protected TextView tv_currency_selected;
	protected TextView tv_notification;
	protected RelativeLayout rl_notification;
	protected TextView tv_locator;
	protected LayoutRipple rl_locator;
	protected ToggleButton tb_notification;

	public SettingAppBlock(View view, Context context) {
		super(view, context);
	}

	@Override
	public void initView() {
		tv_language = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_language"));
		tv_language.setText(SimiTranslator.getInstance().translate("Language"));
		tv_language.setTextColor(AppColorConfig.getInstance().getContentColor());
		tv_language_selected = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_language_selected"));
		rl_language = (LayoutRipple) mView.findViewById(Rconfig.getInstance()
				.id("rl_language"));
		tv_currency = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_currency"));
		tv_currency.setText(SimiTranslator.getInstance().translate("Currency"));
		tv_currency.setTextColor(AppColorConfig.getInstance().getContentColor());
		tv_currency_selected = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_currency_selected"));
		rl_currency = (LayoutRipple) mView.findViewById(Rconfig.getInstance()
				.id("rl_currency"));
		tv_notification = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_notification"));
		tv_notification.setBackgroundColor(AppColorConfig.getInstance()
				.getAppBackground());
		tv_notification.setTextColor(AppColorConfig.getInstance().getContentColor());
		tv_notification.setText(SimiTranslator.getInstance().translate(
				"Show Notification"));
		rl_notification = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rl_notification"));
		rl_notification.setBackgroundColor(AppColorConfig.getInstance()
				.getAppBackground());
		tv_locator = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_locator"));
		tv_locator.setTextColor(AppColorConfig.getInstance().getContentColor());
		tv_locator.setText(SimiTranslator.getInstance().translate("Location Setting"));
		rl_locator = (LayoutRipple) mView.findViewById(Rconfig.getInstance()
				.id("rl_locator"));

		View v_under = (View) mView.findViewById(Rconfig.getInstance().id(
				"v_under"));
		v_under.setBackgroundColor(AppColorConfig.getInstance()
				.getAppBackground());

		tb_notification = (ToggleButton) mView.findViewById(Rconfig
				.getInstance().id("tb_notification"));
		if (DataPreferences.enableNotification()) {
			tb_notification.setChecked(true);
		} else {
			tb_notification.setChecked(false);
		}

		setColor(mView);
	}

	public void setOnClickNotification(OnClickListener clickListener) {
		rl_notification.setOnClickListener(clickListener);
	}

	public void setOnClickLocation(OnClickListener clickListener) {
		rl_locator.setOnClickListener(clickListener);
	}

	public void setOnClickCurrency(OnClickListener clickListener) {
		rl_currency.setOnClickListener(clickListener);
	}

	public void setOnClickLanguage(OnClickListener clickListener) {
		rl_language.setOnClickListener(clickListener);
	}

	private void setColor(View mView) {
		ImageView im_language_extend = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("im_language_extend"));
		ImageView im_currency_extend = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("im_currency_extend"));
		ImageView im_locator_extend = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("im_locator_extend"));
		Drawable icon = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_extend"));
		icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
				PorterDuff.Mode.SRC_ATOP);
		im_language_extend.setImageDrawable(icon);
		im_currency_extend.setImageDrawable(icon);
		im_locator_extend.setImageDrawable(icon);

		ImageView im_language = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("im_language"));
		Drawable ic_lang = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_lang"));
		ic_lang.setColorFilter(AppColorConfig.getInstance().getContentColor(),
				PorterDuff.Mode.SRC_ATOP);
		im_language.setImageDrawable(ic_lang);

		ImageView im_currency = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("im_currency"));
		Drawable ic_currency = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_currency"));
		ic_currency.setColorFilter(AppColorConfig.getInstance().getContentColor(),
				PorterDuff.Mode.SRC_ATOP);
		im_currency.setImageDrawable(ic_currency);

		ImageView im_notification = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("im_notification"));
		Drawable ic_notification = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_notification"));
		ic_notification.setColorFilter(AppColorConfig.getInstance().getContentColor(),
				PorterDuff.Mode.SRC_ATOP);
		im_notification.setImageDrawable(ic_notification);

		ImageView im_locator = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("im_locator"));
		Drawable ic_location_setting = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_location_setting"));
		ic_location_setting.setColorFilter(AppColorConfig.getInstance()
				.getContentColor(), PorterDuff.Mode.SRC_ATOP);
		im_locator.setImageDrawable(ic_location_setting);

		View v_language = (View) mView.findViewById(Rconfig.getInstance().id(
				"v_language"));
		View v_currency = (View) mView.findViewById(Rconfig.getInstance().id(
				"v_currency"));
		View v_notification = (View) mView.findViewById(Rconfig.getInstance()
				.id("v_notification"));
		View v_locator = (View) mView.findViewById(Rconfig.getInstance().id(
				"v_locator"));
		v_language.setBackgroundColor(AppColorConfig.getInstance().getLineColor());
		v_currency.setBackgroundColor(AppColorConfig.getInstance().getLineColor());
		v_notification.setBackgroundColor(AppColorConfig.getInstance().getLineColor());
		v_locator.setBackgroundColor(AppColorConfig.getInstance().getLineColor());

		mView.setBackgroundColor(AppColorConfig.getInstance()
				.getAppBackground());
	}

	@Override
	public void drawView(SimiCollection collection) {
	}

	@Override
	public void setCurrency() {
		if (DataPreferences.listCurrency != null && DataPreferences.listCurrency.size() > 0) {
			for (CurrencyEntity entity : DataPreferences.listCurrency) {
				if (entity.getValue().equals(DataPreferences.getCurrencyID())) {
					tv_currency_selected.setText(entity.getTitle());
				}
			}
		}

	}

	@Override
	public void successData() {
		this.dismissLoading();
		mView.setVisibility(View.VISIBLE);

		if (DataLocal.listStores.size() > 1) {
			rl_language.setVisibility(View.VISIBLE);
		} else {
			rl_language.setVisibility(View.GONE);
		}
		if (DataPreferences.listCurrency.size() > 1) {
			rl_currency.setVisibility(View.VISIBLE);
		} else {
			rl_currency.setVisibility(View.GONE);
		}
	}

	@Override
	public void setLanguage() {
		if (DataLocal.listStores != null && DataLocal.listStores.size() > 0) {
			for (Stores stores : DataLocal.listStores) {
				if (stores.getStoreID().equals(DataPreferences.getStoreID())) {
					tv_language_selected.setText(stores.getStoreName());
				}
			}
		}
		if (tv_language_selected.getText().length() < 1) {
			tv_language_selected.setText(AppStoreConfig.getInstance().getStoreName());
		}

	}

	@Override
	public void updateSettingNotification() {
		if (tb_notification.isChecked()) {
			tb_notification.setChecked(false);
			DataPreferences.saveNotificationSet(false);
		} else {
			tb_notification.setChecked(true);
			DataPreferences.saveNotificationSet(true);
		}

	}

	@Override
	public String getCurrency() {
		return tv_currency_selected.getText().toString();
	}

	@Override
	public String getLanguage() {
		return tv_language_selected.getText().toString();
	}

}

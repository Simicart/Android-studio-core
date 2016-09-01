package com.simicart.plugins.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.plugins.download.fragment.DownloadFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class DownloadProduct {

	protected Context mContext;
	protected HashMap<String, String> mFragments;
	protected ArrayList<ItemNavigation> mItems;
	public final String MY_DOWNLOAD_LABLE = "Manage Downloads";

	public DownloadProduct() {

		mContext = SimiManager.getIntance().getCurrentActivity();

		// register event: add navigation item to slide menu
		BroadcastReceiver addItemReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getBundleExtra(Constants.DATA);
				SimiData data = bundle.getParcelable("entity");
				mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
				mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);
				if(isExistDownloadable() == false) {
					addItemToSlideMenu();
				}
			}
		};
		SimiEvent.registerEvent(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_RELATED_PERSONAL, addItemReceiver);

		// register event: remove navigation item to slide menu
		BroadcastReceiver removeItemReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getBundleExtra(Constants.DATA);
				SimiData data = bundle.getParcelable("entity");
				mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
				mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);
				for (ItemNavigation mItemNavigation : mItems) {
					if (mItemNavigation.getName().equals(MY_DOWNLOAD_LABLE)) {
						mFragments.remove(mItemNavigation.getName());
						mItems.remove(mItemNavigation);
					}
				}
			}
		};
		SimiEvent.registerEvent(KeyEvent.SLIDE_MENU_EVENT.REMOVE_ITEM, removeItemReceiver);

	}

	protected boolean isExistDownloadable() {
		for (ItemNavigation item : mItems) {
			if (item.getName().equals(SimiTranslator.getInstance().translate(
					MY_DOWNLOAD_LABLE))) {
				return true;
			}
		}
		return false;
	}

	protected void addItemToSlideMenu() {
		ItemNavigation mItemNavigation = new ItemNavigation();
		mItemNavigation.setType(ItemNavigation.TypeItem.PLUGIN);
		Drawable icon = SimiManager
				.getIntance()
				.getCurrentActivity()
				.getResources()
				.getDrawable(
						Rconfig.getInstance().drawable("plugin_downloadable_ic_down"));
		icon.setColorFilter(AppColorConfig.getInstance().getMenuIconColor(),
				PorterDuff.Mode.SRC_ATOP);
		mItemNavigation.setName(MY_DOWNLOAD_LABLE);
		mItemNavigation.setIcon(icon);
		mItems.add(mItemNavigation);

		Fragment fragment = null;
		if(DataLocal.isTablet) {
			fragment = DownloadFragment.newInstance();
		} else {
			fragment = DownloadFragment.newInstance();
		}
		mFragments.put(mItemNavigation.getName(),
				fragment.getClass().getName());
	}

}

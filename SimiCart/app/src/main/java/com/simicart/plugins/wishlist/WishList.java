package com.simicart.plugins.wishlist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.plugins.wishlist.common.WishListConstants;
import com.simicart.plugins.wishlist.entity.ButtonAddWishList;
import com.simicart.plugins.wishlist.fragment.MyWishListFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class WishList {
	public static final String MY_WISHLIST = "My WishList";
	public String MY_WISH_LIST = WishListConstants.MY_WISHLIST;
	public static String MY_WISH_LIST_OLD = WishListConstants.MY_WISHLIST;
	public static ButtonAddWishList bt_addWish;
	public static String product_ID = "";
	protected ArrayList<ItemNavigation> mItems;
	protected TextView tv_qtyWishList;
	protected Context mContext;
	protected HashMap<String, String> mFragments;

	public WishList() {

		mContext = SimiManager.getIntance().getCurrentActivity();

		// register event: add navigation item to slide menu
		IntentFilter addItemFilter = new IntentFilter(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_RELATED_PERSONAL);
		BroadcastReceiver addItemReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getBundleExtra(Constants.DATA);
				SimiData data = bundle.getParcelable("entity");
				mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
				mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);
				addItemToSlideMenu();
			}
		};
		LocalBroadcastManager.getInstance(mContext).registerReceiver(addItemReceiver, addItemFilter);

		// register event: add navigation item to slide menu
		IntentFilter removeItemFilter = new IntentFilter(KeyEvent.SLIDE_MENU_EVENT.REMOVE_ITEM);
		BroadcastReceiver removeItemReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getBundleExtra(Constants.DATA);
				SimiData data = bundle.getParcelable("entity");
				mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
				mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);
				for (ItemNavigation mItemNavigation : mItems) {
					if (mItemNavigation.getName().equals(MY_WISHLIST)) {
						mFragments.remove(mItemNavigation.getName());
						mItems.remove(mItemNavigation);
					}
				}
			}
		};
		LocalBroadcastManager.getInstance(mContext).registerReceiver(removeItemReceiver, removeItemFilter);

	}

	protected void addItemToSlideMenu() {
		ItemNavigation mItemNavigation = new ItemNavigation();
		mItemNavigation.setType(ItemNavigation.TypeItem.PLUGIN);
		Drawable icon = SimiManager
				.getIntance()
				.getCurrentActivity()
				.getResources()
				.getDrawable(
						Rconfig.getInstance().drawable("plugins_wishlist_iconmenu"));
		icon.setColorFilter(AppColorConfig.getInstance().getMenuIconColor(),
				PorterDuff.Mode.SRC_ATOP);
		mItemNavigation.setName(MY_WISHLIST);
		mItemNavigation.setIcon(icon);
		mItems.add(mItemNavigation);

		Fragment fragment =  MyWishListFragment.newInstance();
		mFragments.put(mItemNavigation.getName(),
				fragment.getClass().getName());
	}

}

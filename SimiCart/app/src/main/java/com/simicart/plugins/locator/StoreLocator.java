package com.simicart.plugins.locator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageFragment;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageTabletFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreLocator {
//	SlideMenuData mSlideMenuData;

    public StoreLocator() {
        Log.e("abc", "here");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                addStoreLocatorMenuItem(intent);
            }
        };

        IntentFilter filter = new IntentFilter("com.simicart.menuleft.additem.more");
        Context context = SimiManager.getIntance().getCurrentActivity();
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
//		this.mSlideMenuData = slideMenuData;
//		if (methodName.equals("addItem")) {
//			mItems = mSlideMenuData.getItemNavigations();
//			ItemNavigation mItemNavigation = new ItemNavigation();
//			mItemNavigation.setType(ItemNavigation.TypeItem.PLUGIN);
//			mItemNavigation.setIcon(AppColorConfig.getInstance().getIcon("plugins_locator", AppColorConfig.getInstance().getMenuIconColor()));
//			mItemNavigation.setName(SimiTranslator.getInstance().translate("Store Locator"));
//			mItems.add(mItemNavigation);
//
//			if(DataLocal.isTablet) {
//				StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance();
//				mSlideMenuData.getPluginFragment().put(mItemNavigation.getName(),
//						fragment.getClass().getName());
//			} else {
//				StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance();
//				mSlideMenuData.getPluginFragment().put(mItemNavigation.getName(),
//						fragment.getClass().getName());
//			}
//		}
    }

    protected void addStoreLocatorMenuItem(Intent intent) {
        Bundle bundle = intent.getBundleExtra("data");
        SimiData data = bundle.getParcelable("entity");
        ArrayList<ItemNavigation> mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
        HashMap<String, String> mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);

        ItemNavigation mItemNavigation = new ItemNavigation();
        mItemNavigation.setType(ItemNavigation.TypeItem.PLUGIN);
        mItemNavigation.setIcon(AppColorConfig.getInstance().getIcon("plugins_locator", AppColorConfig.getInstance().getMenuIconColor()));
        mItemNavigation.setName("Store Locator");
        if (DataLocal.isTablet) {
            StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance();
            mFragments.put(mItemNavigation.getName(),
                    fragment.getClass().getName());
        } else {
            StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance();
            mFragments.put(mItemNavigation.getName(),
                    fragment.getClass().getName());
        }
        mItems.add(mItemNavigation);
    }

}

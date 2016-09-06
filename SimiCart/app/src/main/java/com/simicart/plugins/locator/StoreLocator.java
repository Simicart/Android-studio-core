package com.simicart.plugins.locator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageFragment;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageTabletFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreLocator {

    protected ArrayList<ItemNavigation> mItems;

    public StoreLocator() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                addStoreLocatorMenuItem(intent);
            }
        };
        SimiEvent.registerEvent(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_MORE, receiver);
    }

    protected void addStoreLocatorMenuItem(Intent intent) {
        Bundle bundle = intent.getBundleExtra("data");
        SimiData data = bundle.getParcelable("entity");
        mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
        HashMap<String, String> mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);

        if(isExistStoreLocator() == false) {
            ItemNavigation mItemNavigation = new ItemNavigation();
            mItemNavigation.setType(ItemNavigation.TypeItem.PLUGIN);
            mItemNavigation.setIcon(AppColorConfig.getInstance().getIcon("plugins_locator", AppColorConfig.getInstance().getMenuIconColor()));
            mItemNavigation.setName("Store Locator");
            if (DataLocal.isTablet) {
                StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance(null);
                mFragments.put(mItemNavigation.getName(),
                        fragment.getClass().getName());
            } else {
                StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance(null);
                mFragments.put(mItemNavigation.getName(),
                        fragment.getClass().getName());
            }
            mItems.add(mItemNavigation);
        }
    }

    protected boolean isExistStoreLocator() {
        for (ItemNavigation item : mItems) {
            if (item.getName().equals(SimiTranslator.getInstance().translate(
                    "Store Locator"))) {
                return true;
            }
        }
        return false;
    }

}

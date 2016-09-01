package com.simicart.plugins.instantcontact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.core.slidemenu.entity.ItemNavigation.TypeItem;
import com.simicart.plugins.instantcontact.fragment.ContactUsFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactUs {

	protected Context mContext;
	protected ArrayList<ItemNavigation> mItems;

	public ContactUs() {

		mContext = SimiManager.getIntance().getCurrentActivity();

		// register event: add navigation item to slide menu
		IntentFilter addItemFilter = new IntentFilter(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_MORE);
		BroadcastReceiver addItemReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getBundleExtra(Constants.DATA);
				SimiData data = bundle.getParcelable("entity");
				mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
				HashMap<String, String> mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);

				if(isExistContactUs() == false) {
					ItemNavigation mItemNavigation = new ItemNavigation();
					mItemNavigation.setType(TypeItem.PLUGIN);
					mItemNavigation.setShowPopup(true);
					Drawable iconContactUs = mContext.getResources()
							.getDrawable(
									Rconfig.getInstance().drawable(
											"plugins_contactus_icon"));
					iconContactUs.setColorFilter(AppColorConfig.getInstance().getMenuIconColor(),
							PorterDuff.Mode.SRC_ATOP);
					mItemNavigation.setName("Contact Us");
					mItemNavigation.setIcon(iconContactUs);
					mItems.add(mItemNavigation);

					ContactUsFragment fragment = ContactUsFragment.newInstance();
					mFragments.put(mItemNavigation.getName(),
							fragment.getClass().getName());
				}
			}
		};
		LocalBroadcastManager.getInstance(mContext).registerReceiver(addItemReceiver, addItemFilter);
	}

	protected boolean isExistContactUs() {
		for (ItemNavigation item : mItems) {
			if (item.getName().equals(SimiTranslator.getInstance().translate(
					"Contact Us"))) {
				return true;
			}
		}
		return false;
	}

}

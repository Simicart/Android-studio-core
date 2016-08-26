package com.simicart.core.customer.controller;

import com.simicart.core.base.component.SimiMenuRowComponent;
import com.simicart.core.base.component.callback.MenuRowCallBack;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.delegate.MyAccountDelegate;
import com.simicart.core.customer.fragment.AddressBookFragment;
import com.simicart.core.customer.fragment.OrderHistoryFragment;
import com.simicart.core.customer.fragment.ProfileFragment;

import java.util.HashMap;

public class MyAccountController extends SimiController {

	protected MyAccountDelegate mDelegate;

	public void setDelegate(MyAccountDelegate delegate) {
		mDelegate = delegate;
	}

	@Override
	public void onStart() {

		SimiMenuRowComponent profileRowComponent = new SimiMenuRowComponent();
		profileRowComponent.setIcon("ic_acc_profile");
		profileRowComponent.setLabel(SimiTranslator.getInstance().translate("Profile"));
		profileRowComponent.setmCallBack(new MenuRowCallBack() {
			@Override
			public void onClick() {
				ProfileFragment fragment = ProfileFragment.newInstance();
				if (DataLocal.isTablet) {
					SimiManager.getIntance().replacePopupFragment(fragment);
				} else {
					SimiManager.getIntance().replaceFragment(fragment);
				}
			}
		});
		mDelegate.addItemRow(profileRowComponent.createView());

		SimiMenuRowComponent addressBookRowComponent = new SimiMenuRowComponent();
		addressBookRowComponent.setIcon("ic_acc_address");
		addressBookRowComponent.setLabel(SimiTranslator.getInstance().translate("Address Book"));
		addressBookRowComponent.setmCallBack(new MenuRowCallBack() {
			@Override
			public void onClick() {
				HashMap<String, Object> hmData = new HashMap<>();
				hmData.put(KeyData.ADDRESS_BOOK.OPEN_FOR, ValueData.ADDRESS_BOOK.OPEN_FOR_CUSTOMER);
				SimiData data = new SimiData(hmData);
				AddressBookFragment fragment = AddressBookFragment.newInstance(data);
				if (DataLocal.isTablet) {
					SimiManager.getIntance().replacePopupFragment(fragment);
				} else {
					SimiManager.getIntance().replaceFragment(fragment);
				}
			}
		});
		mDelegate.addItemRow(addressBookRowComponent.createView());

		SimiMenuRowComponent orderHistoryRowComponent = new SimiMenuRowComponent();
		orderHistoryRowComponent.setIcon("ic_acc_history");
		orderHistoryRowComponent.setLabel(SimiTranslator.getInstance().translate("Order History"));
		orderHistoryRowComponent.setmCallBack(new MenuRowCallBack() {
			@Override
			public void onClick() {
				OrderHistoryFragment fragment = OrderHistoryFragment.newInstance();
				if (DataLocal.isTablet) {
					SimiManager.getIntance().replacePopupFragment(fragment);
				} else {
					SimiManager.getIntance().replaceFragment(fragment);
				}
			}
		});
		mDelegate.addItemRow(orderHistoryRowComponent.createView());

		SimiMenuRowComponent signOutRowComponent = new SimiMenuRowComponent();
		signOutRowComponent.setIcon("ic_acc_logout");
		signOutRowComponent.setLabel(SimiTranslator.getInstance().translate("Sign Out"));
		signOutRowComponent.setmCallBack(new MenuRowCallBack() {
			@Override
			public void onClick() {
				SimiManager.getIntance().removeDialog();
				SignOutController signout = new SignOutController();
				signout.setDelegate(mDelegate);
				signout.onStart();
			}
		});
		mDelegate.addItemRow(signOutRowComponent.createView());

	}

	@Override
	public void onResume() {

	}
}

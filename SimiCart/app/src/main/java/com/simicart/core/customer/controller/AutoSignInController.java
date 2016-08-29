package com.simicart.core.customer.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.model.SignInModel;

import java.util.HashMap;

public class AutoSignInController extends SimiController {

	@Override
	public void onStart() {
		String typeSignIn = DataPreferences.getTypeSignIn();
		DataPreferences.saveSignInState(false);
		if (typeSignIn.equals(Constants.NORMAL_SIGN_IN)) {
			final String email = DataPreferences.getEmail();
			final String password = DataPreferences.getPassword();
			final SignInModel model = new SignInModel();
			model.setSuccessListener(new ModelSuccessCallBack() {
				@Override
				public void onSuccess(SimiCollection collection) {
					DataLocal.isNewSignIn = true;
					DataPreferences.saveSignInState(true);
					String name = model.getName();
					String cartQty = model.getCartQty();
					if (null != name) {
						DataPreferences.saveData(name, email, password);

					}

					// update wishlist_items_qty
//					EventController event = new EventController();
//					event.dispatchEvent(
//							"com.simicart.core.customer.controller.SignInController",
//							model.getJSON().toString());
					if (null != cartQty) {
						DataLocal.qtyCartAuto = cartQty;
						SimiManager.getIntance().onUpdateCartQty(cartQty);
					}
				}
			});
			model.addBody(Constants.USER_EMAIL, email);
			model.addBody(Constants.USER_PASSWORD, password);
			model.request();
		} else {
			// event for face book
			SimiEvent.dispatchEvent(KeyEvent.FACEBOOK_EVENT.FACEBOOK_LOGIN_AUTO, new HashMap<String, Object>());
//			EventController event = new EventController();
//			event.dispatchEvent("com.simicart.autoSignIn", typeSignIn);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

}

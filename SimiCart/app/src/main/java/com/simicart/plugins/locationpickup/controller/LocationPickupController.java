package com.simicart.plugins.locationpickup.controller;


import com.simicart.core.checkout.fragment.ReviewOrderFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.controller.NewAddressBookController;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.entity.StateOfCountry;
import com.simicart.core.customer.fragment.AddressBookFragment;
import com.simicart.core.customer.model.NewAddressBookModel;

public class LocationPickupController extends NewAddressBookController{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void OnRequestChangeAddress(MyAddress address) {
		mDelegate.showLoading();
		mModel = new NewAddressBookModel();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				
				if (isSuccess) {
					if (mAfterController == Constants.NEW_ADDRESS) {
						AddressBookFragment fragment = AddressBookFragment
								.newInstance();
//						SimiManager.getIntance().replacePopupFragment(fragment);
					} else {
						MyAddress newAddress = (MyAddress) mModel
								.getCollection().getCollection().get(0);

						CountryAllowed country = getCurrentCountry(mCurrentCountry);
						if (null != country) {
							newAddress.setCountryName(mCurrentCountry);
							newAddress.setCountryCode(country.getCountry_code());
							if (!mCurrentState.equals("")) {
								StateOfCountry state = getCurrentState(
										mCurrentState, country);
								if (null != state) {
									newAddress.setStateName(mCurrentState);
									newAddress.setStateCode(state
											.getState_code());
									newAddress.setStateId(state.getState_id());
								}
							}
						}

						if (null != newAddress) {
							MyAddress shippingAdd = null, billingAdd = null;
							if (mAfterController == Constants.NEW_ADDRESS_CHECKOUT) {
								switch (addressFor) {
								case Constants.KeyAddress.ALL_ADDRESS:
									billingAdd = newAddress;
									shippingAdd = newAddress;
									break;
								case Constants.KeyAddress.BILLING_ADDRESS:
									billingAdd =newAddress;
									shippingAdd = mShippingAddress;
									break;
								case Constants.KeyAddress.SHIPPING_ADDRESS:
									billingAdd =mBillingAddress;
									shippingAdd = newAddress;
									break;
								default:
									break;
								}
								ReviewOrderFragment fragment = ReviewOrderFragment
										.newInstance(mAfterController, shippingAdd, billingAdd);
//								SimiManager.getIntance().replacePopupFragment(
//										fragment);
							} else {
								billingAdd = newAddress;
								shippingAdd = newAddress;
								ReviewOrderFragment fragment = ReviewOrderFragment
										.newInstance(-1, shippingAdd, billingAdd);
								//SimiManager.getIntance().replacePopupFragment(
								//		fragment);
							}
						}
					}
				} else {
					//SimiManager.getIntance().showNotify("FAIL", message, "OK");
				}
			}
		});

		CountryAllowed country = getCurrentCountry(mCurrentCountry);
		if (null != country) {
			address.setCountryName(mCurrentCountry);
			address.setCountryCode(country.getCountry_code());
			if (!mCurrentState.equals("")) {
				StateOfCountry state = getCurrentState(mCurrentState, country);
				if (null != state) {
					address.setStateName(mCurrentState);
					address.setStateCode(state.getState_code());
					address.setStateId(state.getState_id());
				}
			}
		}

//		List<NameValuePair> params = address.toParamsRequest();
//		for (NameValuePair nameValuePair : params) {
//			String key = nameValuePair.getName();
//			String value = nameValuePair.getValue();
//			mModel.addParam(key, value);
//		}
//
//		String lat = address.getBundle().getString("lat");
//		String lng = address.getBundle().getString("long");
//		if(!lat.equals("") && !lng.equals("")){
//			mModel.addParam("latlng", lat + ", " + lng);
//		}
		mModel.request();
	}
}

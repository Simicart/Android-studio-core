package com.simicart.core.customer.controller;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.checkout.fragment.ReviewOrderFragment;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.delegate.NewAddressBookDelegate;
import com.simicart.core.customer.entity.AddressEntity;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.CountryEntity;
import com.simicart.core.customer.entity.ProfileEntity;
import com.simicart.core.customer.entity.StateEntity;
import com.simicart.core.customer.model.GetCountryModel;
import com.simicart.core.customer.model.NewAddressBookModel;

@SuppressLint("DefaultLocale")
public class NewAddressBookController extends SimiController
         {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static int TYPE_SELECT_STATE = 0;
    public static int TYPE_SELECT_COUNTRY = 1;

    protected ArrayList<CountryEntity> mListCountryAllowed;
    protected OnClickListener mChooseCountry;
    protected OnClickListener mChooseStates, mClickShowGender;
    protected NewAddressBookDelegate mDelegate;
    protected OnClickListener mClickSave;
    protected ArrayList<String> mListCountry;
    protected String mCurrentCountry = "";
    protected String mCurrentState = "";
    protected AddressEntity mBillingAddress;
    protected AddressEntity mShippingAddress;

    public void setBillingAddress(AddressEntity mBillingAddress) {
        this.mBillingAddress = mBillingAddress;
    }

    public void setShippingAddress(AddressEntity mShippingAddress) {
        this.mShippingAddress = mShippingAddress;
    }

    protected int mAfterController;
    protected int addressFor = -1;

    public void setAddressFor(int addresFor) {
        this.addressFor = addresFor;
    }

    public int getAddressFor() {
        return this.addressFor;
    }

    public void setAfterController(int controll) {
        mAfterController = controll;
    }

    public OnClickListener getClickSave() {
        return mClickSave;
    }

    public OnClickListener getChooseCountry() {
        return mChooseCountry;
    }

    public OnClickListener getChooseStates() {
        return mChooseStates;
    }

    public OnClickListener showGender() {
        return mClickShowGender;
    }

    public void setDelegate(NewAddressBookDelegate mDelegate) {
        if (mDelegate != null)
            this.mDelegate = mDelegate;
    }

    @Override
    public void onStart() {
        onRequestCountryAllowed();
        mClickShowGender = new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDelegate.getGender().performClick();
            }
        };

        mClickSave = new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AddressEntity address = mDelegate.getNewAddressBook();
                String state = address.getStateName();
                if (null != state) {
                    mCurrentState = state;
                }
                String country = address.getCountryName();
                if (null != country) {
                    mCurrentCountry = country;
                }
                CountryEntity countryAllow = getCurrentCountry(mCurrentCountry);
                if (null != countryAllow) {
//                    address.setCountryName(mCurrentCountry);
//                    address.setCountryCode(countryAllow.getCountry_code());
//                    if (!mCurrentState.equals("")) {
//                        StateEntity stateOfCountry = getCurrentState(
//                                mCurrentState, countryAllow);
//                        if (null != stateOfCountry) {
//                            address.setStateName(mCurrentState);
//                            address.setStateCode(stateOfCountry.getState_code());
//                            address.setStateId(stateOfCountry.getState_id());
//                        }
//
//                    }
                }
                AddressEntity shippingAdd = null, billingAdd = null;
                int afterControll = 0;
                Utils.hideKeyboard(arg0);
                if (isCompleteRequired(address)) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(
                            address.getEmail()).matches()) {
                        SimiManager.getIntance().removeDialog();
                        switch (mAfterController) {
                            case Constants.NEW_AS_GUEST:
                                shippingAdd = address;
                                billingAdd = address;
                                afterControll = mAfterController;
                                Log.d("duyquang", "==1==NEW_AS_GUEST");
                                SimiManager.getIntance().replaceFragment(
                                        ReviewOrderFragment.newInstance(
                                                afterControll, shippingAdd,
                                                billingAdd));
                                break;
                            case Constants.NEW_CUSTOMER:
                                onNewCustomer(address);
                                break;
                            case Constants.EDIT_ADDRESS:
                                onEditAddress(address);
                                break;
                            default:
                                OnRequestChangeAddress(address);
                                break;
                        }
                        ConfigCheckout.getInstance().setStatusAddressBook(true);
                    } else {
//						SimiManager.getIntance().showNotify(
//								null,
//								Config.getInstance().getText(
//										"Invalid email address"),
//								Config.getInstance().getText("OK"));
                    }
                } else {
//					SimiManager.getIntance().showNotify(null,
//							"Please select all (*) fields", "OK");
                }
            }
        };

        mChooseCountry = new OnClickListener() {

            @Override
            public void onClick(View v) {
                changeFragmentCountry(TYPE_SELECT_COUNTRY, mListCountry);
            }
        };

        mChooseStates = new OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<String> states = getStateFromCountry(mCurrentCountry,
                        mListCountryAllowed);
                if (null != states && states.size() > 0) {
                    changeFragmentCountry(
                            TYPE_SELECT_STATE,
                            getStateFromCountry(mCurrentCountry,
                                    mListCountryAllowed));
                }
            }
        };
        mDelegate.createView(mAfterController);
    }

    private void onEditAddress(AddressEntity address) {
        AddressEntity shippingAdd = null, billingAdd = null;
        billingAdd = address;
        shippingAdd = address;

        if (addressFor == Constants.KeyAddress.BILLING_ADDRESS) {
            billingAdd = address;
            shippingAdd = mShippingAddress;
        } else if (addressFor == Constants.KeyAddress.SHIPPING_ADDRESS) {
            shippingAdd = address;
            billingAdd = mBillingAddress;
        } else {
            billingAdd = address;
            shippingAdd = address;
        }
        SimiManager.getIntance().replaceFragment(
                ReviewOrderFragment.newInstance(mAfterController, shippingAdd,
                        billingAdd));
    }

    private void onNewCustomer(AddressEntity address) {
        AddressEntity shippingAdd = null, billingAdd = null;
        ProfileEntity profile = mDelegate.getProfileEntity();
        if (null != profile) {
            String name = profile.getName();
            String email = profile.getEmail();
            String password = profile.getCurrentPass();
            DataPreferences.saveData(email, password);
            DataPreferences.saveData(name, email, password);
            billingAdd = address;
            // fragment.setAfterControll(mAfterController);
            shippingAdd = address;

            if (addressFor == Constants.KeyAddress.BILLING_ADDRESS) {
                billingAdd = address;
                shippingAdd = mShippingAddress;
            } else if (addressFor == Constants.KeyAddress.SHIPPING_ADDRESS) {
                shippingAdd = address;
                billingAdd = mBillingAddress;
            } else {
                billingAdd = address;
                shippingAdd = address;
            }
            SimiManager.getIntance().replaceFragment(
                    ReviewOrderFragment.newInstance(mAfterController,
                            shippingAdd, billingAdd));
        }
    }

    @Override
    public void onResume() {
        mDelegate.createView(mAfterController);
        mDelegate.updateView(mModel.getCollection());
        mDelegate.updateCountry(mCurrentCountry);
        mDelegate.updateState(mCurrentState);
    }

    protected void onRequestCountryAllowed() {
        mDelegate.showLoading();
        mModel = new GetCountryModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                ArrayList<SimiEntity> entity = mModel.getCollection()
                        .getCollection();
                if (null != entity && entity.size() > 0) {
//                    mListCountryAllowed = new ArrayList<CountryEntity>();
//                    for (SimiEntity simiEntity : entity) {
//                        CountryEntity country_add = (CountryEntity) simiEntity;
//                        mListCountryAllowed.add(country_add);
//                    }
//                    mListCountry = new ArrayList<String>();
//                    for (int i = 0; i < mListCountryAllowed.size(); i++) {
//                        mListCountry.add(mListCountryAllowed.get(i)
//                                .getCountry_name());
//                    }
//                    String name = mListCountryAllowed.get(0)
//                            .getCountry_name();
//
//                    if (null != name) {
//                        mDelegate.updateCountry(name);
//                        mCurrentCountry = name;
//                        ArrayList<String> states = getStateFromCountry(
//                                name, mListCountryAllowed);
//                        if (null != states && states.size() > 0) {
//                            mCurrentState = states.get(0);
//                        } else {
//                            mCurrentState = "";
//                        }
//                        mDelegate.updateState(mCurrentState);
//                        mDelegate.updateView(mModel.getCollection());
//                    }
                }
            }
        });

        mModel.request();
    }

    protected void OnRequestChangeAddress(AddressEntity address) {
        mDelegate.showLoading();
        mModel = new NewAddressBookModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                if (mAfterController == Constants.NEW_ADDRESS) {
//                    AddressBookFragment fragment = AddressBookFragment
//                            .newInstance();
//						SimiManager.getIntance().replacePopupFragment(fragment);
                } else {
                    AddressEntity newAddress = (AddressEntity) mModel
                            .getCollection().getCollection().get(0);

                    CountryEntity country = getCurrentCountry(mCurrentCountry);
                    if (null != country) {
//                        newAddress.setCountryName(mCurrentCountry);
//                        newAddress.setCountryCode(country.getCountry_code());
//                        if (!mCurrentState.equals("")) {
//                            StateEntity state = getCurrentState(
//                                    mCurrentState, country);
//                            if (null != state) {
//                                newAddress.setStateName(mCurrentState);
//                                newAddress.setStateCode(state
//                                        .getState_code());
//                                newAddress.setStateId(state.getState_id());
//                            }
//                        }
                    }

                    if (null != newAddress) {

                        AddressEntity shippingAdd = null, billingAdd = null;
                        if (mAfterController == Constants.NEW_ADDRESS_CHECKOUT) {
                            switch (addressFor) {
//                                case Constants.KeyAddress.ALL_ADDRESS:
//                                    billingAdd = newAddress;
//                                    shippingAdd = newAddress;
//                                    break;
                                case Constants.KeyAddress.BILLING_ADDRESS:
                                    billingAdd = newAddress;
                                    shippingAdd = mShippingAddress;
                                    break;
                                case Constants.KeyAddress.SHIPPING_ADDRESS:
                                    billingAdd = mBillingAddress;
                                    shippingAdd = newAddress;
                                    break;
                                default:
                                    break;
                            }
                            Log.d("duyquang", "=4=");
                            ReviewOrderFragment fragment = ReviewOrderFragment
                                    .newInstance(mAfterController,
                                            shippingAdd, billingAdd);
                            SimiManager.getIntance().replaceFragment(
                                    fragment);
                        } else {
                            billingAdd = newAddress;
                            shippingAdd = newAddress;
                            ReviewOrderFragment fragment = ReviewOrderFragment
                                    .newInstance(-1, shippingAdd,
                                            billingAdd);
                            SimiManager.getIntance().replaceFragment(
                                    fragment);
                        }
                    }
                }
            }
        });

        CountryEntity country = getCurrentCountry(mCurrentCountry);
        if (null != country) {
//            address.setCountryName(mCurrentCountry);
//            address.setCountryCode(country.getCountry_code());
//            if (!mCurrentState.equals("")) {
//                StateEntity state = getCurrentState(mCurrentState, country);
//                if (null != state) {
//                    address.setStateName(mCurrentState);
//                    address.setStateCode(state.getState_code());
//                    address.setStateId(state.getState_id());
//                }
//            }
        }

//		List<NameValuePair> params = address.toParamsRequest();
//		for (NameValuePair nameValuePair : params) {
//			String key = nameValuePair.getName();
//			String value = nameValuePair.getValue();
//			mModel.addParam(key, value);
//		}

        mModel.request();
    }

    protected CountryEntity getCurrentCountry(String name) {
//        if (null != name && null != mListCountryAllowed)
//            for (CountryEntity ele : mListCountryAllowed) {
//                if (ele.getCountry_name().equals(name)) {
//                    return ele;
//                }
//            }

        return null;
    }

    protected StateEntity getCurrentState(String name, CountryEntity country) {
        if (null != name && null != country) {
            ArrayList<StateEntity> states = country.getStateList();
            if (null != states && states.size() > 0) {
                for (StateEntity state : states) {
//                    if (state.getState_name().equals(name)) {
//                        return state;
//                    }
                }
            }
        }

        return null;
    }

    private boolean isCompleteRequired(AddressEntity add_address) {
        // ConfigCustomerAddress _configCustomer =
        // DataLocal.ConfigCustomerAddress;
        // if (mAfterController == NewAddressBookFragment.NEW_ADDRESS_CHECKOUT
        // || mAfterController == NewAddressBookFragment.NEW_AS_GUEST
        // || mAfterController == NewAddressBookFragment.NEW_CUSTOMER) {
        ConfigCustomerAddress _configCustomer = DataLocal.ConfigCustomerAddress;
        // }
        String name = add_address.getName();
        String email = add_address.getEmail();

        if (null == name || name.equals("") || null == email
                || email.equals("")) {
            return false;
        } else {
            if (_configCustomer.getPrefix().toLowerCase().equals("req")
                    && (add_address.getPrefix() == null || add_address
                    .getPrefix().equals(""))) {
                return false;
            }
            if (_configCustomer.getSuffix().toLowerCase().equals("req")
                    && (add_address.getSuffix() == null || add_address
                    .getSuffix().equals(""))) {
                return false;
            }

            if (mAfterController == Constants.NEW_CUSTOMER
                    || mAfterController == Constants.NEW_AS_GUEST) {
                if (_configCustomer.getTaxvat().toLowerCase().equals("req")
                        && (add_address.getTaxvat() == null || add_address
                        .getTaxvat().equals(""))) {
                    return false;
                }

                if (_configCustomer.getGender().toLowerCase().equals("req")
                        && (null == add_address.getGender() || add_address
                        .getGender().equals(""))) {
                    return false;
                }

                if (_configCustomer.getDob().toLowerCase().equals("req")
                        && (null == add_address.getDay() || add_address
                        .getDay().equals(""))) {
                    return false;
                }

            }

            if (_configCustomer.getStreet().toLowerCase().equals("req")
                    && (add_address.getStreet() == null || add_address
                    .getStreet().equals(""))) {
                return false;
            }
            if (_configCustomer.getCity().toLowerCase().equals("req")
                    && (add_address.getCity() == null || add_address.getCity()
                    .equals(""))) {
                return false;
            }
            if (_configCustomer.getZipcode().toLowerCase().equals("req")
                    && (add_address.getZipCode() == null || add_address
                    .getZipCode().equals(""))) {
                return false;
            }
            if (_configCustomer.getTelephone().toLowerCase().equals("req")
                    && (add_address.getPhone() == null || add_address
                    .getPhone().equals(""))) {
                return false;
            }
            if (_configCustomer.getFax().toLowerCase().equals("req")
                    && (add_address.getFax() == null || add_address.getFax()
                    .equals(""))) {
                return false;
            }
            if (_configCustomer.getVat_id().toLowerCase().equals("req")
                    && (add_address.getTaxvatCheckout() == null || add_address
                    .getTaxvatCheckout().equals(""))) {
                return false;
            }
            if (_configCustomer.getCompany().toLowerCase().equals("req")
                    && (add_address.getCompany() == null || add_address
                    .getCompany().equals(""))) {
                return false;
            }
            return true;
        }
    }

    protected void changeFragmentCountry(int type,
                                         ArrayList<String> list_country) {

//		SimiManager.getIntance().replacePopupFragment(fragment);
    }

    public ArrayList<String> getStateFromCountry(String country,
                                                 ArrayList<CountryEntity> listCountry) {
        ArrayList<String> states = new ArrayList<String>();
        for (CountryEntity countryAllowed : listCountry) {
//            if (countryAllowed.getCountry_name().equals(country)) {
//                for (StateEntity state : countryAllowed.getStateList()) {
//                    states.add(state.getState_name());
//                }
//                return states;
//            }
        }
        return states;
    }




}

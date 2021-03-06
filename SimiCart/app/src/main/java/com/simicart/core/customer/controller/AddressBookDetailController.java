package com.simicart.core.customer.controller;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.component.GenderAdapter;
import com.simicart.core.base.component.SimiDOBRowComponent;
import com.simicart.core.base.component.SimiNavigationRowComponent;
import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.component.SimiSpinnerRowComponent;
import com.simicart.core.base.component.SimiTextRowComponent;
import com.simicart.core.base.component.callback.NavigationRowCallBack;
import com.simicart.core.base.component.callback.SpinnerRowCallBack;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.customer.delegate.AddressBookDetailDelegate;
import com.simicart.core.customer.delegate.ListOfChoiceDelegate;
import com.simicart.core.customer.entity.AddressEntity;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.CountryEntity;
import com.simicart.core.customer.entity.GenderConfig;
import com.simicart.core.customer.entity.StateEntity;
import com.simicart.core.customer.model.AddressBookDetailModel;
import com.simicart.core.customer.model.GetCountryModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressBookDetailController extends SimiController {
    protected OnClickListener mSaveListener;
    protected AddressBookDetailDelegate mDelegate;
    protected HashMap<String, Object> hmData;
    protected ArrayList<SimiRowComponent> mListRowComponent;
    protected SimiNavigationRowComponent mCountryComponent;
    protected SimiNavigationRowComponent mStateComponent;
    protected int openFor;
    protected int action;
    protected AddressEntity mShippingAddress;
    protected AddressEntity mBillingAddress;
    protected AddressEntity mAddressForEdit;
    protected ArrayList<CountryEntity> mListCountry;
    protected CountryEntity mCountry;
    protected StateEntity mState;
    protected SimiTextRowComponent passwordComponent;
    protected SimiTextRowComponent confirmPasswordComponent;
    protected SimiDOBRowComponent dobRowComponent;
    protected GenderConfig mGender;

    @Override
    public void onStart() {

        parseParam();

        initListener();

        onRequestCountryAllowed();

    }

    protected void parseParam() {
        if (null != hmData) {

            if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.OPEN_FOR)) {
                Integer open = (Integer) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.OPEN_FOR);
                this.openFor = open.intValue();
            }

            if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.ACTION)) {
                Integer act = (Integer) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.ACTION);
                this.action = act.intValue();
            }

            if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS)) {
                mBillingAddress = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS);
            }

            if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS)) {
                mShippingAddress = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS);
            }

            if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.ADDRESS_FOR_EDIT)) {
                mAddressForEdit = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.ADDRESS_FOR_EDIT);
            }
        }
    }

    protected void initListener() {
        mSaveListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        };
    }


    protected void onRequestCountryAllowed() {
        mDelegate.showLoading();

        final GetCountryModel getCountryModel = new GetCountryModel();

        getCountryModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.updateView(null);
                ArrayList<SimiEntity> entities = getCountryModel.getCollection()
                        .getCollection();
                if (null != entities && entities.size() > 0) {
                    mListCountry = new ArrayList<>();
                    for (int i = 0; i < entities.size(); i++) {
                        CountryEntity country = (CountryEntity) entities.get(i);
                        mListCountry.add(country);

                    }
                    if (null != mListCountry && mListCountry.size() > 0) {
                        setupDefaultValue();
                    }
                }
                showView();
            }
        });

        getCountryModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissLoading();
            }
        });

        getCountryModel.request();
    }

    protected void setupDefaultValue() {
        if (null == mCountry) {
            mCountry = mListCountry.get(0);
            ArrayList<StateEntity> states = mCountry.getStateList();
            if (null != states && states.size() > 0) {
                mState = states.get(0);
            }
        }
    }

    protected void showView() {
        if (null == mListRowComponent) {
            mListRowComponent = new ArrayList<>();
        }


        if (this.openFor == ValueData.ADDRESS_BOOK_DETAIL.OPEN_FOR_CUSTOMER) {
            showViewForCustomer();
        } else {
            if (this.action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_GUEST) {
                showViewForGuest();
            } else if (this.action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_NEW_CUSTOMER) {
                showViewForNewCustomer();
            } else {
                // create a new address for checkout as existing customer
                showViewForCustomer();
            }
        }

        // dispatch event for plugins
        // event for address auto fill
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(KeyData.ADDRESS_AUTO_FILL.LIST_COMPONENTS, mListRowComponent);
        hmData.put(KeyData.ADDRESS_AUTO_FILL.LIST_COUNTRIES, mListCountry);
        SimiEvent.dispatchEvent(KeyEvent.ADDRESS_AUTO_FILL.ADDRESS_AUTO_FILL_ADD_MAP, hmData);

        ArrayList<View> listRow = new ArrayList<>();
        if (null != mListRowComponent) {
            for (int i = 0; i < mListRowComponent.size(); i++) {
                SimiRowComponent component = mListRowComponent.get(i);
                View view = component.createView();
                listRow.add(view);
            }
        }
        mDelegate.showRows(listRow);

    }

    protected void showViewForCustomer() {
        // prefix
        initComponent("prefix", "Prefix", "prefix", "prefix", (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME));

        // name
        initComponentRequired("Name", "name", "name", (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME));

        // suffix
        initComponent("suffix", "Suffix", "suffix", "suffix", (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME));

        // email
        initComponentRequired("Email", "email", "email", (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));

        // company
        initComponent("company", "Company", "company", "company", InputType.TYPE_CLASS_TEXT);

        // phone
        initComponent("phone", "Telephone", "phone", "phone", (InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE));

        // fax
        initComponent("fax", "Fax", "fax", "fax", InputType.TYPE_CLASS_TEXT);

        // country
        initCountryComponent();

        // state
        initStateComponent();

        // city
        initComponent("city", "City", "city", "city", InputType.TYPE_CLASS_TEXT);

        // street
        initComponent("street", "Street", "street", "street", InputType.TYPE_CLASS_TEXT);

        // ZIP code
        initComponent("zipcode", "Zip Code", "zip", "zip", InputType.TYPE_CLASS_TEXT);


    }

    protected void showViewForGuest() {

        showViewForCustomer();

        // tax/vat number
        initComponent("taxvat", "Tax/VAT number", "taxvat", "taxvat", InputType.TYPE_CLASS_TEXT);

        // VAT number vat_id
        initComponent("vat_id", "VAT number", "vat_id", "vat_id", InputType.TYPE_CLASS_TEXT);

        // date of birth
        initDOBComponent();

        // gender
        initGenderComponent();
    }

    protected void showViewForNewCustomer() {


        showViewForGuest();

        // pass
        passwordComponent = new SimiTextRowComponent();
        passwordComponent.setTitle("Password");
        passwordComponent.setRequired(true);
        if (this.action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_EDIT && null != mAddressForEdit) {
            String name = mAddressForEdit.getData("user_password");
            passwordComponent.setValue(name);
        }
        passwordComponent.setKey("user_password");
        passwordComponent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mListRowComponent.add(passwordComponent);


        // confirm pass
        confirmPasswordComponent = new SimiTextRowComponent();
        confirmPasswordComponent.setTitle("Confirm Password");
        confirmPasswordComponent.setRequired(true);
        if (this.action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_EDIT && null != mAddressForEdit) {
            String name = mAddressForEdit.getData("user_password");
            confirmPasswordComponent.setValue(name);
        }
        confirmPasswordComponent.setKey("user_password");
        confirmPasswordComponent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mListRowComponent.add(confirmPasswordComponent);
    }


    protected void initComponent(String keyForConfig, String title, String keyForData, String keyForParam, int inputType) {
        String configValue = ConfigCustomerAddress.getInstance().getValueWithKey(keyForConfig);
        if (!ConfigCustomerAddress.getInstance().isHidden(configValue)) {
            SimiTextRowComponent component = new SimiTextRowComponent();
            component.setTitle(title);
            boolean isRequired = ConfigCustomerAddress.getInstance().isRequired(configValue);
            component.setRequired(isRequired);
            if (this.action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_EDIT && null != mAddressForEdit) {
                String prefix = mAddressForEdit.getData(keyForData);
                component.setValue(prefix);
            }
            component.setKey(keyForParam);
            component.setInputType(inputType);
            mListRowComponent.add(component);
        }
    }

    protected void initComponentRequired(String title, String keyForData, String keyForParam, int inputType) {
        SimiTextRowComponent component = new SimiTextRowComponent();
        component.setTitle(title);
        component.setRequired(true);
        if (this.action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_EDIT && null != mAddressForEdit) {
            String name = mAddressForEdit.getData(keyForData);
            component.setValue(name);
        }
        component.setKey(keyForParam);
        component.setInputType(inputType);
        mListRowComponent.add(component);
    }

    protected void initCountryComponent() {
        String configCountry = ConfigCustomerAddress.getInstance().getCountry();
        if (!ConfigCustomerAddress.getInstance().isHidden(configCountry)) {
            mCountryComponent = new SimiNavigationRowComponent();
            if (this.action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_EDIT && null != mAddressForEdit) {
                String countryName = mAddressForEdit.getCountryName();
                mCountryComponent.setValue(countryName);
            } else if (null != mCountry) {
                String countryName = mCountry.getName();
                mCountryComponent.setValue(countryName);
            }

            mCountryComponent.setKey("country_name");
            mCountryComponent.setCallBack(new NavigationRowCallBack() {
                @Override
                public void onNavigate() {
                    openCountryPage();
                }
            });

            mListRowComponent.add(mCountryComponent);
        }
    }

    protected void initStateComponent() {
        String configState = ConfigCustomerAddress.getInstance().getState();
        if (!ConfigCustomerAddress.getInstance().isHidden(configState)) {
            mStateComponent = new SimiNavigationRowComponent();
            if (this.action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_EDIT && null != mAddressForEdit) {
                String stateName = mAddressForEdit.getStateName();
                mStateComponent.setValue(stateName);
            } else if (null != mState) {
                String stateName = mState.getName();
                mStateComponent.setValue(stateName);
            }
            mStateComponent.setKey("state_name");
            mStateComponent.setSuggestValue("State");
            mStateComponent.setCallBack(new NavigationRowCallBack() {
                @Override
                public void onNavigate() {
                    openStatePage();
                }
            });
            mListRowComponent.add(mStateComponent);
        }
    }

    protected void initDOBComponent() {
        String configDOB = ConfigCustomerAddress.getInstance().getDob();
        if (!ConfigCustomerAddress.getInstance().isHidden(configDOB)) {
            int day = 0;
            int month = 0;
            int year = 0;
            if (null != mAddressForEdit) {
                String sday = mAddressForEdit.getDay();
                String sMonth = mAddressForEdit.getMonth();
                String sYear = mAddressForEdit.getYear();
                try {
                    if (Utils.validateString(sday)) {
                        day = Integer.parseInt(sday);
                    }

                    if (Utils.validateString(sMonth)) {
                        month = Integer.parseInt(sMonth);
                    }

                    if (Utils.validateString(sYear)) {
                        year = Integer.parseInt(sYear);
                    }
                } catch (Exception e) {

                }

            }
            dobRowComponent = new SimiDOBRowComponent(day, month, year);
            boolean isRequired = ConfigCustomerAddress.getInstance().isRequired(configDOB);
            dobRowComponent.setRequired(isRequired);
            mListRowComponent.add(dobRowComponent);

        }
    }

    protected void initGenderComponent() {
        String configGender = ConfigCustomerAddress.getInstance().getGender();
        if (!ConfigCustomerAddress.getInstance().isHidden(configGender)) {
            final SimiSpinnerRowComponent genderComponent = new SimiSpinnerRowComponent();
            if (null != mAddressForEdit) {
                String gender = mAddressForEdit.getGender();
                genderComponent.setValue(gender);
            }
            boolean isRequired = ConfigCustomerAddress.getInstance().isRequired(configGender);
            genderComponent.setRequired(isRequired);
            genderComponent.setKey("gender");
            final ArrayList<GenderConfig> genderConfigs = ConfigCustomerAddress.getInstance().getGenderConfigs();
            GenderAdapter adapter = new GenderAdapter(genderConfigs);
            genderComponent.setAdapter(adapter);
            genderComponent.setCallBack(new SpinnerRowCallBack() {
                @Override
                public void onSelect(int position) {
                    mGender = genderConfigs.get(position);
                }
            });
            mListRowComponent.add(genderComponent);
        }
    }

    protected void openCountryPage() {
        HashMap<String, Object> hm = new HashMap<>();
        ListOfChoiceDelegate delegate = new ListOfChoiceDelegate() {
            @Override
            public void chooseItem(String value) {
                if (Utils.validateString(value)) {
                    getCountryWithName(value);
                    mState = null;
                }
            }
        };
        ArrayList<String> listNameCountry = getListNameCountry();
        hm.put(KeyData.LIST_OF_CHOICE.LIST_DATA, listNameCountry);
        hm.put(KeyData.LIST_OF_CHOICE.DELEGATE, delegate);
        SimiManager.getIntance().openListOfChoice(hm);
    }

    protected void openStatePage() {
        if (null != mCountry) {
            final ArrayList<StateEntity> states = mCountry.getStateList();
            ArrayList<String> listNameState = getListNameState(states);
            ListOfChoiceDelegate delegate = new ListOfChoiceDelegate() {
                @Override
                public void chooseItem(String value) {
                    getStateWithName(value, states);
                }
            };
            HashMap<String, Object> hm = new HashMap<>();
            hm.put(KeyData.LIST_OF_CHOICE.DELEGATE, delegate);
            hm.put(KeyData.LIST_OF_CHOICE.LIST_DATA, listNameState);
            SimiManager.getIntance().openListOfChoice(hm);
        }
    }

    protected void onSave() {
        if (this.openFor == ValueData.ADDRESS_BOOK_DETAIL.OPEN_FOR_CUSTOMER) {
            saveForCustomer();
        } else if (this.openFor == ValueData.ADDRESS_BOOK_DETAIL.OPEN_FOR_CHECKOUT) {
            Log.e("Address Detail Controller ", "-----> OPEN FOR CHECKOUT ");
            saveForCheckout();
        }
    }

    protected void saveForCustomer() {
        mDelegate.showLoading();
        mModel = new AddressBookDetailModel();

        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                HashMap<String, Object> hm = new HashMap<>();
                hm.put(KeyData.ADDRESS_BOOK.OPEN_FOR, openFor);
                SimiManager.getIntance().openAddressBook(hm);
            }
        });

        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissLoading();
            }
        });

        if (null != mAddressForEdit) {
            String id = mAddressForEdit.getID();
            if (Utils.validateString(id)) {
                mModel.addBody("address_id", id);
            }
        }

        if (null != mListRowComponent) {
            for (int i = 0; i < mListRowComponent.size(); i++) {
                SimiRowComponent rowComponent = mListRowComponent.get(i);
                SimiRowComponent.TYPE_ROW type = rowComponent.getType();
                if (type == SimiRowComponent.TYPE_ROW.TEXT) {
                    String key = rowComponent.getKey();
                    String value = (String) rowComponent.getValue();
                    if (rowComponent.isRequired() && null == value) {
                        return;
                    }
                    mModel.addBody(key, value);
                }
            }
        }

        if (null != dobRowComponent) {
            ArrayList<Integer> dob = (ArrayList<Integer>) dobRowComponent.getValue();
            int day = dob.get(0);
            int month = dob.get(1);
            int year = dob.get(2);

            mModel.addBody("day", String.valueOf(day));
            mModel.addBody("month", String.valueOf(month));
            mModel.addBody("year", String.valueOf(year));
        }

        if (null != mGender) {
            mModel.addBody("gender", mGender.getValue());
        }

        if (null != mCountry) {
            String name = mCountry.getName();
            if (Utils.validateString(name)) {
                mModel.addBody("country_name", name);
            }

            String code = mCountry.getCode();
            if (Utils.validateString(code)) {
                mModel.addBody("country_code", code);
            }
        }

        if (null != mState) {
            String name = mState.getName();
            if (Utils.validateString(name)) {
                mModel.addBody("state_name", name);
            }

            String code = mState.getCode();
            if (Utils.validateString(code)) {
                mModel.addBody("state_code", code);
            }

            String id = mState.getID();
            if (Utils.validateString(id)) {
                mModel.addBody("state_id", id);
            }
        }


        mModel.request();
    }

    protected void saveForCheckout() {
        try {
            JSONObject jsonAddress = getJSONAddressForCheckout();
            if (null != jsonAddress) {
                AddressEntity addressEntity = new AddressEntity();
                addressEntity.parse(jsonAddress);

                if (action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_NEW_CUSTOMER) {
                    saveForNewCustomer(addressEntity);
                } else if (action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_GUEST) {
                    saveForGuest(addressEntity);

                } else {
                    if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.EDIT_FOR)) {
                        int editFor = (Integer) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.EDIT_FOR);
                        if (editFor == ValueData.ADDRESS_BOOK_DETAIL.EDIT_FOR_NEW_CUSTOMER) {
                            editForNewCustomer(addressEntity);
                        } else if (editFor == ValueData.ADDRESS_BOOK_DETAIL.EDIT_FOR_GUEST) {
                            editForGuest(addressEntity);
                        } else {
                            saveForExitCustomer(addressEntity);
                        }
                    } else {
                        saveForExitCustomer(addressEntity);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void saveForNewCustomer(AddressEntity addressEntity) {
        String password = (String) passwordComponent.getValue();
        if (!Utils.validateString(password)) {

            return;
        }

        String confirmPassword = (String) confirmPasswordComponent.getValue();
        if (!Utils.validateString(confirmPassword)) {

            return;
        }

        if (!password.equals(confirmPassword)) {

            return;
        }

        DataPreferences.savePassword(password);

        HashMap<String, Object> hm = new HashMap<>();
        hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, addressEntity);
        hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, addressEntity);
        hm.put(KeyData.REVIEW_ORDER.PLACE_FOR, ValueData.REVIEW_ORDER.PLACE_FOR_NEW_CUSTOMER);
        SimiManager.getIntance().openReviewOrder(hm);

    }

    protected void editForNewCustomer(AddressEntity addressEntity) {
        HashMap<String, Object> hm = new HashMap<>();

        if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS)) {
            AddressEntity billingAddress = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS);


            hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, billingAddress);


            hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, addressEntity);


        } else if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS)) {
            AddressEntity shippingAddress = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS);
            Log.e("Address Detail ", "++++++ SHIPPINg NAME " + shippingAddress.getName());
            hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, shippingAddress);

            Log.e("Address Detail ", "++++++ BILLING NAME " + addressEntity.getName());
            hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, addressEntity);
        }
        hm.put(KeyData.REVIEW_ORDER.PLACE_FOR, ValueData.REVIEW_ORDER.PLACE_FOR_NEW_CUSTOMER);
        SimiManager.getIntance().openReviewOrder(hm);
    }

    protected void saveForGuest(AddressEntity addressEntity) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, addressEntity);
        hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, addressEntity);
        hm.put(KeyData.REVIEW_ORDER.PLACE_FOR, ValueData.REVIEW_ORDER.PLACE_FOR_GUEST);
        SimiManager.getIntance().openReviewOrder(hm);
    }

    protected void editForGuest(AddressEntity addressEntity) {
        HashMap<String, Object> hm = new HashMap<>();
        if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS)) {
            AddressEntity billingAddress = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS);
            hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, billingAddress);
            hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, addressEntity);
        } else if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS)) {
            AddressEntity shippingAddress = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS);
            hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, shippingAddress);
            hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, addressEntity);
        }
        hm.put(KeyData.REVIEW_ORDER.PLACE_FOR, ValueData.REVIEW_ORDER.PLACE_FOR_GUEST);
        SimiManager.getIntance().openReviewOrder(hm);

    }

    protected void saveForExitCustomer(AddressEntity addressEntity) {
        HashMap<String, Object> hm = new HashMap<>();
        if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS)) {
            AddressEntity shippingAddress = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS);
            hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, shippingAddress);
            hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, addressEntity);
        } else if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS)) {
            AddressEntity billingAddress = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS);
            hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, billingAddress);
            hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, addressEntity);
        } else {
            hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, addressEntity);
            hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, addressEntity);
        }

        hm.put(KeyData.REVIEW_ORDER.PLACE_FOR, ValueData.REVIEW_ORDER.PLACE_FOR_EXISTING_CUSTOMER);
        SimiManager.getIntance().openReviewOrder(hm);
    }

    protected JSONObject getJSONAddressForCheckout() throws JSONException {
        JSONObject jsonAddress = new JSONObject();

        if (null != mListRowComponent) {
            for (int i = 0; i < mListRowComponent.size(); i++) {
                SimiRowComponent rowComponent = mListRowComponent.get(i);
                SimiRowComponent.TYPE_ROW type = rowComponent.getType();
                if (type == SimiRowComponent.TYPE_ROW.TEXT) {
                    String key = rowComponent.getKey();
                    String value = (String) rowComponent.getValue();
                    if (rowComponent.isRequired() && null == value) {
                        return null;
                    }
                    jsonAddress.put(key, value);
                }
            }
        }

        if (null != mCountry) {
            String name = mCountry.getName();
            if (Utils.validateString(name)) {
                jsonAddress.put("country_name", name);
            }

            String code = mCountry.getCode();
            if (Utils.validateString(code)) {
                jsonAddress.put("country_code", code);
            }
        }

        if (null != mState) {
            String name = mState.getName();
            if (Utils.validateString(name)) {
                jsonAddress.put("state_name", name);
            }

            String code = mState.getCode();
            if (Utils.validateString(code)) {
                jsonAddress.put("state_code", code);
            }

            String id = mState.getID();
            if (Utils.validateString(id)) {
                jsonAddress.put("state_id", id);
            }
        }
        return jsonAddress;
    }

    @Override
    public void onResume() {
        ArrayList<View> listRow = new ArrayList<>();
        if (null != mListRowComponent) {
            for (int i = 0; i < mListRowComponent.size(); i++) {
                SimiRowComponent component = mListRowComponent.get(i);
                Object value = component.getValue();
                View view = component.createView();
                listRow.add(view);
            }
        }
        mDelegate.showRows(listRow);
        if (null != mCountry) {
            String countryName = mCountry.getName();
            mCountryComponent.setValue(countryName);
            mCountryComponent.updateView();
        }


        if (null != mState) {
            String stateName = mState.getName();
            mStateComponent.setValue(stateName);
            mStateComponent.updateView();
        } else {
            ArrayList<StateEntity> states = mCountry.getStateList();
            if (null != states && states.size() > 0) {
                mState = states.get(0);
            } else {
                mState = null;
                if (null != mStateComponent) {
                    mStateComponent.enableEdit();
                }
            }

        }

    }

    protected ArrayList<String> getListNameCountry() {
        ArrayList<String> listName = new ArrayList<>();

        if (null != mListCountry && mListCountry.size() > 0) {
            for (int i = 0; i < mListCountry.size(); i++) {
                String name = mListCountry.get(i).getName();
                listName.add(name);
            }
        }

        return listName;
    }

    protected void getCountryWithName(String name) {
        if (null != mListCountry && mListCountry.size() > 0) {
            for (int i = 0; i < mListCountry.size(); i++) {
                CountryEntity country = mListCountry.get(i);
                String currentName = country.getName();
                if (currentName.equals(name)) {
                    mCountry = country;

                    return;
                }
            }
        }
    }

    protected ArrayList<String> getListNameState(ArrayList<StateEntity> states) {
        ArrayList<String> listName = new ArrayList<>();
        if (null != states && states.size() > 0) {
            for (int i = 0; i < states.size(); i++) {
                StateEntity state = states.get(i);
                String name = state.getName();
                listName.add(name);
            }
        }
        return listName;
    }

    protected void getStateWithName(String name, ArrayList<StateEntity> states) {
        if (null != states && states.size() > 0) {
            for (int i = 0; i < states.size(); i++) {
                StateEntity state = states.get(i);
                String currentName = state.getName();
                if (name.equals(currentName)) {
                    mState = state;
                    return;
                }
            }
        }
    }


    public OnClickListener getSaveListener() {
        return mSaveListener;
    }


    public void setDelegate(AddressBookDetailDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    public void setData(HashMap<String, Object> data) {
        hmData = data;
    }

}

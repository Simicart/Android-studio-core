package com.simicart.core.customer.controller;

import android.text.InputType;
import android.view.View;

import com.simicart.core.base.component.GenderAdapter;
import com.simicart.core.base.component.SimiDOBRowComponent;
import com.simicart.core.base.component.SimiParentRowComponent;
import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.component.SimiSpinnerRowComponent;
import com.simicart.core.base.component.SimiTextRowComponent;
import com.simicart.core.base.component.callback.SpinnerRowCallBack;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.checkout.adapter.DateAdapter;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.customer.delegate.RegisterCustomerDelegate;
import com.simicart.core.customer.entity.AddressEntity;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.GenderConfig;
import com.simicart.core.customer.model.CustomerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class CustomerController extends SimiController {
    protected RegisterCustomerDelegate mDelegate;
    protected ArrayList<SimiRowComponent> mListComponent;
    protected View.OnClickListener mRegisterListener;
    protected int mOpenFor;
    protected String mCurrentMonth;
    protected String mYear;
    protected AddressEntity mAddressForEdit;
    protected SimiTextRowComponent passwordComponent;
    protected SimiTextRowComponent newPasswordComponent;
    protected SimiTextRowComponent confirmPasswordComponent;
    protected SimiDOBRowComponent dobRowComponent;
    protected HashMap<String, Object> mData;
    protected GenderConfig mGender;


    public void setDelegate(RegisterCustomerDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    @Override
    public void onStart() {

        parseParam();

        initListener();

        if (mOpenFor == ValueData.CUSTOMER_PAGE.OPEN_FOR_EDIT) {
            requestData();
        } else {
            initRows();
        }


    }

    protected void parseParam() {
        if (mData.containsKey(KeyData.CUSTOMER_PAGE.OPEN_FOR)) {
            mOpenFor = (int) mData.get(KeyData.CUSTOMER_PAGE.OPEN_FOR);
        }
    }

    protected void requestData() {

    }

    protected void initListener() {
        mRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister();
            }
        };
    }

    protected void initRows() {
        mListComponent = new ArrayList<>();

        // prefix
        initComponent("prefix", "Prefix", "prefix", "prefix", InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        // full name
        initComponentRequired("Full Name", "name", "name", InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        // suffix
        initComponent("suffix", "Suffix", "suffix", "suffix", InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        // date of birth
        initDOBComponent();

        // gender
        initGenderComponent();

        // tax/VAT number
        initComponent("taxvat", "Tax/VAT number", "taxvat", "taxvat", InputType.TYPE_CLASS_TEXT);

        // email
        initComponentRequired("Email", "email", "email", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        // pass
        passwordComponent = new SimiTextRowComponent();
        passwordComponent.setTitle("Password");
        passwordComponent.setRequired(true);
        passwordComponent.setKey("user_password");
        passwordComponent.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mListComponent.add(passwordComponent);

        if (mOpenFor == ValueData.CUSTOMER_PAGE.OPEN_FOR_EDIT) {
            // new password
            newPasswordComponent = new SimiTextRowComponent();
            newPasswordComponent.setTitle("Confirm Password");
            newPasswordComponent.setRequired(true);
            newPasswordComponent.setKey("user_password");
            newPasswordComponent.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mListComponent.add(newPasswordComponent);
        }


        // confirm pass
        confirmPasswordComponent = new SimiTextRowComponent();
        confirmPasswordComponent.setTitle("Confirm Password");
        confirmPasswordComponent.setRequired(true);
        confirmPasswordComponent.setKey("user_password");
        confirmPasswordComponent.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mListComponent.add(passwordComponent);

        showView();
    }

    protected void initComponent(String keyForConfig, String title, String keyForData, String keyForParam, int inputType) {
        String configValue = ConfigCustomerAddress.getInstance().getValueWithKey(keyForConfig);
        if (!ConfigCustomerAddress.getInstance().isHidden(configValue)) {
            SimiTextRowComponent component = new SimiTextRowComponent();
            component.setTitle(title);
            boolean isRequired = ConfigCustomerAddress.getInstance().isRequired(configValue);
            component.setRequired(isRequired);
            if (null != mAddressForEdit) {
                String prefix = mAddressForEdit.getData(keyForData);
                component.setValue(prefix);
            }
            component.setKey(keyForParam);
            component.setInputType(inputType);
            mListComponent.add(component);
        }
    }

    protected void initComponentRequired(String title, String keyForData, String keyForParam, int inputType) {
        SimiTextRowComponent component = new SimiTextRowComponent();
        component.setTitle(title);
        component.setRequired(true);
        if (null != mAddressForEdit && Utils.validateString(keyForData)) {
            String name = mAddressForEdit.getData(keyForData);
            component.setValue(name);
        }
        component.setKey(keyForParam);
        component.setInputType(inputType);
        mListComponent.add(component);
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
            mListComponent.add(genderComponent);
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
            mListComponent.add(dobRowComponent);

        }
    }

    protected void showView() {
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < mListComponent.size(); i++) {
            views.add(mListComponent.get(i).createView());
        }

        mDelegate.showView(views);

    }

    protected void onChangeProfile(){

    }


    protected void onRegister() {

        mModel = new CustomerModel();

        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {

            }
        });

        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {

            }
        });





    }

    protected boolean canRegister(){

        return  true;
    }




    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }

    public void setData(HashMap<String, Object> data) {
        mData = data;
    }

    public View.OnClickListener getRegisterListener() {
        return mRegisterListener;
    }

}

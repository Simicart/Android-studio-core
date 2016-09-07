package com.simicart.core.customer.controller;

import android.text.InputType;
import android.view.View;

import com.simicart.core.base.component.GenderAdapter;
import com.simicart.core.base.component.SimiDOBRowComponent;
import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.component.SimiSpinnerRowComponent;
import com.simicart.core.base.component.SimiTextRowComponent;
import com.simicart.core.base.component.callback.SpinnerRowCallBack;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.customer.delegate.CustomerDelegate;
import com.simicart.core.customer.entity.AddressEntity;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.GenderConfig;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerController extends SimiController {
    protected CustomerDelegate mDelegate;
    protected ArrayList<SimiRowComponent> mListComponent;
    protected View.OnClickListener mRegisterListener;
    protected int mOpenFor;
    protected AddressEntity mAddressForEdit;
    protected SimiTextRowComponent passwordComponent;
    protected SimiTextRowComponent newPasswordComponent;
    protected SimiTextRowComponent confirmPasswordComponent;
    protected SimiDOBRowComponent dobRowComponent;
    protected HashMap<String, Object> mData;
    protected GenderConfig mGender;
    protected String mEmail;
    protected String mName;


    public void setDelegate(CustomerDelegate mDelegate) {
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
        mDelegate.showLoading();

        mModel = new SimiModel();
        mModel.setUrlAction("connector/customer/get_profile");

        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissLoading();
                String msg = error.getMessage();
                if (Utils.validateString(msg)) {
                    SimiNotify.getInstance().showToast(msg);
                    SimiManager.getIntance().backPreviousFragment();
                }

            }
        });

        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                ArrayList<SimiEntity> entities = collection.getCollection();
                if (null != entities && entities.size() > 0) {
                    SimiEntity entity = entities.get(0);
                    mAddressForEdit = new AddressEntity();
                    mAddressForEdit.parse(entity.getJSONObject());
                    initRows();
                }
            }
        });

        mModel.request();


    }

    protected void initListener() {
        mRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOpenFor == ValueData.CUSTOMER_PAGE.OPEN_FOR_REGISTER) {
                    onRegister();
                } else {
                    onChangeProfile();
                }
            }
        };
    }

    protected void initRows() {
        mListComponent = new ArrayList<>();

        // prefix
        initComponent("prefix", "Prefix", "prefix", "prefix", (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME));

        // full name
        initComponentRequired("Full Name", "user_name", "name", (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME));

        // suffix
        initComponent("suffix", "Suffix", "suffix", "suffix", (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME));

        // date of birth
        initDOBComponent();

        // gender
        initGenderComponent();

        // tax/VAT number
        initComponent("taxvat", "Tax/VAT number", "taxvat", "taxvat", InputType.TYPE_CLASS_TEXT);

        // email
        initComponentRequired("Email", "user_email", "user_email", (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));

        // pass
        passwordComponent = new SimiTextRowComponent();
        passwordComponent.setTitle("Password");
        if (mOpenFor != ValueData.CUSTOMER_PAGE.OPEN_FOR_EDIT) {
            passwordComponent.setRequired(true);
        }
        passwordComponent.setKey("user_password");
        passwordComponent.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mListComponent.add(passwordComponent);

        if (mOpenFor == ValueData.CUSTOMER_PAGE.OPEN_FOR_EDIT) {
            // new password
            newPasswordComponent = new SimiTextRowComponent();
            newPasswordComponent.setTitle("New Password");
            newPasswordComponent.setRequired(false);
            newPasswordComponent.setKey("new_password");
            newPasswordComponent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mListComponent.add(newPasswordComponent);
        }


        // confirm pass
        confirmPasswordComponent = new SimiTextRowComponent();
        confirmPasswordComponent.setTitle("Confirm Password");
        if (mOpenFor != ValueData.CUSTOMER_PAGE.OPEN_FOR_EDIT) {
            confirmPasswordComponent.setRequired(true);
        }
        confirmPasswordComponent.setKey("user_password");
        confirmPasswordComponent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mListComponent.add(confirmPasswordComponent);

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

    protected void onChangeProfile() {
        final boolean isChangedPass = isChangedPassword();
        if (isChangedPass && !isValidatePasswordForEdit()) {
            return;
        }

        mModel = new SimiModel();
        mModel.setUrlAction("connector/customer/change_user");

        if (!addParamForModel()) {
            return;
        }

        if (isChangedPass) {
            String oldPass = (String) passwordComponent.getValue();
            String newPass = (String) newPasswordComponent.getValue();
            mModel.addBody("change_password", "1");
            mModel.addBody("old_password", oldPass);
            mModel.addBody("new_password", newPass);
            mModel.addBody("com_password", newPass);
        } else {
            mModel.addBody("change_password", "0");
        }

        mDelegate.showDialogLoading();
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissDialogLoading();
                String msg = "Can not save your profile";
                if (null != error) {
                    msg = error.getMessage();
                }
                if (Utils.validateString(msg)) {
                    SimiNotify.getInstance().showToast(msg);
                }
            }
        });

        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissDialogLoading();
                if (isChangedPass) {
                    DataPreferences.saveData(mName, mEmail, (String) newPasswordComponent.getValue());
                } else {
                    DataPreferences.saveData(mName, mEmail, (String) passwordComponent.getValue());
                }
                SimiManager.getIntance().onUpdateItemSignIn();
                String msg = "";
                SimiError error = mModel.getError();
                if (null != error) {
                    msg = error.getMessage();
                }
                if (!Utils.validateString(msg)) {
                    msg = "SUCCESS";
                }
                SimiNotify.getInstance().showToast(msg);
                SimiManager.getIntance().backToHomeFragment();

            }
        });

        mModel.request();

    }


    protected void onRegister() {

        if (!isValidatePasswordForRegister()) {
            return;
        }

        mModel = new SimiModel();
        mModel.setUrlAction("connector/customer/register");

        final String password = (String) passwordComponent.getValue();
        mModel.addBody("user_password", password);
        if (!addParamForModel()) {
            return;
        }


        mDelegate.showLoading();
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissLoading();
                String msg = error.getMessage();
                SimiNotify.getInstance().showToast(msg);
            }
        });

        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                SimiError error = mModel.getError();
                if (null != error) {
                    String msg = error.getMessage();
                    if (Utils.validateString(msg)) {
                        SimiNotify.getInstance().showToast(msg);
                    }
                    DataPreferences.saveData(mName, mEmail, password);
                    SimiManager.getIntance().backPreviousFragment();
                }
            }
        });


        mModel.request();


    }

    protected boolean addParamForModel() {


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

        for (int i = 0; i < mListComponent.size(); i++) {
            SimiRowComponent rowComponent = mListComponent.get(i);
            SimiRowComponent.TYPE_ROW type = rowComponent.getType();
            if (type == SimiRowComponent.TYPE_ROW.TEXT) {
                String key = rowComponent.getKey();
                String value = (String) rowComponent.getValue();
                if (rowComponent.isRequired() && !Utils.validateString(value)) {
                    return false;
                }
                if (key.equals("name")) {
                    mName = value;
                }
                if (key.equals("user_email")) {
                    mEmail = value;

                }

                mModel.addBody(key, value);
            }

        }

        return true;
    }

    protected boolean isValidatePasswordForRegister() {
        String password = (String) passwordComponent.getValue();
        if (!Utils.validateString(password)) {
            return false;
        }

        String confirmPass = (String) confirmPasswordComponent.getValue();
        if (!Utils.validateString(confirmPass)) {
            return false;
        }

        if (!password.equals(confirmPass)) {
            String msg = SimiTranslator.getInstance().translate("Password and Confirm password dont't match.");
            SimiNotify.getInstance().showToast(msg);
            return false;
        }

        return true;
    }

    protected boolean isValidatePasswordForEdit() {
        newPasswordComponent.showError(null);
        confirmPasswordComponent.showError(null);
        passwordComponent.showError(null);
        String newPassword = (String) newPasswordComponent.getValue();
        if (Utils.validateString(newPassword)) {
            String password = (String) passwordComponent.getValue();
            if (!Utils.validateString(password)) {
                String warming = "The current password is required.";
                passwordComponent.showError(warming);
                return false;
            }

            String currentPassword = DataPreferences.getPassword();
            if (!password.equals(currentPassword)) {
                String warming = SimiTranslator.getInstance().translate("The current password is not corrent.");
                passwordComponent.showError(warming);
                return false;
            }

            String confirmPassword = (String) confirmPasswordComponent.getValue();
            if (!Utils.validateString(confirmPassword)) {
                String warming = "The confirm password is required";
                confirmPasswordComponent.showError(warming);
                return false;
            }

            if (!newPassword.equals(confirmPassword)) {
                String warming = SimiTranslator.getInstance().translate("New password and confirm password don't match.");
                newPasswordComponent.showError(warming);
                return false;
            }
        } else {
            String confirmPassword = (String) confirmPasswordComponent.getValue();
            if (Utils.validateString(confirmPassword)) {
                String warming = "The new password is required";
                newPasswordComponent.showError(warming);
                return false;
            } else {
                String password = (String) passwordComponent.getValue();
                if (Utils.validateString(password)) {
                    String warming = "The new password is required";
                    newPasswordComponent.showError(warming);
                    return false;
                }
            }
        }


        return true;
    }

    protected boolean isChangedPassword() {

        String password = (String) passwordComponent.getValue();
        if (Utils.validateString(password)) {
            return true;
        }

        String newPassword = (String) newPasswordComponent.getValue();
        if (Utils.validateString(newPassword)) {
            return true;
        }

        String confirmPassword = (String) confirmPasswordComponent.getValue();
        if (Utils.validateString(confirmPassword)) {
            return true;
        }


        return false;
    }


    @Override
    public void onResume() {
        if (mOpenFor == ValueData.CUSTOMER_PAGE.OPEN_FOR_EDIT) {
            mDelegate.updateView(mModel.getCollection());
        }
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < mListComponent.size(); i++) {
            views.add(mListComponent.get(i).createView());
        }

        mDelegate.showView(views);
    }

    public void setData(HashMap<String, Object> data) {
        mData = data;
    }

    public View.OnClickListener getRegisterListener() {
        return mRegisterListener;
    }

}

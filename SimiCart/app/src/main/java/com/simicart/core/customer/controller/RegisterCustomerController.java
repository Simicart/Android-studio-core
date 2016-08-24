package com.simicart.core.customer.controller;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.delegate.RegisterCustomerDelegate;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.RegisterCustomer;
import com.simicart.core.customer.fragment.SignInFragment;
import com.simicart.core.customer.model.RegisterCustomerModel;

@SuppressLint("DefaultLocale")
public class RegisterCustomerController extends SimiController {
    protected RegisterCustomerDelegate mDelegate;
    protected OnClickListener mRegister, onClickRelative;

    public OnClickListener getOnclickRegister() {
        return mRegister;
    }

    public OnClickListener getOnClickRelative() {
        return onClickRelative;
    }

    public void setDelegate(RegisterCustomerDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    @Override
    public void onStart() {
        mRegister = new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Utils.hideKeyboard(v);
//				RegisterCustomer register = mDelegate.getRegisterCustomer();
//				if (isCompleteRequired(register)) {
//					if (android.util.Patterns.EMAIL_ADDRESS.matcher(
//							register.getEmail()).matches()) {
//						if (register.getPass()
//								.equals(register.getConfirmPass())) {
//							onRegisterCustomer();
//						} else {
//							SimiManager
//									.getIntance()
//									.showNotify(
//											null,
//											Config.getInstance()
//													.getText(
//															"Password and Confirm password dont't match."),
//											Config.getInstance().getText("OK"));
//						}
//					} else {
//						SimiManager.getIntance().showNotify(
//								null,
//								Config.getInstance().getText(
//										"Invalid email address"),
//								Config.getInstance().getText("OK"));
//					}
//				} else {
//					SimiManager.getIntance().showNotify(
//							null,
//							Config.getInstance().getText(
//									"Please select all (*) fields"),
//							Config.getInstance().getText("OK"));
//				}
            }
        };
        onClickRelative = new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDelegate.getSpinnerSex().performClick();
            }
        };

    }

    private boolean isCompleteRequired(RegisterCustomer register) {
        ConfigCustomerAddress _configCustomer = DataLocal.ConfigCustomerProfile;
        if (register.getName().equals("") || register.getEmail().equals("")) {
            return false;
        } else {
            if (_configCustomer.getPrefix().toLowerCase().equals("req")
                    && register.getPrefix().equals("")) {
                return false;
            }
            if (_configCustomer.getSuffix().toLowerCase().equals("req")
                    && register.getSuffix().equals("")) {
                return false;
            }
            if (_configCustomer.getGender().toLowerCase().equals("req")
                    && register.getGender().equals("")) {
                return false;
            }
            if (_configCustomer.getTaxvat().toLowerCase().equals("req")
                    && register.getTaxVat().equals("")) {
                return false;
            }
            if (_configCustomer.getDob().toLowerCase().equals("req")
                    && register.getDay().equals("")) {
                return false;
            }

            String pass = register.getPass();
            if (null == pass || pass.equals("")) {
                return false;
            }
            String confirmPass = register.getConfirmPass();
            if (null == confirmPass || confirmPass.equals("")) {
                return false;
            }
            return true;
        }
    }

    protected void onRegisterCustomer() {
        mDelegate.showLoading();
        mModel = new RegisterCustomerModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();

                mDelegate.updateView(mModel.getCollection());
//					SimiManager.getIntance().showNotify(
//							Config.getInstance().getText("SUCCESS"), message,
//							Config.getInstance().getText("OK"));
                replaceFragment();
            }
        });

        RegisterCustomer register = mDelegate.getRegisterCustomer();
        if (!register.getEmail().equals("")) {
            mModel.addBody(Constants.USER_EMAIL, register.getEmail());
        }
        if (!register.getPass().equals("")) {
            mModel.addBody(Constants.USER_PASSWORD, register.getPass());
        }
        if (!register.getName().equals("")) {
            mModel.addBody(Constants.USER_NAME, register.getName());
        }
        if (!register.getPrefix().equals("")) {
            mModel.addBody("prefix", register.getPrefix());
        }
        if (!register.getSuffix().equals("")) {
            mModel.addBody("suffix", register.getSuffix());
        }
        if (!register.getGender().equals("")) {
            mModel.addBody("gender",
                    "" + Utils.getValueGender(register.getGender()));
        }
        if (!register.getTaxVat().equals("")) {
            mModel.addBody("taxvat", register.getTaxVat());
        }
        if (register.getDay() != null && !register.getDay().equals("")) {
            mModel.addBody("day", register.getDay());
            mModel.addBody("month", register.getMonth());
            mModel.addBody("year", register.getYear());
        }
        mModel.request();
    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }

    private void replaceFragment() {
        RegisterCustomer register = mDelegate.getRegisterCustomer();
        String email = register.getEmail();
        String pass = register.getPass();
//        SignInFragment fragment = SignInFragment.newInstance(email, pass, false);
//		String email = register.getEmail();
//		fragment.setEmail(email);
//		String pass = register.getPass();
//		fragment.setPassword(pass);
//        DataPreferences.saveData(email, pass);
//        DataPreferences.saveEmailPassRemember(email, pass);
//        SimiManager.getIntance().backPreviousFragment();
//        SimiManager.getIntance().replacePopupFragment(fragment);
        //fadf
    }
}

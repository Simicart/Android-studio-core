package com.simicart.core.customer.controller;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.model.CartModel;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.SignInDelegate;
import com.simicart.core.customer.entity.ProfileEntity;
import com.simicart.core.customer.fragment.AddressBookFragment;
import com.simicart.core.customer.fragment.ForgotPasswordFragment;
import com.simicart.core.customer.fragment.RegisterCustomerFragment;
import com.simicart.core.customer.model.SignInModel;

import java.util.ArrayList;
import java.util.HashMap;

public class SignInController extends SimiController {

    protected SignInDelegate mDelegate;
    protected OnClickListener mSignInClicker;
    protected OnClickListener mForgotPassClicker;
    protected OnTouchListener mCreateAccClicker;
    protected OnCheckedChangeListener mOnCheckBox;
    protected boolean isCheckout = false;
    protected boolean isRememberEmailPass = false;

    public SignInDelegate getDelegate() {
        return mDelegate;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onStart() {

        mSignInClicker = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(v);
                ProfileEntity signInProfile = mDelegate.getProfileSignIn();
                if (signInProfile != null) {
                    onSignIn(signInProfile);
                }
            }
        };

        mCreateAccClicker = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Utils.hideKeyboard(v);
                        v.setBackgroundColor(0xCCCACACA);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(0xCCFFFFFF);
                        onCreateAccount();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        v.setBackgroundColor(0xCCFFFFFF);
                        break;

                    default:
                        break;
                }
                return true;
            }
        };

        mForgotPassClicker = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(v);
                onForgotPasswrod();
            }
        };

        mOnCheckBox = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                isRememberEmailPass = isChecked;
            }
        };
    }

    protected void onSignIn(ProfileEntity entity) {

        final String email = entity.getEmail();
        final String password = entity.getCurrentPass();

        mDelegate.showLoading();
        mModel = new SignInModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {

                SimiManager.getIntance().getRequestQueue().clearCacheL1();

                saveDataSignIn(email, password);

                String cartQty = ((SignInModel) mModel).getCartQty();
                if (null != cartQty && !cartQty.equals("0")) {
                    SimiManager.getIntance().onUpdateCartQty(cartQty);
                }

                onSignInSuccess();

                // update wishlist_items_qty
//				EventController event = new EventController();
//				event.dispatchEvent(
//						"com.simicart.core.customer.controller.SignInController",
//						mModel.getJSON().toString());

            }
        });
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissLoading();
                SimiNotify.getInstance().showNotify(error.getMessage());
            }
        });
        mModel.addBody(Constants.USER_EMAIL, email);
        mModel.addBody(Constants.USER_PASSWORD, password);
        mModel.request();

    }

    private void saveDataSignIn(String email, String password) {

        DataLocal.isNewSignIn = true;
        DataPreferences.saveTypeSignIn(Constants.NORMAL_SIGN_IN);

        String name = ((SignInModel) mModel).getName();
        DataPreferences.saveData(name, email, password);

        if (isRememberEmailPass == true) {
            DataPreferences.saveCheckRemember(true);
            DataPreferences.saveEmailPassRemember(email, password);
        } else {
            DataPreferences.saveCheckRemember(false);
            DataPreferences.saveEmailPassRemember("", "");
        }

        DataPreferences.saveSignInState(true);

    }

    private void onSignInSuccess() {

        showToastSignIn();

        if (isCheckout == true) {
            processCheckout();
        } else {
            if (DataLocal.isTablet) {
                SimiManager.getIntance().clearAllChidFragment();
                SimiManager.getIntance().removeDialog();
            } else {
                SimiManager.getIntance().backToHomeFragment();
            }
        }
    }

    private void processCheckout() {
        mModel = new CartModel();
        mDelegate.showLoading();

        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                int carQty = ((CartModel) mModel).getQty();
                SimiManager.getIntance().onUpdateCartQty(
                        String.valueOf(carQty));

                ArrayList<SimiEntity> entity = mModel
                        .getCollection().getCollection();
                if (null != entity && entity.size() > 0) {
                    ArrayList<Cart> carts = new ArrayList<Cart>();
                    DataLocal.listCarts.clear();
                    for (int i = 0; i < entity.size(); i++) {
                        SimiEntity simiEntity = entity
                                .get(i);
                        Cart cart = (Cart) simiEntity;
                        carts.add(cart);
                        DataLocal.listCarts.add(cart);
                    }
                }

                HashMap<String, Object> hmData = new HashMap<>();
                hmData.put(KeyData.ADDRESS_BOOK.OPEN_FOR, ValueData.ADDRESS_BOOK.OPEN_FOR_CHECKOUT);
                SimiData data = new SimiData(hmData);
                AddressBookFragment fragment = AddressBookFragment.newInstance(data);
                if (DataLocal.isTablet) {
                    SimiManager.getIntance().replacePopupFragment(fragment);
                } else {
                    SimiManager.getIntance().replaceFragment(fragment);
                }

            }
        });
        mModel.request();
    }

    private void showToastSignIn() {
        LayoutInflater inflater = SimiManager.getIntance().getCurrentActivity()
                .getLayoutInflater();
        View layout_toast = inflater
                .inflate(
                        Rconfig.getInstance().layout(
                                "core_custom_toast_productlist"),
                        (ViewGroup) SimiManager
                                .getIntance()
                                .getCurrentActivity()
                                .findViewById(
                                        Rconfig.getInstance().id(
                                                "custom_toast_layout")));
        TextView txt_toast = (TextView) layout_toast.findViewById(Rconfig
                .getInstance().id("txt_custom_toast"));
        Toast toast = new Toast(SimiManager.getIntance().getCurrentActivity());
        txt_toast.setText(String.format(SimiTranslator.getInstance().translate("Welcome %s! Start shopping now"), DataPreferences.getUsername()));
        toast.setView(layout_toast);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 400);
        toast.show();
    }

    protected void onCreateAccount() {
        RegisterCustomerFragment fragment = RegisterCustomerFragment
                .newInstance();
        SimiManager.getIntance().replacePopupFragment(fragment);
    }

    protected void onForgotPasswrod() {
        ForgotPasswordFragment fragment = ForgotPasswordFragment.newInstance();
        SimiManager.getIntance().replacePopupFragment(fragment);

    }

    @Override
    public void onResume() {
        mDelegate.updateView(null);
    }

    public void setDelegate(SignInDelegate delegate) {
        mDelegate = delegate;
    }

    public OnClickListener getSignInClicker() {
        return mSignInClicker;
    }

    public OnClickListener getForgotPassClicker() {
        return mForgotPassClicker;
    }

    public OnTouchListener getCreateAccClicker() {
        return mCreateAccClicker;
    }

    public void setCheckout(boolean isCheckout) {
        this.isCheckout = isCheckout;
    }

    public OnCheckedChangeListener getOnCheckBox() {
        return mOnCheckBox;
    }

}

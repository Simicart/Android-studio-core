package com.simicart.plugins.facebookconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.event.fragment.SimiEventFragmentEntity;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.SimiModel;
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
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.SignInDelegate;
import com.simicart.core.customer.fragment.AddressBookFragment;
import com.simicart.core.customer.fragment.SignInFragment;
import com.simicart.core.customer.model.SignInModel;
import com.simicart.plugins.facebookconnect.model.FacebookLoginModel;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Martial on 8/29/2016.
 */
public class FacebookConnect {

    protected Context mContext;
    protected SimiModel mModel;
    protected CallbackManager callbackManager;
    protected SignInDelegate mDelegate;
    protected SignInFragment mSignInFragment;

    public FacebookConnect() {

        mContext = SimiManager.getIntance().getCurrentActivity();

        BroadcastReceiver blockReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                View view = (View) data.getData().get(KeyData.SIMI_BLOCK.VIEW);
                mDelegate = (SignInDelegate) data.getData().get(KeyData.SIMI_BLOCK.BLOCK);
                addButtonFacebookLogin(view);
            }
        };
        SimiEvent.registerEvent(KeyEvent.FACEBOOK_EVENT.FACEBOOK_LOGIN_BLOCK, blockReceiver);

        BroadcastReceiver fragmentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                String method = (String) data.getData().get(KeyData.SIMI_FRAGMENT.METHOD);
                if (method != null) {
                    if (method.equals("createFragment")) {
                        SimiEventFragmentEntity entity = (SimiEventFragmentEntity) data.getData().get(KeyData.SIMI_FRAGMENT.FRAGMENT);
                        mSignInFragment = (SignInFragment) entity.getFragment();
                    } else if (method.equals("onActivityResult")) {
                        int requestCode = (int) data.getData().get(KeyData.FACEBOOK.REQUEST_CODE);
                        int resultCode = (int) data.getData().get(KeyData.FACEBOOK.RESULT_CODE);
                        Intent intentData = (Intent) data.getData().get(KeyData.FACEBOOK.INTENT);
                        onActivityResult(requestCode, resultCode, intentData);
                    }
                }
            }
        };
        SimiEvent.registerEvent(KeyEvent.FACEBOOK_EVENT.FACEBOOK_LOGIN_FRAGMENT, fragmentReceiver);

        BroadcastReceiver autoSignInReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String email = DataPreferences.getEmail();
                String name = DataPreferences.getUsername();
                requestFaceBookSignIn(email, name, false);
            }
        };
        SimiEvent.registerEvent(KeyEvent.FACEBOOK_EVENT.FACEBOOK_LOGIN_AUTO, autoSignInReceiver);

    }

    protected void addButtonFacebookLogin(View rootView) {

        FacebookSdk.sdkInitialize(mContext);
        callbackManager = CallbackManager.Factory.create();

        LinearLayout llSignIn = (LinearLayout) rootView
                .findViewById(Rconfig.getInstance().id("ll_signin"));

        LoginButton btLogin = new LoginButton(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 50);
        int dp = Utils.getValueDp(5);
        params.setMargins(dp, dp, dp, dp);
        btLogin.setLayoutParams(params);
        llSignIn.addView(btLogin);

        // check session
        LoginManager.getInstance().logOut();
        btLogin.setFragment(mSignInFragment);
        btLogin.setReadPermissions(Arrays
                .asList("email,user_photos,user_birthday"));
        btLogin.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult result) {
                        onLoginSuccess(result);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("Login Facebook Error", error.getMessage());
                    }

                    @Override
                    public void onCancel() {
                        Log.e("Login Facebook Cancel:", "Cancel");
                    }
                });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void onLoginSuccess(LoginResult result) {
        GraphRequest request = GraphRequest.newMeRequest(
                result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        processResultLogin(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields",
                "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    protected void processResultLogin(JSONObject json) {
        String name = "";
        String email = "";
        String id = "";
        try {
            if (json.has("id")) {
                id = json.getString("id");
            }
            if (json.has("name")) {
                name = json.getString("name");
            }
            if (json.has("email")) {
                email = json
                        .getString("email");
            } else {
                email = id + "@facebook.com";
            }
            if (email.length() > 0
                    && mSignInFragment != null) {

                boolean isCheckout = mSignInFragment.isCheckout();
                requestFaceBookSignIn(email, name, isCheckout);
            }
        } catch (Exception e) {
            Log.e("FacebookLogin:", "Get Information");
        }
    }

    protected void requestFaceBookSignIn(final String facebook_email, final String facebook_name,
                                       final boolean checkOut) {

        if(mDelegate != null) {
            mDelegate.showLoading();
        }
        //DataLocal.saveData(facebook_name, facebook_email, "");

        mModel = new FacebookLoginModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                if(mDelegate != null) {
                    mDelegate.dismissLoading();
                }
                if (collection != null && collection.getCollection().size() > 0) {

                    saveDataSignIn(facebook_name, facebook_email);

                    String cartQty = ((SignInModel) mModel).getCartQty();
                    if (null != cartQty && !cartQty.equals("0")) {
                        SimiManager.getIntance().onUpdateCartQty(cartQty);
                    }

                    onSignInSuccess(checkOut);

                }
            }
        });
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                if(mDelegate != null) {
                    mDelegate.dismissLoading();
                }
                if (error != null) {
                    SimiNotify.getInstance().showNotify(error.getMessage());
                }
            }
        });
        mModel.addBody("email", "" + facebook_email + "");
        mModel.addBody("first_name", "" + facebook_name + "");
        mModel.addBody("last_name", "");
        mModel.request();
    }

    protected void saveDataSignIn(String name, String email) {
        DataLocal.isNewSignIn = true;
        DataPreferences.saveTypeSignIn(Constants.FACEBOOK_SIGN_IN);
        DataPreferences.saveData(name, email, getMD5(email));
        DataPreferences.saveSignInState(true);
    }

    protected String getMD5(String email) {
        String input = "simicart" + email;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            // throw new RuntimeException(e);
            return "";
        }
    }

    protected void onSignInSuccess(boolean isCheckout) {

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

    protected void showToastSignIn() {
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

    protected void processCheckout() {
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

}

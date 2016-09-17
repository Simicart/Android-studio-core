package com.simicart.core.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Constants;
import com.simicart.core.setting.entity.CurrencyEntity;
import com.simicart.core.store.entity.Stores;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Glenn on 8/15/2016.
 */
public class DataPreferences {

    public static final String NAME_REFERENCE = "simicart";
    private static final String RESTART_COUNT = "RestartCount";
    private static final String CATEGORY_ID = "CategoryKey";
    private static final String CATEGORY_NAME = "CategoryNameKey";
    private static final String EMAIL_KEY = "EmailKey";
    private static final String USER_NAME_KEY = "UsernameKey";
    private static final String PASS_WORD_KEY = "PasswordKey";
    private static final String EMAIL_KEY_REMEMBER = "EmailKeyRemember";
    private static final String PASS_WORK_REMEMBER = "PasswordRemember";
    private static final String SIGNIN_KEY = "SignInKey";
    private static final String NOTIFICATION_KEY = "NotificationKey";
    private static final String STORE_KEY = "StoreKey";
    private static final String CURRENCY_KEY = "CurrencyKey";
    private static final String TYPE_SIGNIN = "TypeSignIn";
    private static final String CHECK_REMEMBER_PASSWORD = "check_remember_password";
    private static final String EMAIL_CARD_CREDIT_CARD = "EmailCardCreditCard";
    private static final String SIMI_CREDIT_CARD = "SimiCreditCard";
    public static Context mContext;
    public static SharedPreferences mSharedPre;
    public static boolean enNotification = true;
    public static ArrayList<CurrencyEntity> listCurrency;
    public static ArrayList<Stores> listStores;

    public static void init(Context context) {
        mContext = context;
        mSharedPre = mContext.getSharedPreferences(NAME_REFERENCE,
                Context.MODE_PRIVATE);
        listCurrency = new ArrayList<CurrencyEntity>();
        listStores = new ArrayList<>();
    }

    public static String getCatID() {
        String cat_id = "-1";
        if (mSharedPre != null) {
            cat_id = mSharedPre.getString(CATEGORY_ID, "-1");
        }
        return cat_id;
    }

    public static String getCatName() {
        String cat_name = "";
        if (mSharedPre != null) {
            cat_name = mSharedPre.getString(CATEGORY_NAME, "");
        }
        return cat_name;
    }

    public static String getEmail() {
        String email = "";
        if (mSharedPre != null) {
            email = mSharedPre.getString(EMAIL_KEY, "");
        }
        return email;
    }

    public static String getPassword() {
        String password = "";
        if (mSharedPre != null) {
            password = mSharedPre.getString(PASS_WORD_KEY, "");
        }
        return password;
    }

    public static void savePassword(String password) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString(PASS_WORD_KEY, password);
        editor.commit();
    }

    public static String getUsername() {
        String username = "";
        if (mSharedPre != null) {
            username = mSharedPre.getString(USER_NAME_KEY, "");
        }
        return username;
    }

    public static Boolean getCheckRemember() {
        boolean check_save = false;
        if (mSharedPre != null) {
            check_save = mSharedPre.getBoolean(CHECK_REMEMBER_PASSWORD, false);
        }
        return check_save;
    }

    public static String getStoreID() {
        String id = "";
        if (listStores != null && listStores.size() > 0) {
            id = listStores.get(0).getStoreID();
        }
        if (mSharedPre != null) {
            id = mSharedPre.getString(STORE_KEY, id);
        }
        return id;
    }

    public static String getCurrencyID() {
        String id = null;
        if (listCurrency != null && listCurrency.size() > 0) {
            id = listCurrency.get(0).getValue();
        }
        if (null != mSharedPre) {
            id = mSharedPre.getString(CURRENCY_KEY, id);
        }

        return id;
    }

    public static boolean isSignInComplete() {
        boolean isSignIn = false;
        if (mSharedPre != null) {
            isSignIn = mSharedPre.getBoolean(SIGNIN_KEY, false);
        }
        return isSignIn;
    }

    public static boolean enableNotification(Context context) {
        init(context);
        if (mSharedPre != null) {
            enNotification = mSharedPre.getBoolean(NOTIFICATION_KEY, true);
        }
        return enNotification;
    }

    public static boolean enableNotification() {
        if (mSharedPre != null) {
            enNotification = mSharedPre.getBoolean(NOTIFICATION_KEY, true);
        }
        return enNotification;
    }

    public static void saveSignInState(boolean state) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putBoolean(SIGNIN_KEY, state);
        editor.commit();
        SimiManager.getIntance().onUpdateItemSignIn();
    }

    public static void saveCateID(String cate_id, String cat_name) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString(CATEGORY_ID, cate_id);
        editor.putString(CATEGORY_NAME, cat_name);
        editor.commit();
    }

    public static void saveData(String email, String pass) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString(EMAIL_KEY, email);
        editor.putString(PASS_WORD_KEY, pass);
        editor.commit();
        saveEmailCreditCart(email);
    }

    public static void saveCheckRemember(boolean check) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putBoolean(CHECK_REMEMBER_PASSWORD, check);
        editor.commit();
    }

    public static void saveData(String name, String email, String pass) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        if (Utils.validateString(email)) {
            editor.putString(EMAIL_KEY, email);
        }
        if (Utils.validateString(pass)) {
            editor.putString(PASS_WORD_KEY, pass);
        }
        if (Utils.validateString(name)) {
            editor.putString(USER_NAME_KEY, name);
        }
        saveEmailCreditCart(email);
        editor.commit();
    }

    public static void saveEmailPassRemember(String email, String password) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString(EMAIL_KEY_REMEMBER, email);
        editor.putString(PASS_WORK_REMEMBER, password);
        editor.commit();
    }

    public static void saveTypeSignIn(String type) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString(TYPE_SIGNIN, type);
        editor.commit();
    }

    public static String getTypeSignIn() {
        return mSharedPre.getString(TYPE_SIGNIN, Constants.NORMAL_SIGN_IN);
    }

    public static void saveCurrencyID(String id) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString(CURRENCY_KEY, id);
        editor.commit();
    }

    public static void saveStoreID(String id) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString(STORE_KEY, id);
        editor.commit();
    }

    public static void saveNotificationSet(boolean enNotification) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putBoolean(NOTIFICATION_KEY, enNotification);
        editor.commit();

    }

    public static String getEmailRemember() {
        String email = mSharedPre.getString(EMAIL_KEY_REMEMBER, null);
        return email;
    }

    public static String getPasswordRemember() {
        String password = mSharedPre.getString(PASS_WORK_REMEMBER, null);
        return password;
    }

    public static void saveEmailCreditCart(String email) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString(EMAIL_CARD_CREDIT_CARD, email);
        editor.commit();
    }

    public static String getEmailCreditCart() {
        String email = "";
        if (isSignInComplete()) {
            email = mSharedPre.getString(EMAIL_CARD_CREDIT_CARD, "");
        }
        return email;
    }

    public static void saveCreditCard(JSONObject jsonSave) {
        String emailCreditCard = getEmailCreditCart();
        String key = SIMI_CREDIT_CARD + emailCreditCard;
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString(key, jsonSave.toString());
        editor.commit();
    }


    public static void clearEmailPassowrd() {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.remove(EMAIL_KEY);
        editor.remove(PASS_WORD_KEY);
        editor.remove(TYPE_SIGNIN);
        editor.remove(USER_NAME_KEY);
        editor.remove(CATEGORY_ID);
        editor.remove(CATEGORY_NAME);
        editor.commit();
    }


    public static int getRestartCount() {
        int count = 0;
        if (mSharedPre != null) {
            count = mSharedPre.getInt(RESTART_COUNT, 0);
        }
        return count;
    }

    public static void saveRestartCount(int count) {
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putInt(RESTART_COUNT, count);
        editor.commit();
    }

}

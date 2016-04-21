package com.simicart.core.splashscreen.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.GenderConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreViewModel extends SimiModel {

    ConfigCustomerAddress configCustomerAddress = new ConfigCustomerAddress();

    @Override
    protected void paserData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            JSONObject jsonResult = list.getJSONObject(0);
            parseJSONStoreView(jsonResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJSONStoreView(JSONObject jsonResult) throws JSONException {
        // hien thi list hoac grid product
        String check = "0";
        if (jsonResult.has("view_products_default")) {
            check = jsonResult.getString("view_products_default");
        }
        Config.getInstance().setDefaultList(check);

        // store configuration
        JSONObject js_store_config = jsonResult.getJSONObject(
                "store_config");
        setStoreConfig(js_store_config);

        //cusstomer address config
        setConfigCustomerAddress(jsonResult);
    }

    private void setStoreConfig(JSONObject js_store_config) throws JSONException {
        Config.getInstance().setCurrency_code(
                js_store_config.getString("currency_code"));
        DataLocal.saveCurrencyID(Config.getInstance().getCurrencyCode());
        Config.getInstance().setStore_id(js_store_config.getInt("store_id"));
        Config.getInstance().setStore_name(
                js_store_config.getString("store_name"));
        Config.getInstance().setLocale_identifier(
                js_store_config.getString("locale_identifier"));

        Config.getInstance().setCurrency_symbol(
                js_store_config.getString("currency_symbol"));
        if (js_store_config.has("use_store")) {
            Config.getInstance().setUse_store(
                    js_store_config.getString("use_store"));
        } else {
            Config.getInstance().setUse_store("0");
        }
        if (js_store_config.has("is_rtl")) {
            if (js_store_config.getString("is_rtl").equals("1"))
                DataLocal.isLanguageRTL = true;
        }

        if (js_store_config.has("is_show_zero_price")) {
            if (js_store_config.getString("is_show_zero_price").equals("0")) {
                Config.getInstance().setIs_show_zero_price(false);
            } else {
                Config.getInstance().setIs_show_zero_price(true);
            }
        }
        if (js_store_config.has("is_show_link_all_product")) {
            if (js_store_config.getString("is_show_link_all_product").equals(
                    "0")) {
                Config.getInstance().setShow_link_all_product(false);
            } else {
                Config.getInstance().setShow_link_all_product(true);
            }
        }
        if (js_store_config.has("currency_position")) {
            Config.getInstance().setCurrencyPosition(
                    js_store_config.getString("currency_position"));
        }
        if (js_store_config.has("is_reload_payment_method")) {
            if (js_store_config.getString("is_reload_payment_method").equals(
                    "0")) {
                Config.getInstance().setReload_payment_method(false);
            } else {
                Config.getInstance().setReload_payment_method(true);
            }
        }
    }

    public ConfigCustomerAddress getConfigCustomerAddress() {
        return configCustomerAddress;
    }

    private void setConfigCustomerAddress(JSONObject jsonResult) throws JSONException {
        // Customer address configuration
        JSONObject js_customer_address_config = jsonResult
                .getJSONObject("customer_address_config");
        ConfigCustomerAddress configCustomerAddress = new ConfigCustomerAddress();
        configCustomerAddress.setJSONObject(js_customer_address_config);
        configCustomerAddress.setPrefix(js_customer_address_config
                .getString("prefix_show"));
        configCustomerAddress.setSuffix(js_customer_address_config
                .getString("suffix_show"));
        configCustomerAddress.setDob(js_customer_address_config
                .getString("dob_show"));
        configCustomerAddress.setGender(js_customer_address_config
                .getString("gender_show"));
        configCustomerAddress
                .setGenderConfigs(getGenderConfigs(js_customer_address_config
                        .getJSONArray("gender_value")));
        if (js_customer_address_config.has("taxvat_show")) {
            try {
                String tav_show = js_customer_address_config
                        .getString("taxvat_show");
                if (Utils.validateString(tav_show)) {
                    configCustomerAddress.setTaxvat(tav_show);
                }
            } catch (Exception e) {
                configCustomerAddress
                        .setTaxvat(ConfigCustomerAddress.OPTION_HIDE);
            }
        }

        // checkout configuration
        JSONObject js_checkout_config = jsonResult
                .getJSONObject("checkout_config");
        Config.getInstance().setGuest_checkout(
                js_checkout_config.getInt("enable_guest_checkout"));
        Config.getInstance().setEnable_agreements(
                js_checkout_config.getInt("enable_agreements"));
        if (jsonResult.has("android_sender")) {
            Config.getInstance().setSenderId(
                    jsonResult.getString("android_sender"));
        }
        if (js_checkout_config.has("taxvat_show")) {
            try {
                if ((js_checkout_config.getString("taxvat_show").equals("1"))) {
                    configCustomerAddress
                            .setVat_id(ConfigCustomerAddress.OPTION_REQUIRE);
                }
            } catch (Exception e) {
            }
        }
    }

    private ArrayList<GenderConfig> getGenderConfigs(JSONArray jsonArray)
            throws JSONException {
        int n = jsonArray.length();
        ArrayList<GenderConfig> genderList = new ArrayList<GenderConfig>();
        for (int i = 0; i < n; i++) {
            GenderConfig gender = new GenderConfig();
            JSONObject js_item = jsonArray.getJSONObject(i);
            gender.setLabel(js_item.getString("label"));
            gender.setValue(js_item.getString("value"));
            genderList.add(gender);
        }
        return genderList;
    }

    @Override
    protected void setUrlAction() {
        url_action = Constants.GET_STORE_VIEW;
    }


}

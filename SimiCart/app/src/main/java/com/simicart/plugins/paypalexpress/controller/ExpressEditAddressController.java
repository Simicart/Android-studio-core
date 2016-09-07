package com.simicart.plugins.paypalexpress.controller;

import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.Utils;
import com.simicart.core.customer.controller.AddressBookDetailController;
import com.simicart.core.customer.entity.AddressEntity;
import com.simicart.plugins.paypalexpress.fragment.ExpressAddressFragment;
import com.simicart.plugins.paypalexpress.fragment.ExpressEditAddressFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by frank on 9/7/16.
 */
public class ExpressEditAddressController extends AddressBookDetailController {

    @Override
    protected void saveForCustomer() {

        try {
            JSONObject json = getAddress();

            if (null != json) {
                mAddressForEdit = new AddressEntity();
                mAddressForEdit.parse(json);
                if (hmData.containsKey("type_edit")) {
                    int typeEdit = (Integer) hmData.get("type_edit");


                    HashMap<String, Object> hm = new HashMap<>();
                    if (typeEdit == ExpressEditAddressFragment.EDIT_BILLING_ADDRESS) {
                        if (hmData.containsKey("shipping_address")) {
                            AddressEntity shippingAddress = (AddressEntity) hmData.get("shipping_address");
                            hm.put("shipping_address", shippingAddress);
                        }

                        hm.put("billing_address", mAddressForEdit);
                    } else {

                        if (hmData.containsKey("billing_address")) {
                            AddressEntity billingAddress = (AddressEntity) hmData.get("billing_address");
                            hm.put("billing_address", billingAddress);
                        }

                        hm.put("shipping_address", mShippingAddress);

                    }

                    SimiData data = new SimiData(hm);
                    ExpressAddressFragment addressFragment = ExpressAddressFragment.newInstance(data);
                    SimiManager.getIntance().addPopupFragment(addressFragment);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    protected JSONObject getAddress() throws JSONException {
        JSONObject json = new JSONObject();

        if (null != mAddressForEdit) {
            String id = mAddressForEdit.getID();
            if (Utils.validateString(id)) {
                json.put("address_id", id);
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
                        return null;
                    }
                    json.put(key, value);
                }
            }
        }

        if (null != dobRowComponent) {
            ArrayList<Integer> dob = (ArrayList<Integer>) dobRowComponent.getValue();
            int day = dob.get(0);
            int month = dob.get(1);
            int year = dob.get(2);

            json.put("day", String.valueOf(day));
            json.put("month", String.valueOf(month));
            json.put("year", String.valueOf(year));
        }

        if (null != mGender) {
            json.put("gender", mGender.getValue());
        }

        if (null != mCountry) {
            String name = mCountry.getName();
            if (Utils.validateString(name)) {
                json.put("country_name", name);
            }

            String code = mCountry.getCode();
            if (Utils.validateString(code)) {
                json.put("country_code", code);
            }
        }

        if (null != mState) {
            String name = mState.getName();
            if (Utils.validateString(name)) {
                json.put("state_name", name);
            }

            String code = mState.getCode();
            if (Utils.validateString(code)) {
                json.put("state_code", code);
            }

            String id = mState.getID();
            if (Utils.validateString(id)) {
                json.put("state_id", id);
            }
        }
        return json;


    }

}

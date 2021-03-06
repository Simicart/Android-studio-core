package com.simicart.core.customer.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.entity.AddressEntity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;

public class AddressBookModel extends SimiModel {

    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            collection = new SimiCollection();
            for (int i = 0; i < list.length(); i++) {
                AddressEntity address = new AddressEntity();
                address.parse(list.getJSONObject(i));
                collection.addEntity(address);
            }
            Collections.reverse(collection.getCollection());
//			ConfigCheckout.newInstance().setCollectionAddressBook(collection);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.GET_USER_ADDRESS;
    }
}

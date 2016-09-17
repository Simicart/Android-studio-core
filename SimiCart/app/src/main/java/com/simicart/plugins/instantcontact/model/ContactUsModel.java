package com.simicart.plugins.instantcontact.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.plugins.instantcontact.entity.ContactUsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactUsModel extends SimiModel {

    @Override
    protected void parseData() {
        try {
            JSONArray list = this.mJSON.getJSONArray("data");
            collection = new SimiCollection();
            if (null != list && list.length() > 0) {
                JSONObject json = list.getJSONObject(0);
                ContactUsEntity contact = new ContactUsEntity();
                contact.parse(json);
                collection.addEntity(contact);
            }
        } catch (JSONException e) {

        }
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = "simicontact/api/get_contacts";
    }

}

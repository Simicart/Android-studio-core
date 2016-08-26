package com.simicart.core.checkout.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentMethodModel extends SimiModel {

    protected TotalPrice mTotalPrice;

    protected String FEE = "fee";

    @Override
    protected void parseData() {
        try {
            JSONArray array = this.mJSON.getJSONArray("data");
            if (null != array && array.length() > 0) {
                JSONObject json = array.getJSONObject(0);
                if (json.has(FEE)) {
                    JSONObject jsFee = json.getJSONObject(FEE);
                    parseFee(jsFee);
                }
            }
        } catch (JSONException e) {

        }
    }

    protected void parseFee(JSONObject json) throws JSONException {
        mTotalPrice = new TotalPrice();
        mTotalPrice.parse(json);
    }

    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.SAVE_PAYMENT_METHOD;
    }

    public TotalPrice getTotalPrice() {
        return mTotalPrice;
    }
}

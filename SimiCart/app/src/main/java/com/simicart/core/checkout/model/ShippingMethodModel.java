package com.simicart.core.checkout.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShippingMethodModel extends SimiModel {

    protected TotalPrice mTotalPrice;

    private ArrayList<PaymentMethodEntity> mListPaymentMethod;

    protected String PAYMENT_METHOD_LIST = "payment_method_list";
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

                if (json.has(PAYMENT_METHOD_LIST)) {
                    JSONArray arrayPayment = json.getJSONArray(PAYMENT_METHOD_LIST);
                    parseListPayment(arrayPayment);
                }

            }
        } catch (Exception e) {

        }
    }

    protected void parseFee(JSONObject json) throws JSONException {
        mTotalPrice = new TotalPrice();
        mTotalPrice.parse(json);
    }

    protected void parseListPayment(JSONArray array) throws JSONException {
        if (null != array && array.length() > 0) {
            mListPaymentMethod = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                PaymentMethodEntity paymentMethod = new PaymentMethodEntity();
                paymentMethod.parse(array.getJSONObject(i));
                mListPaymentMethod.add(paymentMethod);
            }
        }
    }


    @Override
    protected void setUrlAction() {
        mUrlAction = Constants.SAVE_SHIPPING_METHOD;
    }

    public TotalPrice getTotalPrice() {
        return mTotalPrice;
    }

    public ArrayList<PaymentMethodEntity> getListPaymentMehtod() {
        return mListPaymentMethod;
    }


}

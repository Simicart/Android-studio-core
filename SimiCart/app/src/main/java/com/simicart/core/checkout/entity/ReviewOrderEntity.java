package com.simicart.core.checkout.entity;

import android.util.Log;

import com.simicart.core.base.model.entity.SimiEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by frank on 25/08/2016.
 */
public class ReviewOrderEntity extends SimiEntity {

    protected ArrayList<ShippingMethodEntity> mListShippingMethod;
    protected ArrayList<PaymentMethodEntity> mListPaymentMethod;
    protected ArrayList<Condition> mListCondition;
    protected TotalPrice mTotalPrice;

    protected String SHIPPING_METHOD_LIST = "shipping_method_list";
    protected String PAYMENT_METHOD_LIST = "payment_method_list";
    protected String FEE = "fee";
    protected String CONDITION = "condition";

    @Override
    public void parse() {

        try {
            // shipping method
            if (hasKey(SHIPPING_METHOD_LIST)) {
                JSONArray array = getArray(SHIPPING_METHOD_LIST);
                parseListShipping(array);
            }

            // payment method
            if (hasKey(PAYMENT_METHOD_LIST)) {
                JSONArray array = getArray(PAYMENT_METHOD_LIST);
                parseListPayment(array);
            }

            if (hasKey(FEE)) {
                JSONObject json = getJSONObjectWithKey(mJSON, FEE);
                parseFee(json);
            }
        } catch (JSONException e) {
            Log.e("ReviewOrderEntity ", "Exception " + e.getMessage());
        }
    }

    protected void parseListShipping(JSONArray array) throws JSONException {
        if (null != array && array.length() > 0) {
            Log.e("ReviewOrderEntity", "parseListShipping " + array.toString());
            mListShippingMethod = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                ShippingMethodEntity shippingMethod = new ShippingMethodEntity();
                shippingMethod.parse(array.getJSONObject(i));
                mListShippingMethod.add(shippingMethod);
            }
        }
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


    protected void parseFee(JSONObject json) throws JSONException {
        mTotalPrice = new TotalPrice();
        mTotalPrice.parse(json);
        // term & condition
        if (json.has(CONDITION)) {
            JSONArray array = json.getJSONArray(CONDITION);
            parseListCondition(array);
        }
    }

    protected void parseListCondition(JSONArray array) throws JSONException {
        if (null != array && array.length() > 0) {
            mListCondition = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                Condition condition = new Condition();
                condition.setJSONObject(array.getJSONObject(i));
                mListCondition.add(condition);
            }
        }
    }

    public ArrayList<ShippingMethodEntity> getListShippingMethod() {
        return mListShippingMethod;
    }

    public ArrayList<PaymentMethodEntity> getListPaymentMethod() {
        return mListPaymentMethod;
    }

    public TotalPrice getTotalPrice() {
        return mTotalPrice;
    }

    public ArrayList<Condition> getListCondition() {
        return mListCondition;
    }


    public void setTotalPrice(TotalPrice totalPrice) {
        mTotalPrice = totalPrice;
    }

    public void setListPaymentMethod(ArrayList<PaymentMethodEntity> listPayment) {
        mListPaymentMethod = listPayment;
    }

    public void setListShippingMethod(ArrayList<ShippingMethodEntity> listShipping) {
        mListShippingMethod = listShipping;
    }

    protected void setListCondition(ArrayList<Condition> conditions) {
        mListCondition = conditions;
    }
}

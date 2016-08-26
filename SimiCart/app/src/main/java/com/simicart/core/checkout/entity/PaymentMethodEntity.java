package com.simicart.core.checkout.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by frank on 6/29/16.
 */
public class PaymentMethodEntity extends SimiEntity {

    public enum PAYMENTMETHODTYPE {
        OFFLINE,
        CARD,
        SDK,
        WEBVIEW
    }

    protected String mPaymentMethod;
    protected String mContent;
    protected String mTitle;
    protected boolean isSelected;
    protected ArrayList<CreditCardEntity> mListCCType;
    protected boolean isUseCVV;
    protected PAYMENTMETHODTYPE mType;
    /**
     * This variable was used to save card information that user entered.
     */
    protected CreditCardEntity mCurrentCardEntity;

    private String payment_method = "payment_method";
    private String title = "title";
    private String useccv = "useccv";
    private String show_type = "show_type";
    private String p_method_selected = "p_method_selected";
    private String cc_types = "cc_types";
    private String content = "content";


    @Override
    public void parse() {

        if (hasKey(payment_method)) {
            mPaymentMethod = getData(payment_method);
        }

        if (hasKey(title)) {
            mTitle = getData(title);
        }

        if (hasKey(content)) {
            mContent = getData(content);
        }

        if (hasKey(useccv)) {
            String use = getData(useccv);
            if (Utils.TRUE(use)) {
                setUseCVV(true);
            }
        }

        if (hasKey(p_method_selected)) {
            String selected = getData(p_method_selected);
            if (Utils.TRUE(selected)) {
                setSelected(true);
            }
        }

        if (hasKey(cc_types)) {
            JSONArray array = getArray(cc_types);
            if (null != array && array.length() > 0) {
                mListCCType = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject json = array.getJSONObject(i);
                        CreditCardEntity entity = new CreditCardEntity();
                        entity.parse(json);
                        mListCCType.add(entity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        if (hasKey(show_type)) {
            String type = getData(show_type);
            parseType(type);
        }

    }

    protected void parseType(String type) {
        if (Utils.validateString(type)) {
            switch (type) {
                case "0":
                    setType(PAYMENTMETHODTYPE.OFFLINE);
                    break;
                case "1":
                    setType(PAYMENTMETHODTYPE.CARD);
                    break;
                case "2":
                    setType(PAYMENTMETHODTYPE.SDK);
                    break;
                case "3":
                    setType(PAYMENTMETHODTYPE.WEBVIEW);
                    break;
            }
        }
    }

    public JSONObject toParamSave() {
        try {
            JSONObject json = new JSONObject();
            JSONObject jsParam = new JSONObject();
            jsParam.put("method", mPaymentMethod);


            if (mType == PAYMENTMETHODTYPE.CARD) {
                jsParam = addParamForCard(jsParam);
            }
            json.put("p_method", jsParam);
            return json;
        } catch (JSONException e) {

        }

        return null;
    }

    protected JSONObject addParamForCard(JSONObject jsParam) throws JSONException {

        if (null == mCurrentCardEntity) {
            return jsParam;
        }

        String type = mCurrentCardEntity.getType();
        if (Utils.validateString(type)) {
            jsParam.put("cc_type", type);
        }

        String cid = mCurrentCardEntity.getCId();
        if (Utils.validateString(cid)) {
            jsParam.put("cc_cid", cid);
        }

        String expMonth = mCurrentCardEntity.getExpMonth();
        if (Utils.validateString(expMonth)) {
            jsParam.put("cc_exp_month", expMonth);
        }

        String number = mCurrentCardEntity.getNumber();
        if (Utils.validateString(number)) {
            jsParam.put("cc_number", number);
        }

        String expYear = mCurrentCardEntity.getExpYear();
        if (Utils.validateString(expYear)) {
            jsParam.put("cc_exp_year", expYear);
        }


        return jsParam;
    }

    public CreditCardEntity getCurrentCardEntity() {
        return mCurrentCardEntity;
    }

    public void setCurrentCardEntity(CreditCardEntity mCurrentCardEntity) {
        this.mCurrentCardEntity = mCurrentCardEntity;
    }

    public PAYMENTMETHODTYPE getType() {
        return mType;
    }

    public void setType(PAYMENTMETHODTYPE mType) {
        this.mType = mType;
    }

    public boolean isUseCVV() {
        return isUseCVV;
    }

    public void setUseCVV(boolean useCVV) {
        isUseCVV = useCVV;
    }

    public String getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(String mPaymentMethod) {
        this.mPaymentMethod = mPaymentMethod;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<CreditCardEntity> getListCCType() {
        return mListCCType;
    }

    public void setListCCType(ArrayList<CreditCardEntity> mListCCType) {
        this.mListCCType = mListCCType;
    }
}

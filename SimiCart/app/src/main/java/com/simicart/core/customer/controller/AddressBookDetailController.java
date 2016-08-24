package com.simicart.core.customer.controller;

import java.util.ArrayList;
import java.util.HashMap;


import android.annotation.SuppressLint;
import android.view.View.OnClickListener;

import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.KeyData;
import com.simicart.core.customer.delegate.AddressBookDetailDelegate;
import com.simicart.core.customer.entity.AddressEntity;
import com.simicart.core.customer.model.GetCountryModel;

@SuppressLint("DefaultLocale")
public class AddressBookDetailController extends SimiController {
    protected OnClickListener mSaveListener;
    protected AddressBookDetailDelegate mDelegate;
    protected HashMap<String, Object> hmData;
    protected ArrayList<SimiRowComponent> mListRow;
    protected int openFor;
    protected int action;
    protected AddressEntity mShippingAddress;
    protected AddressEntity mBillingAddress;


    @Override
    public void onStart() {
        parseParam();

        initListener();

        onRequestCountryAllowed();


    }

    protected void parseParam() {
        if (null != hmData) {

            if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.OPEN_FOR)) {
                Integer open = (Integer) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.OPEN_FOR);
                this.openFor = open.intValue();
            }

            if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.ACTION)) {
                Integer act = (Integer) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.ACTION);
                this.action = act.intValue();
            }

            if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS)) {
                mBillingAddress = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS);
            }

            if (hmData.containsKey(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS)) {
                mShippingAddress = (AddressEntity) hmData.get(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS);
            }

        }
    }

    protected void initListener() {

    }


    protected void onRequestCountryAllowed() {
        mDelegate.showLoading();
        mModel = new GetCountryModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                ArrayList<SimiEntity> entity = mModel.getCollection()
                        .getCollection();

            }
        });

        mModel.request();
    }

    @Override
    public void onResume() {
    }


    public OnClickListener getSaveListener() {
        return mSaveListener;
    }


    public void setDelegate(AddressBookDetailDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    public void setData(HashMap<String, Object> data) {
        hmData = data;
    }

}

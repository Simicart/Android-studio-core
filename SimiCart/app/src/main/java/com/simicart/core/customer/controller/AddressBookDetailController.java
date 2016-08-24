package com.simicart.core.customer.controller;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.fragment.ReviewOrderFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.delegate.AddressBookDetailDelegate;
import com.simicart.core.customer.delegate.ChooseCountryDelegate;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.entity.StateOfCountry;
import com.simicart.core.customer.fragment.CountryFragment;
import com.simicart.core.customer.model.GetCountryModel;

@SuppressLint("DefaultLocale")
public class AddressBookDetailController extends SimiController {
    protected OnClickListener mSaveListener;
    protected AddressBookDetailDelegate mDelegate;
    protected ArrayList<SimiRowComponent> mListRow;

    @Override
    public void onStart() {

        initListener();

        onRequestCountryAllowed();


    }

    protected void initListener(){

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


}

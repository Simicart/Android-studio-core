package com.simicart.core.customer.controller;

import android.annotation.SuppressLint;
import android.view.View;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Constants;
import com.simicart.core.customer.model.AddressBookModel;

@SuppressLint("ClickableViewAccessibility")
public class AddressBookController extends SimiController {

    protected SimiDelegate mDelegate;
    protected View.OnClickListener mSaveListener;

    protected int addressBookFor = -1;


    public void setDelegate(SimiDelegate delegate) {
        mDelegate = delegate;
    }


    @Override
    public void onStart() {

        mDelegate.showLoading();
        mModel = new AddressBookModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.updateView(mModel.getCollection());
            }
        });
        if(addressBookFor == Constants.KeyAddressBook.CUSTOMER_ADDRESS) {
            mModel.addBody("is_get_order_address", "NO");
        } else {
            mModel.addBody("is_get_order_address", "YES");
        }
        mModel.addBody("user_email", "v@simi.com");
        mModel.addBody("user_password", "123456");
        mModel.request();



    }

    @Override
    public void onResume() {

    }

    

}

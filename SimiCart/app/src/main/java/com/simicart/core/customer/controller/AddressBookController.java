package com.simicart.core.customer.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.fragment.AddressBookDetailFragment;
import com.simicart.core.customer.fragment.NewAddressBookFragment;
import com.simicart.core.customer.model.AddressBookModel;

import org.json.JSONObject;

@SuppressLint("ClickableViewAccessibility")
public class AddressBookController extends SimiController {

    protected SimiDelegate mDelegate;
    protected OnTouchListener mListener;
    protected int addressBookFor = -1;

    public OnTouchListener getListener() {
        return mListener;
    }

    public void setDelegate(SimiDelegate delegate) {
        mDelegate = delegate;
    }

    public void setAddressBookFor(int addressBookFor) {
        this.addressBookFor = addressBookFor;
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

        mListener = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                NewAddressBookFragment fragment = NewAddressBookFragment
//                        .newInstance();
//                Bundle bundleAfter = new Bundle();
//                SimiFragment.setData(Constants.KeyData.AFTER_CONTROL,
//                        Constants.NEW_ADDRESS, Constants.KeyData.TYPE_INT,
//                        bundleAfter);
//                if (DataLocal.isTablet) {
//                    SimiManager.getIntance().replacePopupFragment(fragment);
//                } else {
//                    SimiManager.getIntance().replaceFragment(fragment);
//                }
                return true;
            }
        };

    }

    @Override
    public void onResume() {
//        if (ConfigCheckout.getInstance().getStatusAddressBook() == true) {
//            if (mModel != null)
                mDelegate.updateView(mModel.getCollection());
//        } else {
//            mDelegate.updateView(ConfigCheckout.getInstance()
//                    .getCollectionAddressBook());
//        }
    }
}

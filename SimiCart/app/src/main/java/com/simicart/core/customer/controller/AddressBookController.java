package com.simicart.core.customer.controller;

import android.annotation.SuppressLint;
import android.view.View;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.ValueData;
import com.simicart.core.customer.entity.AddressEntity;
import com.simicart.core.customer.model.AddressBookModel;

import java.util.HashMap;

@SuppressLint("ClickableViewAccessibility")
public class AddressBookController extends SimiController {

    protected SimiDelegate mDelegate;
    protected View.OnClickListener mCreateNewListener;
    protected HashMap<String, Object> mData;
    protected int openFor = -1;


    @Override
    public void onStart() {
        parseParam();

        initListener();

        requestListAddress();
    }

    protected void parseParam() {
        if (mData.containsKey(KeyData.ADDRESS_BOOK.OPEN_FOR)) {
            openFor = ((Integer) mData.get(KeyData.ADDRESS_BOOK.OPEN_FOR)).intValue();
        }
    }

    protected void initListener() {
        mCreateNewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAction();
            }
        };
    }

    protected void requestListAddress() {
        mDelegate.showLoading();
        mModel = new AddressBookModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.updateView(mModel.getCollection());
            }
        });
        if (openFor == ValueData.ADDRESS_BOOK.OPEN_FOR_CUSTOMER) {
            mModel.addBody("is_get_order_address", "NO");
        } else {
            mModel.addBody("is_get_order_address", "YES");
        }
        mModel.request();
    }

    protected void createNewAction() {
        HashMap<String, Object> data = new HashMap<>();

        if (openFor == ValueData.ADDRESS_BOOK.OPEN_FOR_CHECKOUT) {
            data.put(KeyData.ADDRESS_BOOK_DETAIL.OPEN_FOR, ValueData.ADDRESS_BOOK_DETAIL.OPEN_FOR_CHECKOUT);
            data.put(KeyData.ADDRESS_BOOK_DETAIL.ACTION, ValueData.ADDRESS_BOOK_DETAIL.ACTION_NEW);
            if (mData.containsKey(KeyData.ADDRESS_BOOK.BILLING_ADDRESS)) {
                AddressEntity billingAddress = (AddressEntity) mData.get(KeyData.ADDRESS_BOOK.BILLING_ADDRESS);
                data.put(KeyData.ADDRESS_BOOK_DETAIL.BILLING_ADDRESS, billingAddress);
            }

            if (mData.containsKey(KeyData.ADDRESS_BOOK.SHIPPING_ADDRESS)) {
                AddressEntity shippingAddress = (AddressEntity) mData.get(KeyData.ADDRESS_BOOK.SHIPPING_ADDRESS);
                data.put(KeyData.ADDRESS_BOOK_DETAIL.SHIPPING_ADDRESS, shippingAddress);
            }
        } else {
            data.put(KeyData.ADDRESS_BOOK_DETAIL.OPEN_FOR, ValueData.ADDRESS_BOOK_DETAIL.OPEN_FOR_CUSTOMER);
            data.put(KeyData.ADDRESS_BOOK_DETAIL.ACTION, ValueData.ADDRESS_BOOK_DETAIL.ACTION_NEW);
        }

        SimiManager.getIntance().openAddressBookDetail(data);

    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }

    public View.OnClickListener getCreateNewListener() {
        return mCreateNewListener;
    }

    public void setData(HashMap<String, Object> data) {
        mData = data;
    }

    public void setDelegate(SimiDelegate delegate) {
        mDelegate = delegate;
    }

}

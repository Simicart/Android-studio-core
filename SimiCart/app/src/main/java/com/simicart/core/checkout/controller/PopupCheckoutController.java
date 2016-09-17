package com.simicart.core.checkout.controller;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.checkout.delegate.CartDelegate;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.fragment.SignInFragment;

import java.util.HashMap;

@SuppressLint("ClickableViewAccessibility")
public class PopupCheckoutController extends SimiController {

    protected CartDelegate mBlockDelegate;
    protected OnTouchListener onCancel;
    protected OnTouchListener onExcustomer;
    protected OnTouchListener onNewcustomer;
    protected OnTouchListener onAsguest;

    public OnTouchListener getOnAsguest() {
        return onAsguest;
    }

    public OnTouchListener getOnCancel() {
        return onCancel;
    }

    public OnTouchListener getOnExcustomer() {
        return onExcustomer;
    }

    public OnTouchListener getOnNewcustomer() {
        return onNewcustomer;
    }

    @Override
    public void onStart() {
        cancelAction();
        exCustomerAction();
        newCustomerAction();
        guestAction();
    }

    @Override
    public void onResume() {

    }

    public void cancelAction() {
        onCancel = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setBackgroundColor(Color.parseColor("#EBEBEB"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mBlockDelegate.dismissPopupCheckout();
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        v.setBackgroundColor(0);
                        break;
                    }
                }
                return true;
            }
        };
    }

    public void exCustomerAction() {
        onExcustomer = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setBackgroundColor(Color.parseColor("#EBEBEB"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mBlockDelegate.dismissPopupCheckout();
                        HashMap<String, Object> hmData = new HashMap<>();
                        hmData.put(KeyData.SIGN_IN.IS_CHECKOUT, true);
                        SignInFragment fragment = SignInFragment.newInstance(new SimiData(hmData));
                        if (DataLocal.isTablet) {
                            SimiManager.getIntance().replacePopupFragment(fragment);
                        } else {
                            SimiManager.getIntance().replaceFragment(fragment);
                        }
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        v.setBackgroundColor(0);
                        break;
                    }
                }
                return true;
            }
        };
    }

    public void newCustomerAction() {
        onNewcustomer = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setBackgroundColor(Color.parseColor("#EBEBEB"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mBlockDelegate.dismissPopupCheckout();
                        HashMap<String, Object> hm = new HashMap<>();
                        hm.put(KeyData.ADDRESS_BOOK_DETAIL.OPEN_FOR, ValueData.ADDRESS_BOOK_DETAIL.OPEN_FOR_CHECKOUT);
                        hm.put(KeyData.ADDRESS_BOOK_DETAIL.ACTION, ValueData.ADDRESS_BOOK_DETAIL.ACTION_NEW_CUSTOMER);
                        SimiManager.getIntance().openAddressBookDetail(hm);

                    }

                    case MotionEvent.ACTION_CANCEL: {
                        v.setBackgroundColor(0);
                        break;
                    }
                }
                return true;
            }
        };
    }

    public void guestAction() {
        onAsguest = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setBackgroundColor(Color.parseColor("#EBEBEB"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mBlockDelegate.dismissPopupCheckout();
                        HashMap<String, Object> hm = new HashMap<>();
                        hm.put(KeyData.ADDRESS_BOOK_DETAIL.OPEN_FOR, ValueData.ADDRESS_BOOK_DETAIL.OPEN_FOR_CHECKOUT);
                        hm.put(KeyData.ADDRESS_BOOK_DETAIL.ACTION, ValueData.ADDRESS_BOOK_DETAIL.ACTION_GUEST);
                        SimiManager.getIntance().openAddressBookDetail(hm);
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        v.setBackgroundColor(0);
                        break;
                    }
                }
                return true;
            }
        };
    }

    public void setDelegate(CartDelegate mBlockDelegate) {
        this.mBlockDelegate = mBlockDelegate;
    }

}

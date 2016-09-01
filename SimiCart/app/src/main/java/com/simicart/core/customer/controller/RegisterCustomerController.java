package com.simicart.core.customer.controller;

import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.customer.delegate.RegisterCustomerDelegate;

import java.util.ArrayList;

public class RegisterCustomerController extends SimiController {
    protected RegisterCustomerDelegate mDelegate;
    protected ArrayList<SimiRowComponent> mListRow;


    public void setDelegate(RegisterCustomerDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    @Override
    public void onStart() {

    }

    protected void  initListener(){

    }

    protected void initRows(){

    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }


}

package com.simicart.core.customer.controller;

import android.view.View;

import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.component.SimiTextRowComponent;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.common.ValueData;
import com.simicart.core.customer.delegate.RegisterCustomerDelegate;

import java.util.ArrayList;

public class CustomerController extends SimiController {
    protected RegisterCustomerDelegate mDelegate;
    protected ArrayList<SimiRowComponent> mListRow;
    protected View.OnClickListener mRegisterListener;


    public void setDelegate(RegisterCustomerDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    @Override
    public void onStart() {

        initListener();

        initRows();

    }


    protected void initListener() {
        mRegisterListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister();
            }
        };
    }

    protected void initRows() {
        // prefix

        // full name

        // suffix

        // date of birth

        // gender

        // tax/VAT number

        // email

        // password

        // confirm password
    }

//    protected void initComponentRequired(String title, String keyForData, String keyForParam, int inputType) {
//        SimiTextRowComponent component = new SimiTextRowComponent();
//        component.setTitle(title);
//        component.setRequired(true);
//        if (this.action == ValueData.ADDRESS_BOOK_DETAIL.ACTION_EDIT && null != mAddressForEdit) {
//            String name = mAddressForEdit.getData(keyForData);
//            component.setValue(name);
//        }
//        component.setKey(keyForParam);
//        component.setInputType(inputType);
//        View nameView = component.createView();
//        mListRow.add(nameView);
//        mListRowComponent.add(component);
//    }

    protected void onRegister() {

    }



    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }


    public View.OnClickListener getRegisterListener() {
        return mRegisterListener;
    }

}

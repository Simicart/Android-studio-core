package com.simicart.core.customer.entity;

import com.simicart.core.base.model.entity.SimiEntity;

/**
 * Created by frank on 31/08/2016.
 */
public class CustomerEntity extends SimiEntity {

    protected AddressEntity mAddress;
    protected String mPassword;

    @Override
    public void parse() {

        mAddress = new AddressEntity();
        mAddress.parse(mJSON);


    }

    public AddressEntity getAddress() {
        return mAddress;
    }

    public void setAddress(AddressEntity mAddress) {
        this.mAddress = mAddress;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}

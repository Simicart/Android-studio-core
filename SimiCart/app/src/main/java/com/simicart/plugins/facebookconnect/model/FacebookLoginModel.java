package com.simicart.plugins.facebookconnect.model;

import com.simicart.core.customer.model.SignInModel;

public class FacebookLoginModel extends SignInModel {

    @Override
    protected void setUrlAction() {
        mUrlAction = "simifblogin/index/login";
    }
}

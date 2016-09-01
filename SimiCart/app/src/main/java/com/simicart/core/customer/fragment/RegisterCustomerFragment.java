package com.simicart.core.customer.fragment;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.customer.controller.CustomerController;

public class RegisterCustomerFragment extends SimiFragment {
    protected CustomerController mController;

    public static RegisterCustomerFragment newInstance() {
        RegisterCustomerFragment fragment = new RegisterCustomerFragment();
        return fragment;
    }


}

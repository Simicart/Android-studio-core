package com.simicart.plugins.custompayment;

import android.os.Bundle;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;

/**
 * Created by frank on 30/08/2016.
 */
public class CustomPaymentFragment extends SimiFragment {

    public static CustomPaymentFragment newIntance(SimiData data) {
        CustomPaymentFragment fragment = new CustomPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

}

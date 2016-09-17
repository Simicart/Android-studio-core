package com.simicart.core.base.event.fragment;

import com.simicart.core.base.fragment.SimiFragment;

import java.io.Serializable;

/**
 * Created by MSI on 17/08/2016.
 */
public class SimiEventFragmentEntity implements Serializable {
    private SimiFragment mFragment;

    public void setmFragment(SimiFragment fragment) {
        mFragment = fragment;
    }

    public SimiFragment getFragment() {
        return mFragment;
    }
}

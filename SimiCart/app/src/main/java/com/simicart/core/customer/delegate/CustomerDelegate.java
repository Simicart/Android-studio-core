package com.simicart.core.customer.delegate;

import android.view.View;

import com.simicart.core.base.delegate.SimiDelegate;

import java.util.ArrayList;

public interface CustomerDelegate extends SimiDelegate {

    public void showView(ArrayList<View> rows);

}

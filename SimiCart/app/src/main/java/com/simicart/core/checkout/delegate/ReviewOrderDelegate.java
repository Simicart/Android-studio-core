package com.simicart.core.checkout.delegate;

import android.view.View;

import com.simicart.core.base.delegate.SimiDelegate;

import java.util.ArrayList;

public interface ReviewOrderDelegate extends SimiDelegate {

    public void showView(ArrayList<View> rows);

}

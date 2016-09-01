package com.simicart.core.customer.delegate;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.customer.entity.RegisterCustomer;

import java.util.ArrayList;

public interface CustomerDelegate extends SimiDelegate{

	public void showView(ArrayList<View> rows);

}

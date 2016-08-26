package com.simicart.core.customer.block;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.MyAccountDelegate;

public class MyAccountBlock extends SimiBlock implements MyAccountDelegate {

    protected LinearLayout llMyAccount;

    public MyAccountBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        llMyAccount = (LinearLayout) mView.findViewById(Rconfig.getInstance().id("ll_my_account"));
    }


    @Override
    public void addItemRow(View view) {
        if(view != null) {
            llMyAccount.addView(view);
        }
    }
}

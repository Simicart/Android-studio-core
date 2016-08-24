package com.simicart.core.customer.block;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.MyAccountDelegate;
import com.simicart.core.material.LayoutRipple;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

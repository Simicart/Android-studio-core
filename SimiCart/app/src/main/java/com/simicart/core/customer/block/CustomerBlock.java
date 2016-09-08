package com.simicart.core.customer.block;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.customer.delegate.CustomerDelegate;

import java.util.ArrayList;

@SuppressLint("DefaultLocale")
public class CustomerBlock extends SimiBlock implements
        CustomerDelegate {

    protected LinearLayout llRegister;
    protected AppCompatButton btnRegister;
    protected int topMargin = Utils.toDp(10);
    protected int mOpenFor;

    public CustomerBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        llRegister = (LinearLayout) id("ll_register");

        btnRegister = (AppCompatButton) id("btn_register");
        String tranlateRegister = SimiTranslator.getInstance().translate("Register");
        if (mOpenFor == ValueData.CUSTOMER_PAGE.OPEN_FOR_REGISTER) {
            tranlateRegister = SimiTranslator.getInstance().translate("Save");
        }
        btnRegister.setText(tranlateRegister);
        btnRegister.setTextColor(AppColorConfig.getInstance().getContentColor());
        btnRegister.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
    }

    @Override
    public void showView(ArrayList<View> rows) {
        llRegister.removeAllViewsInLayout();
        if (null != rows && rows.size() > 0) {
            for (int i = 0; i < rows.size(); i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = topMargin;
                llRegister.addView(rows.get(i));
            }
        }
    }

    public void setRegisterListener(OnClickListener listener) {
        btnRegister.setOnClickListener(listener);
    }

    public void setOpenFor(int openFor) {
        mOpenFor = openFor;
    }

}

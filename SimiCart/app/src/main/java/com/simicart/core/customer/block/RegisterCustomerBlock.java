package com.simicart.core.customer.block;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.adapter.GenderAdapter;
import com.simicart.core.customer.delegate.RegisterCustomerDelegate;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.RegisterCustomer;
import com.simicart.core.material.ButtonRectangle;
import com.simicart.core.material.LayoutRipple;

import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("DefaultLocale")
public class RegisterCustomerBlock extends SimiBlock implements
        RegisterCustomerDelegate {

    protected LinearLayout llRegister;
    protected AppCompatButton btnRegister;

    @Override
    public void initView() {
        llRegister = (LinearLayout) id("ll_register");


        btnRegister = (AppCompatButton) id("btn_register");
        String tranlateRegister = SimiTranslator.getInstance().translate("Register");
        btnRegister.setText(tranlateRegister);
        btnRegister.setTextColor(AppColorConfig.getInstance().getContentColor());
        btnRegister.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
    }

    @Override
    public void showView(ArrayList<View> rows) {
        llRegister.removeAllViewsInLayout();
        if (null != rows && rows.size() > 0) {
            for (int i = 0; i < rows.size(); i++) {
                llRegister.addView(rows.get(i));
            }
        }

    }

    public void setRegisterListener(OnClickListener listener) {
        btnRegister.setOnClickListener(listener);
    }

}

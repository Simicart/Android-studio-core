package com.simicart.core.customer.block;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.AddressBookDetailDelegate;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.CountryAllowed;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.entity.StateOfCountry;
import com.simicart.core.material.ButtonRectangle;

import java.util.ArrayList;

public class AddressBookDetailBlock extends SimiBlock implements
        AddressBookDetailDelegate {

    protected LinearLayout llBody;
    protected AppCompatButton btnSave;

    public AddressBookDetailBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        llBody = (LinearLayout) id("ll_body_address");
        btnSave = (AppCompatButton) id("btn_save");
        String textSave = SimiTranslator.getInstance().translate("Save");
        btnSave.setText(textSave);
        btnSave.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
    }


    @Override
    public void showRows(ArrayList<View> rows) {
        if (null != rows && rows.size() > 0) {
            for (int i = 0; i < rows.size(); i++) {
                View row = rows.get(i);
                llBody.addView(row);
            }
        }
    }
}

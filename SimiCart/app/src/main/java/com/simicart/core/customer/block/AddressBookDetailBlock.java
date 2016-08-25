package com.simicart.core.customer.block;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.customer.delegate.AddressBookDetailDelegate;

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
        llBody.removeAllViewsInLayout();
        if (null != rows && rows.size() > 0) {
            for (int i = 0; i < rows.size(); i++) {
                View row = rows.get(i);
                llBody.addView(row);
            }
        }
    }
}

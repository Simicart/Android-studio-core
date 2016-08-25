package com.simicart.core.base.component;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.TextView;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;

/**
 * Created by frank on 23/08/2016.
 */
public class SimiTextRowComponent extends SimiRowComponent {

    protected AppCompatEditText edtBody;
    protected int mInputType = -1;
    protected TextInputLayout tvTitle;

    public SimiTextRowComponent() {
        super();
        mType = TYPE_ROW.TEXT;
    }


    @Override
    protected void initView() {
        rootView = findLayout("core_component_text_row");

    }

    @Override
    protected void initHeader() {
        tvTitle = (TextInputLayout) findView("tv_title");
        if (Utils.validateString(mTitle)) {
            String translateTitle = SimiTranslator.getInstance().translate(mTitle);
            tvTitle.setHint(translateTitle);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initBody() {
        edtBody = (AppCompatEditText) findView("edt_body");
        if (mInputType > -1) {
            edtBody.setInputType(mInputType);
        }
        edtBody.setTextColor(AppColorConfig.getInstance().getContentColor());
        if (null != mValue && Utils.validateString(String.valueOf(mValue))) {
            edtBody.setText(String.valueOf(mValue));
        }
    }

    @Override
    public Object getValue() {
        return edtBody.getText().toString();
    }

    public void setInputType(int inputType) {
        mInputType = inputType;
    }


    public int getInputType() {
        return mInputType;
    }


}

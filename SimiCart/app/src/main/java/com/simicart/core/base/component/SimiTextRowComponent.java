package com.simicart.core.base.component;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
            if (isRequired) {
                translateTitle = translateTitle + "(*)";
            }
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
        String value = edtBody.getText().toString();
        if (isRequired && value.equals("")) {
            String warmingText = "This field";
            if (Utils.validateString(mTitle)) {
                warmingText = mTitle;
            }
            String warming = warmingText + " is required.";
            showError(warming);
            return null;
        }

        return value;
    }

    public void showError(String warming) {
        if (Utils.validateString(warming)) {
            String translateWarming = SimiTranslator.getInstance().translate(warming);
            tvTitle.setError(translateWarming);
            tvTitle.setErrorEnabled(true);
            edtBody.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtBody, InputMethodManager.SHOW_IMPLICIT);
        } else {
            tvTitle.setErrorEnabled(false);
        }
    }

    public void setInputType(int inputType) {
        mInputType = inputType;
    }


    public int getInputType() {
        return mInputType;
    }


}

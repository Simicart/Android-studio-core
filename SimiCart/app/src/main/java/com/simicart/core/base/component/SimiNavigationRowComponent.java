package com.simicart.core.base.component;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.component.callback.NavigationRowCallBack;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 23/08/2016.
 */
public class SimiNavigationRowComponent extends SimiRowComponent {

    protected ImageView imgExtend;
    protected EditText edtBody;
    protected TextView tvBody;
    protected boolean isEnableEdit = false;
    protected NavigationRowCallBack mCallBack;
    protected int iconExtend = Rconfig.getInstance().drawable("ic_extend");

    public SimiNavigationRowComponent() {
        super();
        mType = TYPE_ROW.NAVIGATION;
    }

    @Override
    protected void initView() {
        rootView = findLayout("core_component_navigation_row");
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCallBack && !isEnableEdit) {
                    mCallBack.onNavigate();
                }
            }
        });
    }

    @Override
    protected void initHeader() {
        super.initHeader();
    }

    @Override
    protected void initBody() {
        imgExtend = (ImageView) findView("img_extend");
        imgExtend.setImageResource(iconExtend);
        edtBody = (EditText) findView("edt_body");
        tvBody = (TextView) findView("tv_body");
        updateView();
    }

    @Override
    public void updateView() {
        if (null != mValue && Utils.validateString(String.valueOf(mValue))) {
            isEnableEdit = false;
            edtBody.setVisibility(View.GONE);
            tvBody.setVisibility(View.VISIBLE);
            imgExtend.setVisibility(View.VISIBLE);
            tvBody.setText(String.valueOf(mValue));
        }
    }

    public void enableEdit() {
        isEnableEdit = true;
        imgExtend.setVisibility(View.GONE);
        tvBody.setVisibility(View.GONE);
        edtBody.setVisibility(View.VISIBLE);
        String hintText = mSuggestValue;
//        if (null != mValue) {
//            hintText = String.valueOf(mValue);
//        }
        String translateSuggest = SimiTranslator.getInstance().translate(hintText);
        edtBody.setHint(translateSuggest);
    }

    @Override
    public Object getValue() {
        return super.getValue();
    }

    public void setIconExtend(int idResource) {
        iconExtend = idResource;
    }

    public void setCallBack(NavigationRowCallBack callBack) {
        mCallBack = callBack;
    }

}

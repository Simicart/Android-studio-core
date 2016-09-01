package com.simicart.core.base.component;

import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.simicart.core.base.component.callback.SpinnerRowCallBack;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 23/08/2016.
 */
public class SimiSpinnerRowComponent extends SimiRowComponent {

    protected Spinner mSpinner;
    protected Object mValueSelected;
    protected TextView tvTitle;
    protected ImageView imgExtend;
    protected BaseAdapter mAdapter;
    protected SpinnerRowCallBack mCallBack;
    protected int mPositionSelected;
    protected int iconExtend = Rconfig.getInstance().drawable("");

    public SimiSpinnerRowComponent() {
        super();
        mType = TYPE_ROW.ADAPTER;
    }

    @Override
    protected void initView() {
        rootView = findLayout("core_component_spinner_row");
    }

    @Override
    protected void initHeader() {
        tvTitle = (TextView) findView("tv_title_spinner");
        if (Utils.validateString(mTitle)) {
            tvTitle.setText(mTitle);
        }
    }

    @Override
    protected void initBody() {
        mSpinner = (Spinner) findView("spinner");
        imgExtend = (ImageView) findView("img_extend");
        imgExtend.setImageResource(iconExtend);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setSelection(mPositionSelected);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null != mCallBack) {
                    mCallBack.onSelect(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public Object getValue() {
        return super.getValue();
    }

    public void setCallBack(SpinnerRowCallBack callBack) {
        mCallBack = callBack;
    }

    public void setIconExtend(int idResource) {
        iconExtend = idResource;
    }

    public void setPositinSelected(int position) {
        mPositionSelected = position;
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
    }
}

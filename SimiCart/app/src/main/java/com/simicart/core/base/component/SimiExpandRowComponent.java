package com.simicart.core.base.component;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.component.callback.ExpandRowCallBack;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by frank on 31/08/2016.
 */
public class SimiExpandRowComponent extends SimiRowComponent {


    protected RelativeLayout rltParent;
    protected LinearLayout llChild;
    protected ArrayList<String> mListValueChild;
    protected int icDown = Rconfig.getInstance().drawable("ic_down");
    protected int margin = Utils.toDp(5);
    protected ExpandRowCallBack mCallBack;
    protected TextView tvTitle;
    protected boolean isInitChild;


    @Override
    protected void initView() {
        rootView = findLayout("core_component_expand_row");
    }

    @Override
    protected void initBody() {
        rltParent = (RelativeLayout) findView("rlt_parent");
        llChild = (LinearLayout) findView("ll_child");
        llChild.setVisibility(View.GONE);
        tvTitle = (TextView) findView("tv_title_expand");
        if (Utils.validateString(mTitle)) {
            tvTitle.setText(mTitle);
        }

        // value
        TextView tvValue = (TextView) findView("tv_parent");
        if (null != mValue) {
            tvValue.setText(String.valueOf(mValue));
        }

        rltParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llChild.getVisibility() != View.VISIBLE) {
                    llChild.setVisibility(View.VISIBLE);
                    if (!isInitChild) {
                        initListChild();
                    }

                } else {
                    llChild.setVisibility(View.GONE);
                }
            }
        });

        // initial list child
        //initListChild();
    }

    protected void initListChild() {
        isInitChild = true;
        if (null != mListValueChild && mListValueChild.size() > 0) {
            for (int i = 0; i < mListValueChild.size(); i++) {

                initChild(i);
            }
        }
    }

    protected void initChild(final int position) {
        RadioButton tvChild = new RadioButton(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = margin;
        params.bottomMargin = margin;
        tvChild.setLayoutParams(params);
        tvChild.setText(mListValueChild.get(position));
        llChild.addView(tvChild);
        tvChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCallBack && !mCallBack.onSelect(position)) {
                    llChild.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setListValue(ArrayList<String> listValue) {
        mListValueChild = listValue;
    }

    public void setCallBack(ExpandRowCallBack callBack) {
        mCallBack = callBack;
    }

}

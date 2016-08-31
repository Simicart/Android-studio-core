package com.simicart.core.checkout.component;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.delegate.TermConditionCallBack;
import com.simicart.core.checkout.entity.Condition;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 6/30/16.
 */
public class TermConditionComponent extends SimiComponent {

    protected CheckBox cbAgree;
    protected ImageView imgExtend;
    protected Condition mCondition;
    protected TermConditionCallBack mCallBack;

    public TermConditionComponent(Condition condition) {
        super();
        mCondition = condition;
    }

    @Override
    public View createView() {

        int idView = Rconfig.getInstance().layout("core_component_term_condition");
        rootView = mInflater.inflate(idView, null, false);
        initView();
        return rootView;
    }

    protected void initView() {
        cbAgree = (CheckBox) findView("cb_agree");
        String textAgree = mCondition.getCheckText();
        cbAgree.setText(textAgree);
        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null != mCallBack) {
                    mCallBack.onAgree(isChecked);
                }
            }
        });

        imgExtend = (ImageView) findView("img_extend");
        TextView tvContent = (TextView) findView("tv_term");
        String title = mCondition.getTitle();
        String content = mCondition.getContent();
        StringBuilder builder = new StringBuilder();
        if (Utils.validateString(title)) {
            builder.append(title);
        }
        if (Utils.validateString(content)) {
            builder.append(content);
        }
        tvContent.setText(builder.toString());

        imgExtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCallBack) {
                    mCallBack.onOpenTermCondition();
                }
            }
        });

    }

    public boolean isAgree() {
        return cbAgree.isChecked();
    }

    public void setCallBack(TermConditionCallBack callBack) {
        mCallBack = callBack;
    }

}

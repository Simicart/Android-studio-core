package com.simicart.core.checkout.component;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 6/30/16.
 */
public class TermConditionComponent extends SimiComponent {

    protected CheckBox cbAgree;
    protected ImageView imgExtend;

    @Override
    public View createView() {

        int idView = Rconfig.getInstance().layout("core_component_term_condition");
        rootView = mInflater.inflate(idView, null, false);
        initView();
        return rootView;
    }

    protected void initView() {
        cbAgree = (CheckBox) findView("cb_agree");
        String textAgree = SimiTranslator.getInstance().translate("I agree");

        cbAgree.setText(textAgree);

        imgExtend = (ImageView) findView("img_extend");
        TextView tvContent = (TextView) findView("tv_term");
       // String title = StoreCheckoutEntity.getInstance().getTitleTerm();
        //tvContent.setText(title);

        imgExtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public boolean isAgree() {
        return cbAgree.isChecked();
    }


}

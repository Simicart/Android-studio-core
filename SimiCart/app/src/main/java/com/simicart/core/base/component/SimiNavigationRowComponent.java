package com.simicart.core.base.component;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.component.callback.NavigationRowCallBack;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import org.w3c.dom.Text;

/**
 * Created by frank on 23/08/2016.
 */
public class SimiNavigationRowComponent extends SimiRowComponent {

    protected ImageView imgExtend;
    protected TextView tvBody;
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
                if (null != mCallBack) {
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
        tvBody = (TextView) findView("tv_body");
        updateView();
    }

    @Override
    public void updateView() {
        if (null != mValue && Utils.validateString(String.valueOf(mValue))) {
            tvBody.setText(String.valueOf(mValue));
        }
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

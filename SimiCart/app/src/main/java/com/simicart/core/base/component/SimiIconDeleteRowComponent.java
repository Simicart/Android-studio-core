package com.simicart.core.base.component;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.component.callback.IconDeleteRowCallBack;

/**
 * Created by frank on 31/08/2016.
 */
public class SimiIconDeleteRowComponent extends SimiRowComponent {

    protected IconDeleteRowCallBack mCallBack;

    @Override
    protected void initView() {
        rootView = findLayout("core_component_icon_delete_row");
    }

    @Override
    protected void initBody() {
        TextView tvBody = (TextView) findView("tv_body");
        if (null != mValue) {
            tvBody.setText(String.valueOf(mValue));
        }

        ImageView imgDelete = (ImageView) findView("img_delete");
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCallBack) {
                    mCallBack.onDelete();
                }
            }
        });

    }

    public void setCallBack(IconDeleteRowCallBack callBack) {
        mCallBack = callBack;
    }
}

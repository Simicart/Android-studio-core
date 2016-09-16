package com.simicart.core.checkout.component;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.checkout.delegate.PaymentMethodCallBack;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;

/**
 * Created by frank on 11/05/2016.
 */
public class ItemPaymentMethodView extends SimiComponent {


    protected PaymentMethodEntity mPaymentEntity;
    protected PaymentMethodCallBack mCallBack;
    protected ImageView imgIcon;
    protected TextView tvSaved;
    protected boolean isChecked;


    public ItemPaymentMethodView(PaymentMethodEntity entity) {
        super();
        mPaymentEntity = entity;
    }


    public View createView() {
        int id = Rconfig.getInstance().layout("core_item_payment_method");
        rootView = mInflater.inflate(id, null, false);
        initView();
        return rootView;
    }

    protected void initView() {
        listenSelectAction();

        initIcon();

        initName();

        initSavedInfor();

        initEditIcon();

    }

    protected void listenSelectAction() {
        RelativeLayout rltItem = (RelativeLayout) findView("rlt_item");
        rltItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = true;
                String idIcon = "core_icon_option_selected";
                Drawable icon = AppColorConfig.getInstance().getIcon(idIcon);
                imgIcon.setImageDrawable(icon);
                mCallBack.onSelectItem(mPaymentEntity);
            }
        });
    }

    protected void initIcon() {
        imgIcon = (ImageView) findView("img_icon");
        String idIcon;
        if (mPaymentEntity.isSelected()) {
            isChecked = true;
            idIcon = "core_icon_option_selected";
        } else {
            idIcon = "core_icon_option_single";
        }
        Drawable icon = AppColorConfig.getInstance().getIcon(idIcon);
        imgIcon.setImageDrawable(icon);
    }

    protected void initName() {
        TextView tvName = (TextView) findView("tv_name");

        String name = mPaymentEntity.getTitle();
        if (Utils.validateString(name)) {
            tvName.setText(name);
        }

    }

    protected void initSavedInfor() {
        tvSaved = (TextView) findView("tv_saved");
        tvSaved.setVisibility(View.GONE);
    }

    protected void initEditIcon() {
        ImageView imgEdit = (ImageView) findView("img_edit");
        if (mPaymentEntity.getType() == PaymentMethodEntity.PAYMENTMETHODTYPE.CARD) {
            imgEdit.setVisibility(View.VISIBLE);
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.onEditAction(mPaymentEntity);
                }
            });
        } else {
            imgEdit.setVisibility(View.GONE);
        }

    }

    public void selectItem(boolean isSelect) {
        isChecked = isSelect;
        if (!isSelect) {
            String idIcon = "core_icon_option_single";
            Drawable icon = AppColorConfig.getInstance().getIcon(idIcon);
            imgIcon.setImageDrawable(icon);
        }
    }

    @Override
    public boolean isCompleteRequired() {
        return isChecked;
    }

    public void setCallBack(PaymentMethodCallBack callBack) {
        this.mCallBack = callBack;
    }

    public boolean isEqual(PaymentMethodEntity paymentEntity) {
        String currentMethod = mPaymentEntity.getPaymentMethod();

        if (null == paymentEntity) {
            return false;
        }

        String method = paymentEntity.getPaymentMethod();

        if (Utils.validateString(currentMethod) && Utils.validateString(method)) {
            if (currentMethod.equals(method)) {
                return true;
            }
        }

        return false;
    }

}

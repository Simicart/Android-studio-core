package com.simicart.core.checkout.component;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.checkout.delegate.ShippingMethodCallBack;
import com.simicart.core.checkout.entity.ShippingMethodEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;

/**
 * Created by frank on 11/05/2016.
 */
public class ItemShippingMethodView extends SimiComponent {

    protected ShippingMethodEntity mShippingEntity;
    protected ImageView imgIcon;
    protected boolean isChecked = false;
    protected ShippingMethodCallBack mCallBack;


    public ItemShippingMethodView(ShippingMethodEntity mShippingEntity) {
        super();
        this.mShippingEntity = mShippingEntity;
    }

    @Override
    public View createView() {
        rootView = findLayout("core_item_shipping_method");
        intView();

        return rootView;
    }

    protected void intView() {
        listentSelectAction();

        initIcon();

        initTitle();

        initContent();

        initPrice();


    }

    protected void listentSelectAction() {
        RelativeLayout rltItem = (RelativeLayout) findView("rlt_item");
        rltItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idIcon = "core_icon_option_selected";
                Drawable icon = AppColorConfig.getInstance().getIcon(idIcon);
                imgIcon.setImageDrawable(icon);
                mCallBack.onSelect(mShippingEntity);
            }
        });
    }

    protected void initIcon() {

        imgIcon = (ImageView) findView("img_icon");
        String idIcon;
        if (mShippingEntity.isSelected()) {
            isChecked = true;
            idIcon = "core_icon_option_selected";
        } else {
            idIcon = "core_icon_option_single";
        }
        Drawable icon = AppColorConfig.getInstance().getIcon(idIcon);
        imgIcon.setImageDrawable(icon);

    }

    protected void initTitle() {
        TextView tvTitle = (TextView) findView("tv_title");
        String title = mShippingEntity.getTitle();
        if (Utils.validateString(title)) {
            tvTitle.setText(title);
        }
    }

    protected void initContent() {
        TextView tvContent = (TextView) findView("tv_content");
        String name = mShippingEntity.getName();
        if (Utils.validateString(name)) {
            tvContent.setText(name);
        }

    }

    protected void initPrice() {
        String fee = mShippingEntity.getFee();
        if (Utils.validateString(fee)) {
            TextView tvFee = (TextView) findView("tv_price");
            String price = AppStoreConfig.getInstance().getPrice(fee);
            tvFee.setText(Html.fromHtml(price));
        }
    }

    public void selectItem(boolean isSelected) {
        isChecked = isSelected;
        if (!isSelected) {
            isChecked = true;
            String idIcon = "core_icon_option_single";
            Drawable icon = AppColorConfig.getInstance().getIcon(idIcon);
            imgIcon.setImageDrawable(icon);
        }
    }

    public boolean isEqual(ShippingMethodEntity shippingEntity) {

        if (null == shippingEntity) {
            return false;
        }

        String id = shippingEntity.getID();

        String currentID = mShippingEntity.getID();

        if (Utils.validateString(id) && Utils.validateString(currentID)) {
            if (id.equals(currentID)) {
                return true;
            }
        }


        return false;
    }

    @Override
    public boolean isCompleteRequired() {
        return isChecked;
    }

    public void setCallBack(ShippingMethodCallBack callBack) {
        this.mCallBack = callBack;
    }

}

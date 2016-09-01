package com.simicart.core.checkout.component;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.delegate.AddressComponentCallback;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.customer.entity.AddressEntity;

/**
 * Created by frank on 10/05/2016.
 */
public class AddressCheckoutComponent extends SimiComponent {

    public static final int BILLING_TYPE = 1;
    public static final int SHIPPING_TYPE = 2;
    public static final int ORDER_DETAIL_TYPE = 2;

    protected int mType;
    protected AddressEntity mAddress;
    protected AddressComponentCallback mCallBack;
    protected int mColorIcon;


    public AddressCheckoutComponent(int type, AddressEntity address) {
        super();
        mType = type;
        mAddress = address;
        mColorIcon = AppColorConfig.getInstance().getIconColor();
    }

    public View createView() {

        if (null == mAddress) {
            return null;
        }

        rootView = findLayout("core_component_address_checkout");
        initView();
        return rootView;
    }

    protected void initView() {
        // tittle
        initTitle();

        // edit
        initEditAction();

        // name
        initName();

        // street
        initStreet();

        // phone
        initPhone();

        // email
        initEmail();

    }

    protected void initTitle() {
        String title = "";
        if (mType == BILLING_TYPE) {
            title = "Billing Address";
        } else if(mType == SHIPPING_TYPE) {
            title = "Shipping Address";
        }

        if(!title.equals("")) {
            title = SimiTranslator.getInstance().translate(title);
            TextView tvTitle = (TextView) findView("tv_title");
            int bgColor = AppColorConfig.getInstance().getSectionColor();
            tvTitle.setBackgroundColor(bgColor);
            int textColor = AppColorConfig.getInstance().getContentColor();
            tvTitle.setTextColor(textColor);
            tvTitle.setText(title);
        }
    }

    protected void initEditAction() {
        ImageView imgEdit = (ImageView) findView("img_edit");
        Drawable icEdit = AppColorConfig.getInstance().getIcon("core_icon_edit");
        imgEdit.setBackground(icEdit);
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCallBack) {
                    mCallBack.onSelect();
                }
            }
        });
        if(mType == ORDER_DETAIL_TYPE) {
            imgEdit.setVisibility(View.GONE);
        }
    }

    protected void initName() {
        ImageView imgName = (ImageView) findView("img_name");
        Drawable icName = AppColorConfig.getInstance().getIcon("ic_acc_profile");
        imgName.setBackground(icName);
        TextView tvName = (TextView) findView("tv_name");
        String name = mAddress.getName();

        if (Utils.validateString(name)) {
            tvName.setText(name);
        } else {
            LinearLayout llName = (LinearLayout) findView("ll_name");
            llName.setVisibility(View.GONE);
        }
    }

    protected void initStreet() {
        ImageView imgStreet = (ImageView) findView("img_street");
        Drawable icLocation = AppColorConfig.getInstance().getIcon("core_bg_location");
        imgStreet.setBackground(icLocation);
        TextView tvStreet = (TextView) findView("tv_street");
        String street = mAddress.getStreet();
        if (Utils.validateString(street)) {
            tvStreet.setText(street);
        } else {
            LinearLayout llStreet = (LinearLayout) findView("ll_street");
            llStreet.setVisibility(View.GONE);
        }
    }

    protected void initPhone() {
        ImageView imgPhone = (ImageView) findView("img_phone");
        Drawable icPhone = AppColorConfig.getInstance().getIcon("core_bg_call");
        imgPhone.setBackground(icPhone);
        TextView tvPhone = (TextView) findView("tv_phone");
        String phone = mAddress.getPhone();
        if (Utils.validateString(phone)) {
            tvPhone.setText(phone);
        } else {
            LinearLayout llPhone = (LinearLayout) findView("ll_phone");
            llPhone.setVisibility(View.GONE);
        }
    }

    protected void initEmail() {
        ImageView imgEmail = (ImageView) findView("img_email");
        Drawable icEmail = AppColorConfig.getInstance().getIcon("core_icon_email");
        imgEmail.setImageDrawable(icEmail);
        TextView tvEmail = (TextView) findView("tv_email");
        String email = mAddress.getEmail();
        if (Utils.validateString(email)) {
            tvEmail.setText(email);
        } else {
            LinearLayout llEmail = (LinearLayout) findView("ll_email");
            llEmail.setVisibility(View.GONE);
        }
    }


    public AddressEntity getAddress() {
        return mAddress;
    }

    public void setAddress(AddressEntity mAddress) {
        this.mAddress = mAddress;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public void setCallBack(AddressComponentCallback callback) {
        mCallBack = callback;
    }
}

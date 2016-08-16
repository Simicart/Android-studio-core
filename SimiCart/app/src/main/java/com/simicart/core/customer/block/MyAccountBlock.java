package com.simicart.core.customer.block;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.material.LayoutRipple;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAccountBlock extends SimiBlock {
    protected TextView lable_myAccount;
    protected LayoutRipple rlt_profile;
    protected TextView lb_profile;
    protected LayoutRipple rlt_addressBook;
    protected TextView lb_addressBook;
    protected LayoutRipple rlt_orderHistory;
    protected TextView lb_orderHistory;
    protected LayoutRipple rlt_signOut;
    protected TextView lb_logout;

    public void setProfileClick(OnClickListener click) {
        rlt_profile.setOnClickListener(click);
    }

    public void setAddressBookClick(OnClickListener click) {
        rlt_addressBook.setOnClickListener(click);
    }

    public void setOrderHistory(OnClickListener click) {
        rlt_orderHistory.setOnClickListener(click);
    }

    public void setSignOutClick(OnClickListener click) {
        rlt_signOut.setOnClickListener(click);
    }

    public MyAccountBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        rlt_profile = (LayoutRipple) mView.findViewById(Rconfig.getInstance()
                .id("rl_profile"));
        lb_profile = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "lb_profile"));
        lb_profile.setText(SimiTranslator.newInstance().translate("Profile"));
        lb_profile.setTextColor(AppColorConfig.getInstance().getContentColor());

        rlt_addressBook = (LayoutRipple) mView.findViewById(Rconfig
                .getInstance().id("rl_addressBook"));
        lb_addressBook = (TextView) mView.findViewById(Rconfig.getInstance()
                .id("lb_addressBook"));
        lb_addressBook.setTextColor(AppColorConfig.getInstance().getContentColor());
        lb_addressBook.setText(SimiTranslator.newInstance().translate("Address Book"));

        rlt_orderHistory = (LayoutRipple) mView.findViewById(Rconfig
                .getInstance().id("rl_orderHistory"));
        lb_orderHistory = (TextView) mView.findViewById(Rconfig.getInstance()
                .id("lb_orderHistory"));
        lb_orderHistory.setText(SimiTranslator.newInstance().translate("Order History"));
        lb_orderHistory.setTextColor(AppColorConfig.getInstance().getContentColor());

        rlt_signOut = (LayoutRipple) mView.findViewById(Rconfig.getInstance()
                .id("rl_logout"));
        lb_logout = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "lb_logout"));
        lb_logout.setText(SimiTranslator.newInstance().translate("Sign Out"));
        lb_logout.setTextColor(AppColorConfig.getInstance().getContentColor());

        LinearLayout ll_space = (LinearLayout) mView.findViewById(Rconfig
                .getInstance().id("ll_space"));
        ll_space.setBackgroundColor(AppColorConfig.getInstance().getKeyColor());

        ImageView im_extend_profile = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("im_extend_profile"));
        ImageView im_extend_orderhis = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("im_extend_orderhis"));
        ImageView im_extend_address = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("im_extend_address"));
        Drawable icon = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_extend"));
        icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        im_extend_profile.setImageDrawable(icon);
        im_extend_orderhis.setImageDrawable(icon);
        im_extend_address.setImageDrawable(icon);

        ImageView im_profile = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("im_profile"));
        Drawable ic_acc_profile = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_acc_profile"));
        ic_acc_profile.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        im_profile.setImageDrawable(ic_acc_profile);

        ImageView im_addressbook = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("im_addressbook"));
        Drawable ic_acc_address = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_acc_address"));
        ic_acc_address.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        im_addressbook.setImageDrawable(ic_acc_address);

        ImageView im_order_his = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("im_order_his"));
        Drawable ic_acc_history = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_acc_history"));
        ic_acc_history.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        im_order_his.setImageDrawable(ic_acc_history);

        ImageView im_sign_out = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("im_sign_out"));
        Drawable ic_acc_logout = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_acc_logout"));
        ic_acc_logout.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        im_sign_out.setImageDrawable(ic_acc_logout);

        View v_profile = (View) mView.findViewById(Rconfig.getInstance().id(
                "v_profile"));
        View v_address = (View) mView.findViewById(Rconfig.getInstance().id(
                "v_address"));
        View v_orderhis = (View) mView.findViewById(Rconfig.getInstance().id(
                "v_orderhis"));
        View v_signout = (View) mView.findViewById(Rconfig.getInstance().id(
                "v_signout"));
        v_profile.setBackgroundColor(AppColorConfig.getInstance().getLineColor());
        v_address.setBackgroundColor(AppColorConfig.getInstance().getLineColor());
        v_orderhis.setBackgroundColor(AppColorConfig.getInstance().getLineColor());
        v_signout.setBackgroundColor(AppColorConfig.getInstance().getLineColor());

        if (DataLocal.isLanguageRTL) {
            lb_profile.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            lb_addressBook.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            lb_orderHistory.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            lb_logout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        }
    }
}

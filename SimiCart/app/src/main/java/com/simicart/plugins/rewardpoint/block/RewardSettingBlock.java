package com.simicart.plugins.rewardpoint.block;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.delegate.RewardSettingDelegate;

/**
 * Created by Martial on 8/29/2016.
 */
public class RewardSettingBlock extends SimiBlock implements RewardSettingDelegate {

    protected TextView txt_email;
    protected TextView txt_pointupdate;
    protected TextView txt_subscribe_updatepoint;
    protected TextView txt_pointtransaction;
    protected TextView txt_notification;
    protected Switch switch_point_update;
    protected Switch switch_point_transaction;
    protected AppCompatButton btn_save;
    protected boolean is_notification;
    protected boolean expire_notification;

    public RewardSettingBlock(View view, Context context) {
        super(view, context);
    }

    public void onButtonSaveClick(View.OnClickListener listener) {
        btn_save.setOnClickListener(listener);
    }

    @Override
    public void initView() {
        switch_point_update = (Switch) mView.findViewById(Rconfig.getInstance()
                .id("switch_pointupdate"));
        switch_point_transaction = (Switch) mView.findViewById(Rconfig
                .getInstance().id("switch_pointtransaction"));
        btn_save = (AppCompatButton) mView.findViewById(Rconfig.getInstance().id(
                "btn_save"));
        switch_point_update.setChecked(is_notification);
        switch_point_transaction.setChecked(expire_notification);

        txt_email = (TextView) mView.findViewById(Rconfig.getInstance().id("txt_rewardsetting_email"));
        txt_pointupdate = (TextView) mView.findViewById(Rconfig.getInstance().id("txt_pointupdate"));
        txt_subscribe_updatepoint = (TextView) mView.findViewById(Rconfig.getInstance().id("txt_rewardsetting_subscribe_updatepoint"));
        txt_pointtransaction = (TextView) mView.findViewById(Rconfig.getInstance().id("txt_pointtransaction"));
        txt_notification = (TextView) mView.findViewById(Rconfig.getInstance().id("txt_rewardsetting_subscribe_notification"));
        txt_email.setText(SimiTranslator.getInstance().translate("EMAIL SUBCRIPTIONS"));
        txt_pointupdate.setText(SimiTranslator.getInstance().translate("Point Balance Update"));
        txt_subscribe_updatepoint.setText(SimiTranslator.getInstance().translate("Subscribe to receive updates on your point balance"));
        txt_pointtransaction.setText(SimiTranslator.getInstance().translate("Expired Point Transaction"));
        txt_notification.setText(SimiTranslator.getInstance().translate("Subscribe to receive notification of expiring points in advance"));

        btn_save.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
        btn_save.setTextColor(AppColorConfig.getInstance().getButtonTextColor());
        btn_save.setText(SimiTranslator.getInstance().translate("Save"));
    }

    @Override
    public void drawView(SimiCollection collection) {
        super.drawView(collection);
    }

    public void setExpireNotification(boolean expire_notification) {
        this.expire_notification = expire_notification;
    }

    public void setIsNotification(boolean is_notification) {
        this.is_notification = is_notification;
    }

    @Override
    public boolean isPointUpdate() {
        return switch_point_update.isChecked();
    }

    @Override
    public boolean isPointTransaction() {
        return switch_point_transaction.isChecked();
    }
}

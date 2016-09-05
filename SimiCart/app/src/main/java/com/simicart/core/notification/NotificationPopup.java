package com.simicart.core.notification;

import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

import static com.simicart.core.config.Rconfig.*;

/**
 * Created by MSI on 05/09/2016.
 */
public class NotificationPopup {

    protected NotificationEntity mNotificationEntity;
    protected View rootView;
    protected Context mContext;

    public NotificationPopup(NotificationEntity notificationEntity) {
        mNotificationEntity = notificationEntity;
        mContext = SimiManager.getIntance().getCurrentActivity();
    }

    public View createView() {

    return    initBody();

//        TextView tv = (TextView) alertboxDowload
//                .findViewById(android.R.id.title);
//        tv.setMaxLines(2);
//        tv.setEllipsize(TextUtils.TruncateAt.END);
//        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


//        String title = mNotificationEntity.getTitle();
//        mAlert.setTitle(SimiTranslator.getInstance().translate(
//                title));
//        mAlert.setCancelable(false);
//
//        mAlert.show();
    }

    protected View initBody() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        rootView = inflater.inflate(
                getInstance().layout("core_notification_layout"), null);

        createImageNotification();

        createMessgeNotification();

        createClose();

        createShow();

        return rootView;

    }

    protected void createImageNotification() {
        ImageView im_notification = (ImageView) findView("im_notification");
        String urlImage = mNotificationEntity.getImageUrl();
        if (Utils.validateString(urlImage)) {
            im_notification.setVisibility(View.VISIBLE);
            DrawableManager.fetchDrawableOnThread(urlImage,
                    im_notification);
        } else {
            im_notification.setVisibility(View.GONE);
        }
    }

    protected void createMessgeNotification() {
        TextView tv_notification = (TextView) findView("tv_notification");

        String message = mNotificationEntity.getmMessage();
        String urlImage = mNotificationEntity.getImageUrl();
        if (Utils.validateString(message)) {
            tv_notification.setVisibility(View.VISIBLE);
            if (Utils.validateString(urlImage)) {
                if (message.length() > 253)
                    message = message.substring(0, 250) + "...";
            } else {
                if (message.length() > 503)
                    message = message.substring(0, 500) + "...";
            }
            tv_notification.setText(message);
        } else {
            tv_notification.setVisibility(View.GONE);
        }
    }

    protected void createClose() {
        TextView tv_close = (TextView) findView("tv_close");
        tv_close.setText(SimiTranslator.getInstance().translate("CLOSE"));
        tv_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                mAlert.dismiss();
            }
        });
    }

    protected void createShow() {
        TextView tv_show = (TextView) findView("tv_show");
        tv_show.setText(SimiTranslator.getInstance().translate("SHOW"));
        tv_show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                mAlert.dismiss();
                openNotification();
            }
        });
    }

    protected void openNotification() {

    }

    protected View findView(String id) {
        int idView = Rconfig.getInstance().id(id);
        return rootView.findViewById(idView);
    }

}

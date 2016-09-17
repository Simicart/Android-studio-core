package com.simicart.core.notification;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.Rconfig;

import java.util.HashMap;

import static com.simicart.core.config.Rconfig.getInstance;

/**
 * Created by frank on 05/09/2016.
 */
public class NotificationPopup {

    protected NotificationEntity mNotificationEntity;
    protected NotificationCallBack mCallBack;
    protected View rootView;
    protected Context mContext;


    public NotificationPopup(NotificationEntity notificationEntity) {
        mNotificationEntity = notificationEntity;
        mContext = SimiManager.getIntance().getCurrentActivity();
    }

    public View createView() {
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
                if (null != mCallBack) {
                    mCallBack.close();
                }
            }
        });
    }

    protected void createShow() {
        TextView tv_show = (TextView) findView("tv_show");
        tv_show.setText(SimiTranslator.getInstance().translate("SHOW"));
        tv_show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mCallBack && mCallBack.show()) {

                } else {
                    openNotification();
                }
            }
        });
    }

    protected void openNotification() {
        NotificationEntity.TYPE_OPEN type = mNotificationEntity.getType();
        if (type == NotificationEntity.TYPE_OPEN.PRODUCT_DETAIL) {
            openProductDetail();
        } else if (type == NotificationEntity.TYPE_OPEN.CATEGORY) {
            openCategory();
        } else {
            // web view
            openWebview();
        }
    }

    protected void openProductDetail() {
        String productId = mNotificationEntity.getProductID();
        if (Utils.validateString(productId)) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put(KeyData.PRODUCT_DETAIL.PRODUCT_ID, productId);
            SimiManager.getIntance().openProductDetail(hm);
        }

    }

    protected void openCategory() {
        if (mNotificationEntity.hasChild()) {
            // open category
            String cateId = mNotificationEntity.getCategoryID();
            String cateName = mNotificationEntity.getmCategoryName();
            HashMap<String, Object> hm = new HashMap<>();
            if (Utils.validateString(cateId)) {
                hm.put(KeyData.CATEGORY.CATEGORY_ID, cateId);
            }
            if (Utils.validateString(cateName)) {
                hm.put(KeyData.CATEGORY.CATEGORY_NAME, cateName);
            }
            SimiManager.getIntance().openCategory(hm);

        } else {
            // open category detail
            String cateId = mNotificationEntity.getCategoryID();
            String cateName = mNotificationEntity.getmCategoryName();
            HashMap<String, Object> hm = new HashMap<>();
            if (Utils.validateString(cateId)) {
                hm.put(KeyData.CATEGORY_DETAIL.CATE_ID, cateId);
            }
            if (Utils.validateString(cateName)) {
                hm.put(KeyData.CATEGORY_DETAIL.CATE_NAME, cateName);
            }
            hm.put(KeyData.CATEGORY_DETAIL.TYPE, ValueData.CATEGORY_DETAIL.CATE);
            SimiManager.getIntance().openCategoryDetail(hm);
        }

    }

    protected void openWebview() {
        Log.e("NotificationPopup ", "-----------------> OPEN WEB VIEW ");
        String url = mNotificationEntity.getUrl();
        Log.e("NotificationPopup ", "-----------------> OPEN WEB VIEW URL " + url);
        if (Utils.validateString(url)) {
            if (!url.contains("http")) {
                url = "http://" + url;
            }
            Log.e("NotificationPopup ", "-----------------> URL " + url);
            HashMap<String, Object> hm = new HashMap<>();
            hm.put(KeyData.WEBVIEW_PAGE.URL, url);
            SimiManager.getIntance().openWebviewPage(hm);
        }
    }

    protected View findView(String id) {
        int idView = Rconfig.getInstance().id(id);
        return rootView.findViewById(idView);
    }

    public void setCallBack(NotificationCallBack callBack) {
        mCallBack = callBack;
    }

}

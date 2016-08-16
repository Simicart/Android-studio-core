package com.simicart.core.splashscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.shortcutbadger.ShortcutBadgeException;
import com.simicart.core.shortcutbadger.ShortcutBadger;
import com.simicart.core.splashscreen.block.SplashBlock;
import com.simicart.core.splashscreen.controller.SplashController;
import com.simicart.core.splashscreen.delegate.SplashDelegate;
import com.simicart.core.splashscreen.entity.DeepLinkEntity;
import com.simicart.core.splashscreen.model.DeepLinkModel;

public class SplashActivity extends Activity implements SplashDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        SimiManager.getIntance().setCurrentActivity(this);
        int idView = Rconfig.getInstance().layout("");

        setContentView(Rconfig.getInstance().getId(context,
                "core_splash_screen", "layout"));
        DataLocal.isTablet = Utils.isTablet(context);
        if (DataLocal.isTablet) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        View view = findViewById(Rconfig.getInstance().getId(
                getApplicationContext(), "core_splash_screen", "id"));

        // need to dispatch event for Google Analytics

        SplashBlock block = new SplashBlock(view, context);
        block.initView();
        SplashController model = new SplashController(this, context);
        model.create();

        // Update badge
        try {
            ShortcutBadger.setBadge(context, 0);
        } catch (ShortcutBadgeException e) {

        }

        onOpenDeepLink();
    }

    private void onOpenDeepLink() {
        Intent intent = getIntent();
        Uri link = intent.getData();
        if (null != link){
            String deepLink = link.toString();
            if (deepLink != null && deepLink.contains(".html")) {
                deepLink = deepLink.substring(deepLink.lastIndexOf("/") + 1,
                        deepLink.lastIndexOf(".html"));
                if (Utils.validateString(deepLink)) {
                    DeepLinkEntity.getInstance().setName(deepLink);
                    getDeepLinkItem();
                }
            }
        }
    }


    private void getDeepLinkItem() {
        final DeepLinkModel model = new DeepLinkModel();
        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {

            }
        });

        model.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {

            }
        });

        model.addBody("link", DeepLinkEntity.getInstance().getName());

        model.request();
    }

    @Override
    public void creatMain() {
        SimiManager.getIntance().toMainActivity();
    }

    @Override
    public void onBackPressed() {
    }

}

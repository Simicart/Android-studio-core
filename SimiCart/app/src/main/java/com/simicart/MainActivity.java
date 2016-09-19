package com.simicart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;

import com.magestore.simicart.R;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.FontsOverride;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.SCLocation;
import com.simicart.core.common.SCLocationCallBack;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.controller.AutoSignInController;
import com.simicart.core.menutop.fragment.MenuTopFragment;
import com.simicart.core.notification.NotificationCallBack;
import com.simicart.core.notification.NotificationEntity;
import com.simicart.core.notification.NotificationPopup;
import com.simicart.core.notification.SCRegistrationIntentService;
import com.simicart.core.shortcutbadger.ShortcutBadgeException;
import com.simicart.core.shortcutbadger.ShortcutBadger;
import com.simicart.core.slidemenu.fragment.SlideMenuFragment;

import java.util.HashMap;
import java.util.List;

@SuppressLint("DefaultLocale")
public class MainActivity extends FragmentActivity {

    public static boolean isActive;
    protected SCLocation scLocation;
    protected AlertDialog alertDialogNotification = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimiManager.getIntance().setCurrentActivity(this);
        setContentView(R.layout.core_main_activity);

        scLocation = new SCLocation(this);
        scLocation.setCallBack(new SCLocationCallBack() {
            @Override
            public void getLocationComplete(boolean isSuccess) {
                registerNotification();
            }
        });
        scLocation.connect();

        // DataPreferences.init(this);
        if (DataPreferences.isSignInComplete()) {
            autoSignin();
        }
        // checkTheme();
        SimiManager.getIntance().setManager(getSupportFragmentManager());

        // need to dispatch event for Google Analytic

        if (DataLocal.isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        SlideMenuFragment slideMenuFragment = (SlideMenuFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);

        slideMenuFragment.setup(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        changeFont();

        String qtyCart = DataLocal.qtyCartAuto;
        if (Utils.validateString(qtyCart)) {
            SimiManager.getIntance().onUpdateCartQty(qtyCart);
        }

        // check whether have any notifications
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            NotificationEntity notificationData = (NotificationEntity) extras
                    .getSerializable("NOTIFICATION_DATA");
            showNotification(notificationData);
        }


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MenuTopFragment fragment = MenuTopFragment
                .newInstance(slideMenuFragment);
        ft.replace(Rconfig.getInstance().id("menu_top"), fragment);
        ft.commit();

        isActive = true;

    }


    private void autoSignin() {
        AutoSignInController controller = new AutoSignInController();
        controller.onStart();
    }

    protected void registerNotification() {
        Intent intent = new Intent(this, SCRegistrationIntentService.class);
        startService(intent);

        // local notification
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SimiData simiData = intent.getParcelableExtra("data");
                NotificationEntity notificationEntity = (NotificationEntity) simiData.getData().get("notification_entity");
                showNotification(notificationEntity);
            }
        };

        IntentFilter notificationFilter = new IntentFilter("com.simicart.localnotification");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, notificationFilter);
    }


    protected void showNotification(final NotificationEntity notificationEntity) {

        if (null == notificationEntity) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final AlertDialog.Builder alertBox = new AlertDialog.Builder(
                        SimiManager.getIntance().getCurrentActivity());

                String title = notificationEntity.getTitle();
                if (Utils.validateString(title)) {
                    alertBox.setTitle(title);
                }


                NotificationPopup notificationPopup = new NotificationPopup(notificationEntity);
                notificationPopup.setCallBack(new NotificationCallBack() {
                    @Override
                    public boolean close() {
                        alertDialogNotification.dismiss();
                        return true;
                    }

                    @Override
                    public boolean show() {
                        alertDialogNotification.dismiss();
                        return false;
                    }
                });
                View contentView = notificationPopup.createView();


                alertBox.setView(contentView);
                alertDialogNotification = alertBox.show();
            }
        });

    }


    @Override
    protected void onResume() {
        isActive = true;
        SimiManager.getIntance().setCurrentActivity(this);
        SimiManager.getIntance().setManager(getSupportFragmentManager());
        // Update badge
        try {
            ShortcutBadger.setBadge(this, 0);
        } catch (ShortcutBadgeException e) {
        }
        super.onResume();
    }


    private void changeFont() {
        FontsOverride.setDefaultFont(this, "DEFAULT", Config.getInstance()
                .getFontCustom());
        FontsOverride.setDefaultFont(this, "NORMAL", Config.getInstance()
                .getFontCustom());
        FontsOverride.setDefaultFont(this, "MONOSPACE", Config.getInstance()
                .getFontCustom());
        FontsOverride.setDefaultFont(this, "SERIF", Config.getInstance()
                .getFontCustom());
    }


    @Override
    public void onBackPressed() {

        int count = SimiManager.getIntance().getManager()
                .getBackStackEntryCount();
        if (count == 1) {
            showConfirmExitApp();
        } else {
            super.onBackPressed();
        }
    }

    private void showConfirmExitApp() {
        AlertDialog.Builder alertboxDowload = new AlertDialog.Builder(
                this);
        alertboxDowload.setTitle(SimiTranslator.getInstance().translate("CLOSE APPLICATION").toUpperCase());
        alertboxDowload.setMessage(SimiTranslator.getInstance().translate("Are you sure you want to exit?"));
        alertboxDowload.setCancelable(false);
        alertboxDowload.setPositiveButton(SimiTranslator.getInstance().translate("OK").toUpperCase(),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        SimiManager.getIntance().getManager()
                                .popBackStack();
                        android.os.Process
                                .killProcess(android.os.Process
                                        .myPid());
                        finish();
                    }
                });
        alertboxDowload.setNegativeButton(SimiTranslator.getInstance().translate
                        ("CANCEL").toUpperCase(),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                    }
                });
        alertboxDowload.show();
    }

    @Override
    protected void onPause() {
        isActive = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scLocation.disconnect();
    }

    @Override
    protected void onDestroy() {
        isActive = false;
        System.gc();
        Runtime.getRuntime().freeMemory();
        finish();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCLocation.LOCATION_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                scLocation.updateLocation();
            } else {
                registerNotification();
            }
        }
        {
            HashMap<String, Object> hmData = new HashMap<>();
            hmData.put(KeyData.BAR_CODE.INTENT, data);
            hmData.put(KeyData.BAR_CODE.REQUEST_CODE, requestCode);
            hmData.put(KeyData.BAR_CODE.RESULT_CODE, resultCode);
            SimiEvent.dispatchEvent(KeyEvent.BAR_CODE.BAR_CODE_ON_RESULT, hmData);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            int size = fragments.size();
            Fragment fragment = fragments.get(size - 1);
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}

package com.simicart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.magestore.simicart.R;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.FontsOverride;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.controller.AutoSignInController;
import com.simicart.core.menutop.fragment.MenuTopFragment;
import com.simicart.core.notification.NotificationEntity;
import com.simicart.core.notification.NotificationPopup;
import com.simicart.core.notification.SCRegistrationIntentService;
import com.simicart.core.shortcutbadger.ShortcutBadgeException;
import com.simicart.core.shortcutbadger.ShortcutBadger;
import com.simicart.core.slidemenu.fragment.SlideMenuFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("DefaultLocale")
public class MainActivity extends FragmentActivity {

    private SlideMenuFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimiManager.getIntance().setCurrentActivity(this);
        setContentView(R.layout.core_main_activity);

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
        mNavigationDrawerFragment = (SlideMenuFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setup(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        changeFont();

        String qtyCart = DataLocal.qtyCartAuto;
        if (Utils.validateString(qtyCart)) {
            SimiManager.getIntance().onUpdateCartQty(qtyCart);
        }

        registerNotification();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MenuTopFragment fragment = MenuTopFragment
                .newInstance(mNavigationDrawerFragment);
        ft.replace(Rconfig.getInstance().id("menu_top"), fragment);
        ft.commit();

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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NotificationPopup notificationPopup = new NotificationPopup(notificationEntity);
                View contentView = notificationPopup.createView();

                AlertDialog.Builder alertBox = new AlertDialog.Builder(
                        SimiManager.getIntance().getCurrentActivity());
                alertBox.setView(contentView);
                alertBox.show();
            }
        });

    }


    @Override
    protected void onResume() {
        SimiManager.getIntance().setCurrentActivity(this);
        SimiManager.getIntance().setManager(getSupportFragmentManager());
        // Update badge
        try {
            ShortcutBadger.setBadge(this, 0);
        } catch (ShortcutBadgeException e) {
        }
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
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
    protected void onDestroy() {
        System.gc();
        Runtime.getRuntime().freeMemory();
        finish();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent("com.simicart.leftmenu.onactivityresult.resultbarcode");
        Bundle bundle = new Bundle();
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(KeyData.BAR_CODE.INTENT, data);
        hmData.put(KeyData.BAR_CODE.REQUEST_CODE, requestCode);
        hmData.put(KeyData.BAR_CODE.RESULT_CODE, resultCode);
        bundle.putParcelable(Constants.ENTITY, new SimiData(hmData));
        intent.putExtra(Constants.DATA, bundle);
        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcastSync(intent);

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

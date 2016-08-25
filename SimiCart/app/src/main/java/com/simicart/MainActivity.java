package com.simicart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.magestore.simicart.R;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.FontsOverride;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.controller.AutoSignInController;
import com.simicart.core.menutop.fragment.MenuTopFragment;
import com.simicart.core.shortcutbadger.ShortcutBadgeException;
import com.simicart.core.shortcutbadger.ShortcutBadger;
import com.simicart.core.slidemenu.fragment.SlideMenuFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
public class MainActivity extends FragmentActivity {
    public final static int PAUSE = 2;
    public final static int RESUME = 1;

    private SlideMenuFragment mNavigationDrawerFragment;
//    private NotificationController notification;
    public static int state = 0;

    public static boolean mCheckToDetailAfterScan = false;
    public static int mBackEntryCountDetail = 0;
    public static boolean checkBackScan = false;

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



        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MenuTopFragment fragment = MenuTopFragment
                .newInstance(mNavigationDrawerFragment);
        ft.replace(Rconfig.getInstance().id("menu_top"), fragment);
        ft.commit();
        // ViewServer.get(this).addWindow(this);
    }


    private void autoSignin() {
        AutoSignInController controller = new AutoSignInController();
        controller.onStart();
    }


    @Override
    protected void onPause() {
        state = PAUSE;
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
//        if ((null == Config.getInstance().getCurrencyCode())
//                || (Config.getInstance().getCurrencyCode().equals("null"))) {
//            DataLocal.mSharedPre = getApplicationContext()
//                    .getSharedPreferences(DataLocal.NAME_REFERENCE,
//                            Context.MODE_PRIVATE);
//            SimiManager.getIntance().changeStoreView();
//        }
        state = RESUME;
        SimiManager.getIntance().setCurrentActivity(this);
        SimiManager.getIntance().setManager(getSupportFragmentManager());
        // Update badge
        try {
            ShortcutBadger.setBadge(this, 0);
        } catch (ShortcutBadgeException e) {
        }
        super.onResume();
        // ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
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

//        EventBlock eventBlock = new EventBlock();
//        eventBlock
//                .dispatchEvent("com.simicart.leftmenu.mainactivity.onbackpress.checkentrycount");


        int count = SimiManager.getIntance().getManager()
                .getBackStackEntryCount();
        if (count > 0) {
            try {
                if (count == 1) {
                    if (checkBackScan == true) {
//                        checkBackScan = false;
//                        eventBlock
//                                .dispatchEvent("com.simicart.leftmenu.mainactivity.onbackpress.backtoscan");
                    } else {
                        // out app
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

                } else {
                    try {
                        if (count > 2) {
                            List<Fragment> list = SimiManager.getIntance()
                                    .getManager().getFragments();
                            Fragment fragment = SimiManager.getIntance()
                                    .getManager().getFragments()
                                    .get(list.size() - 1);
                            if (fragment != null) {
                                int tag = fragment.getTargetRequestCode();
                                if (tag == ConfigCheckout.TARGET_REVIEWORDER) {
                                    SimiManager.getIntance()
                                            .backToHomeFragment();
                                } else {
                                    SimiManager.getIntance().getManager()
                                            .popBackStack();
                                }
                            } else {
                                SimiManager.getIntance().getManager()
                                        .popBackStack();
                            }
                        } else {
                            SimiManager.getIntance().getManager()
                                    .popBackStack();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    protected void onDestroy() {
//        notification.destroy();

        System.gc();
        Runtime.getRuntime().freeMemory();

        super.onDestroy();
        // ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // event form signin
//        EventController dispatch = new EventController();
//        dispatch.dispatchEvent("com.simicart.MainActivity.onActivityResult",
//                this);

//        EventBlock block = new EventBlock();
//        if (requestCode == 64209) {
//            block = new EventBlock();
//            block.dispatchEvent("com.simicart.core.catalog.product.block.ProductBlock.resultfacebook.checkresultcode");
//        }

        if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String text = result.get(0);
                if (Utils.validateString(text)) {
                    try {
//                        SimiEntity entity = new SimiEntity();
//                        JSONObject object = new JSONObject();
//                        object.put("textvoice", text);
//                        entity.setJSONObject(object);
//                        CacheBlock cacheBlock = new CacheBlock();
//                        cacheBlock.setSimiEntity(entity);
//                        block.dispatchEvent(
//                                "com.simicart.mainactivity.onactivityresult",
//                                cacheBlock);
                    } catch (Exception e) {
                    }
                }
            }
        }

//        EventActivity eventActivity = new EventActivity();
//        CacheActivity cacheActivity = new CacheActivity();
//        cacheActivity.setData(data);
//        cacheActivity.setRequestCode(requestCode);
//        cacheActivity.setResultCode(resultCode);
//        eventActivity.dispatchEvent("com.simicart.leftmenu.mainactivity.onactivityresult.resultbarcode", cacheActivity);


    }

//    public void nextActivity(View v) {
//        Intent intent = new Intent(this, getClass());
//        intent.putExtra("counter", 1);
//        startActivity(intent);
//    }

}

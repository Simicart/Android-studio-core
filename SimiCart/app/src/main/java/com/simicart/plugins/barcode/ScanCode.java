package com.simicart.plugins.barcode;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.simicart.MainActivity;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.core.slidemenu.entity.ItemNavigation.TypeItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ScanCode {

    public final String QR_BAR_CODE = "Scan Now";

    protected Context mContext;
    protected ArrayList<ItemNavigation> mItems;
    protected ScanCodeModel mModel;

    public ScanCode() {
        mContext = SimiManager.getIntance().getCurrentActivity();

        // register event: add navigation item to slide menu
        IntentFilter addItemFilter = new IntentFilter(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_MORE);
        BroadcastReceiver addItemReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable("entity");
                mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
                ItemNavigation item = new ItemNavigation();
                item.setType(TypeItem.NORMAL);
                item.setName(SimiTranslator.getInstance().translate(QR_BAR_CODE));
                int id_icon = Rconfig.getInstance().drawable("ic_barcode");
                Drawable icon = mContext.getResources().getDrawable(id_icon);
                icon.setColorFilter(Color.parseColor("#ffffff"),
                        PorterDuff.Mode.SRC_ATOP);
                item.setIcon(icon);
                mItems.add(item);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(addItemReceiver, addItemFilter);

        // register event: click item from left menu
        IntentFilter onClickItemFilter = new IntentFilter("com.simicart.menuleft.onnavigate.clickitem");
        BroadcastReceiver onClickItemReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData mData =  bundle.getParcelable(Constants.ENTITY);
                String item_name = (String) mData.getData().get(KeyData.SLIDE_MENU.ITEM_NAME);
                clickItemLeftMenuCode(item_name);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(onClickItemReceiver, onClickItemFilter);

        // register event: on scan result
        IntentFilter onScanResultFilter = new IntentFilter("com.simicart.leftmenu.onactivityresult.resultbarcode");
        BroadcastReceiver onScanResultReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData mData =  bundle.getParcelable(Constants.ENTITY);
                int requestCode = (int) mData.getData().get(KeyData.BAR_CODE.REQUEST_CODE);
                int resultCode = (int) mData.getData().get(KeyData.BAR_CODE.RESULT_CODE);
                Intent data = (Intent) mData.getData().get(KeyData.BAR_CODE.INTENT);

                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() == null) {
                        for (Fragment fragment : SimiManager.getIntance()
                                .getManager().getFragments()) {
                            if (fragment == null) {
                                break;
                            } else {
                                if (fragment instanceof ProductDetailParentFragment) {
//                                    ((ProductDetailParentFragment) fragment).setIsFromScan(false);
                                }
                            }
                        }
                    } else {
                        checkResultBarcode(result.getContents());
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(onScanResultReceiver, onScanResultFilter);

        // register event: on back to scan
        IntentFilter onBackFromDetailFilter = new IntentFilter("com.simicart.leftmenu.mainactivity.onbackpress.backtoscan");
        BroadcastReceiver onBackFromDetailtReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData mData =  bundle.getParcelable(Constants.ENTITY);
                String item_name = (String) mData.getData().get(KeyData.SLIDE_MENU.ITEM_NAME);
                clickItemLeftMenuCode(item_name);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(onBackFromDetailtReceiver, onBackFromDetailFilter);
    }

    private void checkResultBarcode(String code) {
        mModel = new ScanCodeModel();
        final ProgressDialog pd_loading = ProgressDialog
                .show(SimiManager.getIntance().getCurrentActivity(), null,
                        null, true, false);
        pd_loading.setContentView(Rconfig.getInstance().layout(
                "core_base_loading"));
        pd_loading.setCanceledOnTouchOutside(false);
        pd_loading.setCancelable(false);
        pd_loading.show();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                pd_loading.dismiss();
                ArrayList<String> listID = new ArrayList<String>();

//                Product product = null;
//                ArrayList<SimiEntity> entity = mModel.getProductID();
//                if (null != entity && entity.size() > 0) {
//                    product = (Product) entity.get(0);
//                }
                String product_id = mModel.getProductID();
                Log.e("abc", "++" + product_id);
                if (!Utils.validateString(product_id)) {
                    SimiNotify.getInstance().showToast(
                            "Result products is empty");
                    return;
                }

                //listID.add(product_id);
//                ProductDetailParentFragment fragment = ProductDetailParentFragment.newInstance();
//                fragment.setListIDProduct(listID);
//                fragment.setProductID(product_id);
//                fragment.setIsFromScan(true);
//                SimiManager.getIntance().replaceFragment(fragment);
            }
        });
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                pd_loading.dismiss();
                SimiNotify.getInstance().showToast(
                        "Result products is empty");
            }
        });
        mModel.addBody("barcode", code);
        mModel.request();

    }

    private void clickItemLeftMenuCode(String nameItem) {
        if (nameItem.equals(QR_BAR_CODE)) {
            new IntentIntegrator(SimiManager.getIntance().getCurrentActivity()).initiateScan();
        }
    }


}

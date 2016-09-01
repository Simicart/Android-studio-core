package com.simicart.core.setting.controller;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.component.SimiMenuRowComponent;
import com.simicart.core.base.component.SimiMultiMenuRowComponent;
import com.simicart.core.base.component.SimiSwitchMenuRowComponent;
import com.simicart.core.base.component.callback.MenuRowCallBack;
import com.simicart.core.base.component.callback.SwitchMenuCallBack;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.setting.delegate.SettingAppDelegate;
import com.simicart.core.setting.entity.CurrencyEntity;
import com.simicart.core.setting.fragment.ListCurrencyFragment;
import com.simicart.core.setting.fragment.ListLanguageFragment;
import com.simicart.core.splashscreen.model.CurrencyModel;
import com.simicart.core.splashscreen.model.StoreModel;
import com.simicart.core.store.entity.Stores;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingAppController extends SimiController {

    // String currency;
    // String language;
    protected SettingAppDelegate mDelegate;
    private boolean isGetStore, isGetCurrency;

    @Override
    public void onStart() {
        mDelegate.showLoading();
        getStore();
        getCurrency();
    }

    private void getStore() {
        final StoreModel model = new StoreModel();
        DataPreferences.listStores.clear();
        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                isGetStore = true;
                ArrayList<SimiEntity> entity = model.getCollection()
                        .getCollection();
                if (entity.size() > 0) {
                    for (SimiEntity simiEntity : entity) {
                        Stores store = new Stores();
                        store.setJSONObject(simiEntity.getJSONObject());
                        DataPreferences.listStores.add(store);
                    }
                    afterRequestComplete();
                }
            }
        });
        model.request();
    }

    private void getCurrency() {
        final CurrencyModel model = new CurrencyModel();
        DataPreferences.listCurrency.clear();
        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                isGetCurrency = true;
                ArrayList<SimiEntity> entity = model.getCollection()
                        .getCollection();
                if (entity.size() > 0) {
                    for (SimiEntity simiEntity : entity) {
                        CurrencyEntity currency = new CurrencyEntity();
                        currency.setJSONObject(simiEntity.getJSONObject());
                        DataPreferences.listCurrency.add(currency);
                    }
                    afterRequestComplete();
                }
            }
        });
        model.request();
    }

    protected void afterRequestComplete() {
        mDelegate.dismissLoading();
        if(isGetCurrency == true && isGetStore == true) {
            drawView();
        }
    }

    protected void drawView() {

        if (DataPreferences.listStores.size() > 1) {
            SimiMultiMenuRowComponent languageRowComponent = new SimiMultiMenuRowComponent();
            languageRowComponent.setIcon("ic_lang");
            languageRowComponent.setLabel(SimiTranslator.getInstance().translate("Language"));
            languageRowComponent.setCurrent(getCurrentLanguage());
            languageRowComponent.setmCallBack(new MenuRowCallBack() {
                @Override
                public void onClick() {
                    changeLanguage();
                }
            });
            mDelegate.addItemRow(languageRowComponent.createView());
        }

        if (DataPreferences.listCurrency.size() > 1) {
            SimiMultiMenuRowComponent currencyRowComponent = new SimiMultiMenuRowComponent();
            currencyRowComponent.setIcon("ic_currency");
            currencyRowComponent.setLabel(SimiTranslator.getInstance().translate("Currency"));
            currencyRowComponent.setCurrent(getCurrentCurrency());
            currencyRowComponent.setmCallBack(new MenuRowCallBack() {
                @Override
                public void onClick() {
                    changeCurrency();
                }
            });
            mDelegate.addItemRow(currencyRowComponent.createView());
        }

        SimiSwitchMenuRowComponent notificationRowComponent = new SimiSwitchMenuRowComponent();
        notificationRowComponent.setIcon("ic_notification");
        notificationRowComponent.setLabel(SimiTranslator.getInstance().translate("Show Notification"));
        notificationRowComponent.setmCallBack(new SwitchMenuCallBack() {
            @Override
            public void onClick(boolean enable) {
                DataPreferences.saveNotificationSet(enable);
            }
        });
        mDelegate.addItemRow(notificationRowComponent.createView());

        SimiMenuRowComponent locationRowComponent = new SimiMenuRowComponent();
        locationRowComponent.setIcon("ic_location_setting");
        locationRowComponent.setLabel(SimiTranslator.getInstance().translate("Location Setting"));
        locationRowComponent.setmCallBack(new MenuRowCallBack() {
            @Override
            public void onClick() {
                Intent viewIntent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                SimiManager.getIntance().getCurrentActivity()
                        .startActivity(viewIntent);
            }
        });
        mDelegate.addItemRow(locationRowComponent.createView());

    }

    protected void changeCurrency() {
        HashMap<String,Object> hmData = new HashMap<>();
        hmData.put(Constants.KeyData.CURRENT_ITEM, getCurrentCurrency());
        ListCurrencyFragment fragment = ListCurrencyFragment
                .newInstance(new SimiData(hmData));
        if (DataLocal.isTablet) {
            SimiManager.getIntance().replacePopupFragment(fragment);
        } else {
            SimiManager.getIntance().replaceFragment(fragment);
        }
    }

    protected String getCurrentCurrency() {
        String currentCurrency = "";
        if (DataPreferences.listCurrency != null && DataPreferences.listCurrency.size() > 0) {
            for (CurrencyEntity entity : DataPreferences.listCurrency) {
                if (entity.getValue().equals(DataPreferences.getCurrencyID())) {
                    currentCurrency = entity.getTitle();
                    return currentCurrency;
                }
            }
        }
        return currentCurrency;
    }

    protected void changeLanguage() {
        HashMap<String,Object> hmData = new HashMap<>();
        hmData.put(Constants.KeyData.CURRENT_ITEM, getCurrentLanguage());
        ListLanguageFragment fragment = ListLanguageFragment
                .newInstance(new SimiData(hmData));
        if (DataLocal.isTablet) {
            SimiManager.getIntance().replacePopupFragment(fragment);
        } else {
            SimiManager.getIntance().replaceFragment(fragment);
        }
    }

    protected String getCurrentLanguage() {
        String currentLanguage = "";
        if (DataPreferences.listStores != null && DataPreferences.listStores.size() > 0) {
            for (Stores stores : DataPreferences.listStores) {
                if (stores.getStoreID().equals(DataPreferences.getStoreID())) {
                    currentLanguage = stores.getStoreName();
                    return currentLanguage;
                }
            }
        }
        return currentLanguage;
    }

    @Override
    public void onResume() {
        drawView();
    }

    public void setDelegate(SettingAppDelegate settingAppDelegate) {
        this.mDelegate = settingAppDelegate;
    }

}

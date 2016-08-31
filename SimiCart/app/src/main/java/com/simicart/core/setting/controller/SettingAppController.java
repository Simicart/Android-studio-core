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
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.config.DataLocal;
import com.simicart.core.setting.delegate.SettingAppDelegate;
import com.simicart.core.setting.entity.CurrencyEntity;
import com.simicart.core.setting.fragment.ListCurrencyFragment;
import com.simicart.core.setting.fragment.ListLanguageFragment;
import com.simicart.core.splashscreen.model.CurrencyModel;
import com.simicart.core.splashscreen.model.StoreModel;
import com.simicart.core.store.entity.Stores;

import java.util.ArrayList;

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
        DataLocal.listStores.clear();
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
                        DataLocal.listStores.add(store);
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

        if (DataLocal.listStores.size() > 1) {
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
        locationRowComponent.setIcon("ic_acc_profile");
        locationRowComponent.setLabel(SimiTranslator.getInstance().translate("Profile"));
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
        ListCurrencyFragment fragment = ListCurrencyFragment
                .newInstance(getCurrentCurrency());
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
        ListLanguageFragment fragment = ListLanguageFragment
                .newInstance(getCurrentLanguage());
        if (DataLocal.isTablet) {
            SimiManager.getIntance().replacePopupFragment(fragment);
        } else {
            SimiManager.getIntance().replaceFragment(fragment);
        }
    }

    protected String getCurrentLanguage() {
        String currentLanguage = "";
        if (DataLocal.listStores != null && DataLocal.listStores.size() > 0) {
            for (Stores stores : DataLocal.listStores) {
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

package com.simicart.core.splashscreen.controller;

import android.content.Context;
import android.util.Log;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.event.base.ReadXML;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.cms.entity.Cms;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.ReadXMLLanguage;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.splashscreen.model.AppConfigModel;
import com.simicart.core.splashscreen.model.CMSPageModel;
import com.simicart.core.splashscreen.model.ListIDPluginModel;
import com.simicart.core.splashscreen.model.ListSKUPluginModel;
import com.simicart.core.splashscreen.model.SaveCurrencyModel;
import com.simicart.core.splashscreen.model.StoreViewModel;
import com.simicart.core.store.entity.Stores;
import com.simicart.theme.matrixtheme.MatrixTheme;
import com.simicart.theme.ztheme.ZTheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashController {

    protected boolean canOpenMain = false;
    protected boolean isSuccessSku = false;

    private String MATRIX_THEME = "matrix";
    private String ZARA_THEME = "zara";


    public void create() {
        initCommon();
        getAppConfig();
        getAvaiablePlugin();
        String currencyID = DataPreferences.getCurrencyID();
        if (Utils.validateString(currencyID)) {
            saveCurrency(currencyID);
        } else {
            getCMSPage();
            getStoreView();
        }
    }

    protected void initCommon() {
        DataLocal.init();
        Config.getInstance().setBaseUrl();
        DataLocal.listCms.clear();
    }

    private void getAppConfig() {
        final AppConfigModel appConfigModel = new AppConfigModel();
        appConfigModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                enableTheme();
                if (canOpenMain && isSuccessSku) {
                    dispatchEvent();
                    SimiManager.getIntance().toMainActivity();
                } else {
                    canOpenMain = true;
                }
            }
        });

        appConfigModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
            }
        });

        appConfigModel.request();

    }

    protected void enableTheme() {
        String themeType = AppColorConfig.getInstance().getThemeType();
        Log.e("SplashController", "=============> THEME TYPE " + themeType);
        if (Utils.validateString(themeType)) {
            if (themeType.equals(MATRIX_THEME)) {
                new MatrixTheme();
            } else if (themeType.equals(ZARA_THEME)) {
                new ZTheme();
            }
        }
    }


    private void getAvaiablePlugin() {
        final ListIDPluginModel listIDPluginModel = new ListIDPluginModel();

        listIDPluginModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                String ids = listIDPluginModel.getListIDPlugin();
                if (Utils.validateString(ids)) {
                    getSKUPlugin(ids);
                } else {
                    isSuccessSku = true;
                }
            }
        });

        listIDPluginModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
            }
        });

        listIDPluginModel.addBody("limit", "100");
        listIDPluginModel.addBody("offset", "0");
        listIDPluginModel.request();
    }

    private void getSKUPlugin(String ids) {
        final ListSKUPluginModel listSKUPluginModel = new ListSKUPluginModel();

        listSKUPluginModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                isSuccessSku = true;
                ArrayList<String> listSKU = listSKUPluginModel.getListSKU();
                listSKU.add("simi_developer");
                if (null != listSKU && listSKU.size() > 0) {
                    enablePlugins(listSKU);
                }
                if (canOpenMain) {
                    dispatchEvent();
                    SimiManager.getIntance().toMainActivity();
                } else {
                    canOpenMain = true;
                }
            }
        });

        listSKUPluginModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
            }
        });

        listSKUPluginModel.addBody("limit", "100");
        listSKUPluginModel.addBody("offset", "0");
        listSKUPluginModel.addBody("ids", ids);
        listSKUPluginModel.request();
    }

    protected void enablePlugins(ArrayList<String> listSKU) {
        Context context = SimiManager.getIntance().getCurrentActivity();
        ReadXML readXml = new ReadXML(context, listSKU);
        readXml.read();

        // dispatch event for Mobile Analytic
        dispatchEventAnalytic();
    }

    protected void dispatchEventAnalytic() {
        SimiEvent.dispatchEvent("com.simicart.analytics.register", new HashMap<String, Object>());
    }


    protected void saveCurrency(String id) {
        SaveCurrencyModel saveCurrencyModel = new SaveCurrencyModel();
        saveCurrencyModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                getCMSPage();
                getStoreView();
            }
        });

        saveCurrencyModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
            }
        });

        saveCurrencyModel.addBody("currency", id);
        saveCurrencyModel.request();
    }

    protected void dispatchEvent() {
        SimiEvent.dispatchEvent(KeyEvent.HIDDEN_ADDRESS.HIDDEN_ADDRESS_GET_CONFIG_ADDRESS, null);
    }


    protected void getCMSPage() {
        DataLocal.listCms.clear();
        final CMSPageModel cmsPageModel = new CMSPageModel();
        cmsPageModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {

                ArrayList<SimiEntity> entity = collection.getCollection();
                if (entity.size() > 0) {
                    for (SimiEntity simiEntity : entity) {
                        Cms cms = new Cms();
                        cms.setJSONObject(simiEntity.getJSONObject());
                        DataLocal.listCms.add(cms);
                    }
                }
            }
        });

        cmsPageModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
            }
        });

        cmsPageModel.request();
    }

    public void getStoreView() {
        final StoreViewModel storeViewModel = new StoreViewModel();
        storeViewModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                if (Config.getInstance().isUseStore()) {
                    changeBaseUrl();
                }

                createLanguage();
                if (canOpenMain && isSuccessSku) {
                    dispatchEvent();
                    SimiManager.getIntance().toMainActivity();
                } else {
                    canOpenMain = true;
                }
            }
        });

        storeViewModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {


            }
        });

        String id = DataPreferences.getStoreID();
        if (Utils.validateString(id)) {
            storeViewModel.addBody("store_id", id);
        }
        storeViewModel.request();
    }


    private void changeBaseUrl() {
        String currentStoreID = AppStoreConfig.getInstance().getStoreID();
        String baseUrl = Config.getInstance().getBaseUrl();
        for (Stores store : DataPreferences.listStores) {
            String id = store.getStoreID();
            if (id.equals(currentStoreID)) {
                int length = baseUrl.split("/").length;
                String last = baseUrl.split("/")[length - 1];
                baseUrl = baseUrl.replace(last, store.getStoreCode());
                Config.getInstance().setBase_url(baseUrl);
            }
        }
    }

    private void createLanguage() {

        try {
            Context context = SimiManager.getIntance().getCurrentActivity();
            ReadXMLLanguage readlanguage = new ReadXMLLanguage(context);
            String identifier = AppStoreConfig.getInstance().getLocaleIdentifier();
            readlanguage.parseXML(identifier + "/localize.xml");
            Map<String, String> languages = readlanguage.getLanguages();
            SimiTranslator.getInstance().setLanguages(languages);
        } catch (Exception e) {
            Map<String, String> languages = new HashMap<>();
            SimiTranslator.getInstance().setLanguages(languages);
        }

    }
}

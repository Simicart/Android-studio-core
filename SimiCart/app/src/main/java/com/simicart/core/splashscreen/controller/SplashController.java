package com.simicart.core.splashscreen.controller;

import android.content.Context;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.cms.entity.Cms;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.splashscreen.model.AppConfigModel;
import com.simicart.core.splashscreen.model.CMSPageModel;
import com.simicart.core.splashscreen.model.ListIDPluginModel;
import com.simicart.core.splashscreen.model.ListSKUPluginModel;
import com.simicart.core.splashscreen.model.SaveCurrencyModel;
import com.simicart.core.splashscreen.model.StoreViewModel;

import java.util.ArrayList;

public class SplashController {

    protected boolean canOpenMain = false;

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

    private void initCommon() {
        DataLocal.init();
        Config.getInstance().setBaseUrl();
        DataLocal.listCms.clear();
    }

    private void getAppConfig() {
        final AppConfigModel appConfigModel = new AppConfigModel();
        appConfigModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                if (canOpenMain) {
                    enableTheme();
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
        ListSKUPluginModel listSKUPluginModel = new ListSKUPluginModel();

        listSKUPluginModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {

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
//        "com.simicart.splashscreen.controller.SplashController"
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
                //config customerAddress
//                DataLocal.ConfigCustomerAddress = model.getConfigCustomerAddress();
//                DataLocal.ConfigCustomerProfile = model.getConfigCustomerAddress();

                if (Config.getInstance().isUseStore()) {
                    changeBaseUrl();
                }

                createLanguage();
                if (canOpenMain) {
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
//        for (Stores store : DataLocal.listStores) {
//            if (store.getStoreID().equals(
//                    "" + Config.getInstance().getStore_id())) {
//                int leg = Config.getInstance().getBaseUrl().split("/").length;
//                String last = Config.getInstance().getBaseUrl().split("/")[leg - 1];
//                Config.getInstance().setBase_url(
//                        Config.getInstance().getBaseUrl()
//                                .replace(last, store.getStoreCode()));
//            }
//        }
    }

    private void createLanguage() {
//        try {
//            ReadXMLLanguage readlanguage = new ReadXMLLanguage(mContext);
//            readlanguage.parseXML(Config.getInstance().getLocale_identifier()
//                    + "/localize.xml");
//            Config.getInstance().setLanguages(readlanguage.getLanguages());
//        } catch (Exception e) {
//            Map<String, String> languages = new HashMap<String, String>();
//            Config.getInstance().setLanguages(languages);
//        }

    }
}

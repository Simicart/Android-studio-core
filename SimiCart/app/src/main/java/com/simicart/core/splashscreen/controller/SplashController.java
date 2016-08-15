package com.simicart.core.splashscreen.controller;

import android.content.Context;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.networkcloud.request.error.SimiError;
import com.simicart.core.cms.entity.Cms;
import com.simicart.core.common.ReadXMLLanguage;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.event.base.EventListener;
import com.simicart.core.event.base.ReadXML;
import com.simicart.core.event.base.UtilsEvent;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.block.EventBlock;
import com.simicart.core.splashscreen.delegate.SplashDelegate;
import com.simicart.core.splashscreen.model.CMSPageModel;
import com.simicart.core.splashscreen.model.ConfigModelCloud;
import com.simicart.core.splashscreen.model.GetIDPluginsModel;
import com.simicart.core.splashscreen.model.GetSKUPluginModel;
import com.simicart.core.splashscreen.model.SaveCurrencyModel;
import com.simicart.core.splashscreen.model.StoreViewModel;
import com.simicart.core.store.entity.Stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashController {

    protected SplashDelegate mDelegate;
    protected Context mContext;
    protected boolean flagHome = false;
    protected boolean flagGetTheme = false;

    private String MATRIX_THEME = "matrix";
    private String ZARA_THEME = "zara";

    public SplashController(SplashDelegate delegate, Context context) {
        mDelegate = delegate;
        mContext = context;
    }

    public void create() {
        initCommon();

        getAppConfig();
        getAvaiablePlugin();

        flagHome = false;
        flagGetTheme = false;

        String id = DataLocal.getCurrencyID();
        if (id != null && !id.equals("")) {
            saveCurrency(id);
        } else {
            getCMSPage();
            getStoreView();
        }
    }

    private void getAppConfig() {
        final ConfigModelCloud appConfigModel = new ConfigModelCloud();
        appConfigModel
                .setDelegate(new com.simicart.core.base.networkcloud.delegate.ModelDelegate() {
                    @Override
                    public void onFail(SimiError error) {
                        if (error != null) {
                            SimiManager.getIntance().showNotify(null,
                                    error.getMessage(), "Ok");
                        }
                    }

                    @Override
                    public void onSuccess(SimiCollection collection) {
                        if (collection != null) {
                            getTheme(appConfigModel.getLayout());
                        }
                    }
                });

        appConfigModel.request();
    }

    private void getTheme(String layout) {
        if (!flagGetTheme) {
            ReadXML readXml = new ReadXML(mContext);
            readXml.read();
            flagGetTheme = true;
        }

        if (layout.equals(MATRIX_THEME)) {
            EventListener.setEvent("simi_themeone");
        } else if (layout.equals(ZARA_THEME)) {
            EventListener.setEvent("simi_ztheme");
        }
    }

    private void getAvaiablePlugin() {
        // http://dev-api.jajahub.com/rest/site-plugins
        // lay ve danh sach id cua plugin enable
        final GetIDPluginsModel idsModel = new GetIDPluginsModel();
        idsModel.setDelegate(new com.simicart.core.base.networkcloud.delegate.ModelDelegate() {
            @Override
            public void onFail(SimiError error) {
//                if (flagHome) {
//                    callEvent();
//                    mDelegate.creatMain();
//                } else {
//                    flagHome = true;
//                }
            }

            @Override
            public void onSuccess(SimiCollection collection) {
                String ids = idsModel.getIDs();
                if (!flagGetTheme) {
                    ReadXML readXml = new ReadXML(mContext);
                    readXml.read();
                    flagGetTheme = true;
                }
                if (Utils.validateString(ids)) {
                    getSKUPlugin(ids);
                } else {
                    if (flagHome) {
                        callEvent();
                        mDelegate.creatMain();
                    } else {
                        flagHome = true;
                    }
                }
            }
        });

        idsModel.addDataParameter("limit", "100");
        idsModel.addDataParameter("offset", "0");
        idsModel.request();

    }

    private void getSKUPlugin(String ids) {
        // http://dev-api.jajahub.com/rest/public_plugins
        // lay sku cua cac plugin enbale

        final GetSKUPluginModel skuModel = new GetSKUPluginModel();
        skuModel.setDelegate(new com.simicart.core.base.networkcloud.delegate.ModelDelegate() {
            @Override
            public void onFail(SimiError error) {

            }

            @Override
            public void onSuccess(SimiCollection collection) {
                ArrayList<String> mSKUs = skuModel.getListSKU();
                if (mSKUs.size() > 0) {
                    for (String mSKU : mSKUs) {
                        EventListener.setEvent(mSKU);
                    }
                }

                if (flagHome) {
                    callEvent();
                    mDelegate.creatMain();
                } else {
                    flagHome = true;
                }
            }
        });

        skuModel.addDataParameter("limit", "100");
        skuModel.addDataParameter("offset", "0");
        skuModel.addDataParameter("ids", ids);
        skuModel.request();

    }

    private void saveCurrency(String id) {
        SaveCurrencyModel model = new SaveCurrencyModel();
        model.setDelegate(new ModelDelegate() {

            @Override
            public void callBack(String message, boolean isSuccess) {
                getCMSPage();
                getStoreView();
            }
        });

        model.addParam("currency", id);

        model.request();
    }

    private void callEvent() {
        CacheBlock cacheBlock = new CacheBlock();
        EventBlock eventBlock = new EventBlock();
        eventBlock.dispatchEvent(
                "com.simicart.splashscreen.controller.SplashController",
                cacheBlock);
    }

    private void initCommon() {
        DataLocal.init(mContext);
        Config.getInstance().setBaseUrl();
        EventListener.listEvent.clear();
        UtilsEvent.itemsList.clear();
        DataLocal.listCms.clear();
        EventListener.setEvent("simi_developer");
    }

    private void getCMSPage() {
        DataLocal.listCms.clear();
        final CMSPageModel model = new CMSPageModel();
        model.setDelegate(new ModelDelegate() {

            @Override
            public void callBack(String message, boolean isSuccess) {
                if (isSuccess) {

                    ArrayList<SimiEntity> entity = model.getCollection()
                            .getCollection();
                    if (entity.size() > 0) {
                        for (SimiEntity simiEntity : entity) {
                            Cms cms = new Cms();
                            cms.setJSONObject(simiEntity.getJSONObject());
                            DataLocal.listCms.add(cms);
                        }
                    }
                }
            }
        });
        model.request();
    }

    public void getStoreView() {
        final StoreViewModel model = new StoreViewModel();
        ModelDelegate delegate = new ModelDelegate() {

            @Override
            public void callBack(String message, boolean isSuccess) {
                if (isSuccess) {

                    //config customerAddress
                    DataLocal.ConfigCustomerAddress = model.getConfigCustomerAddress();
                    DataLocal.ConfigCustomerProfile = model.getConfigCustomerAddress();

                    if (Config.getInstance().getUse_store().equals("1")) {
                        changeBaseUrl();
                    }

                    createLanguage();
                    if (flagHome) {
                        callEvent();
                        mDelegate.creatMain();
                    } else {
                        flagHome = true;
                    }
                }
            }
        };

        model.setDelegate(delegate);
        String id = DataLocal.getStoreID();
        if (Utils.validateString(id)) {
            model.addParam("store_id", id);
        }
        model.request();

    }


    private void changeBaseUrl() {
        for (Stores store : DataLocal.listStores) {
            if (store.getStoreID().equals(
                    "" + Config.getInstance().getStore_id())) {
                int leg = Config.getInstance().getBaseUrl().split("/").length;
                String last = Config.getInstance().getBaseUrl().split("/")[leg - 1];
                Config.getInstance().setBase_url(
                        Config.getInstance().getBaseUrl()
                                .replace(last, store.getStoreCode()));
            }
        }
    }

    private void createLanguage() {
        try {
            ReadXMLLanguage readlanguage = new ReadXMLLanguage(mContext);
            readlanguage.parseXML(Config.getInstance().getLocale_identifier()
                    + "/localize.xml");
            Config.getInstance().setLanguages(readlanguage.getLanguages());
        } catch (Exception e) {
            Map<String, String> languages = new HashMap<String, String>();
            Config.getInstance().setLanguages(languages);
        }

    }
}

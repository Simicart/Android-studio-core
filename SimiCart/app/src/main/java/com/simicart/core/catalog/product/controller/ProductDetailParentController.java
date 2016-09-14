package com.simicart.core.catalog.product.controller;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.adapter.ProductDetailParentAdapter;
import com.simicart.core.catalog.product.delegate.ProductDelegate;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.catalog.product.fragment.InformationFragment;
import com.simicart.core.catalog.product.fragment.OptionFragment;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.catalog.product.model.AddToCartModel;
import com.simicart.core.catalog.product.model.ProductModel;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.common.options.ProductOptionParentView;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.common.options.delegate.OptionProductDelegate;
import com.simicart.core.common.price.ProductPriceViewV03;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.VerticalViewPager2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("DefaultLocale")
public class ProductDetailParentController extends SimiController implements Serializable {

    protected OnClickListener onAddToCartListener;
    protected OnTouchListener onMoreListener;
    protected OnClickListener onOptionListener;
    protected OnClickListener onDoneClick;
    protected View.OnKeyListener mListenerBack;

    protected Product mProduct;
    protected ProductPriceViewV03 mPriceViewZTheme;
    protected boolean statusTopBottom = true;
    protected boolean fromScan = false;

    protected HashMap<String, Object> mData;
    protected String mID;
    protected ArrayList<String> mListID;
    protected ProductDetailParentAdapter mAdapter;
    protected int mCurrentPosition;
    protected ProductDelegate mDelegate;
    protected ArrayList<CacheOptionView> mOptionView;


    public ProductDetailParentController() {
        mOptionView = new ArrayList<>();
    }


    @Override
    public void onStart() {

        mDelegate.showLoading();

        parseData();

        initAdapter();

        initOnTouchListener();

    }

    protected void parseData() {
        if (mData.containsKey(KeyData.PRODUCT_DETAIL.PRODUCT_ID)) {
            mID = (String) mData.get(KeyData.PRODUCT_DETAIL.PRODUCT_ID);
        }

        if (mData.containsKey(KeyData.PRODUCT_DETAIL.LIST_PRODUCT_ID)) {
            mListID = (ArrayList<String>) mData.get(KeyData.PRODUCT_DETAIL.LIST_PRODUCT_ID);
        }

    }

    protected void initAdapter() {
        mCurrentPosition = getPosition();
        if (mCurrentPosition < 0) {
            mListID = new ArrayList<>();
            mListID.add(mID);
            mCurrentPosition = 0;
        }

        mAdapter = new ProductDetailParentAdapter(
                SimiManager.getIntance().getChilFragmentManager());
        mAdapter.setController(this);
        mAdapter.setListID(mListID);
        mDelegate.dismissLoading();
        mDelegate.updateAdapterProduct(mAdapter, mCurrentPosition);


    }

    @Override
    public void onResume() {
        statusTopBottom = true;
        mDelegate.updateView(mModel.getCollection());
        onUpdatePriceView();
        mDelegate.updateAdapterProduct(mAdapter, mCurrentPosition);
        initBack();
    }

    protected void initOnTouchListener() {

        onAddToCartListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                addtoCart();
            }
        };

        onMoreListener = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // v.setBackgroundColor(Color.GRAY);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        // onShowOptionView();
                        onShowDetail();
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        // v.setBackgroundResource(Rconfig.newInstance().getIdDraw(
                        // "core_background_left_border"));
                    }
                    default:
                        break;
                }
                return true;
            }
        };

        onOptionListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mProduct != null)
                    mProduct.setAddedPriceDependent(false);
                onShowOption();
            }
        };

        onDoneClick = new OnClickListener() {

            @Override
            public void onClick(View v) {
                SimiManager.getIntance().hideKeyboard();
                SimiManager.getIntance().getManager().popBackStack();
                addtoCart();
            }
        };

        mListenerBack = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        processBack();
                        return true;
                    }
                }
                return false;
            }
        };

        initBack();
    }

    public void initBack() {
        mDelegate.setListenerBack(mListenerBack);
    }

    protected void processBack() {
        List<Fragment> list_fragmens = SimiManager.getIntance().getManager().getFragments();
        Fragment backFragment = list_fragmens.get(list_fragmens.size() - 1);
        if (backFragment instanceof OptionFragment) {
            SimiManager.getIntance().getManager().popBackStack();
        } else if (fromScan == true && backFragment instanceof ProductDetailParentFragment) {
            HashMap<String, Object> hmData = new HashMap<>();
            hmData.put(KeyData.SLIDE_MENU.ITEM_NAME, "Scan Now");
            SimiEvent.dispatchEvent(com.simicart.core.common.KeyEvent.BAR_CODE.BAR_CODE_ON_BACK, hmData);
            SimiManager.getIntance().getManager().popBackStack();

            fromScan = false;
        } else {
            SimiManager.getIntance().backPreviousFragment();
        }
    }

    protected void onShowOption() {
        View view_option = onShowOptionView();

        OptionFragment fragment = OptionFragment.newInstance(view_option,
                onDoneClick);
        FragmentTransaction frt = SimiManager.getIntance().getManager()
                .beginTransaction();
        frt.add(Rconfig.getInstance().id("container"), fragment);
        frt.addToBackStack(null);
        frt.commit();

    }

    protected View onShowOptionView() {
        if (null == mProduct) {
            mProduct = getProductFromCollection();
        }
        mPriceViewZTheme = new ProductPriceViewV03(mProduct);

        ProductOptionParentView option_parent = new ProductOptionParentView(mProduct, new OptionProductDelegate() {
            @Override
            public void updatePrice(ProductOption cacheOption, boolean isAdd) {
                if (null != mPriceViewZTheme) {
                    if (cacheOption.getDependence_options() != null) {
                        if (mProduct.getType().equals("configurable")) {
                            View view = mPriceViewZTheme
                                    .updatePriceWithOptionConfigable(mProduct, isAdd);
                            mProduct.updateCurrentListDependence();
                            if (null != view) {
                                mDelegate.onUpdatePriceView(view);
                            }
                        }
                    } else {
                        View view = mPriceViewZTheme.updatePriceWithOption(cacheOption,
                                isAdd);
                        if (null != view) {
                            mDelegate.onUpdatePriceView(view);
                        }
                    }
                }
            }
        });

        mOptionView = option_parent.getOptionView();

        return option_parent.initOptionView();

    }


    protected void addtoCart() {
        SimiManager.getIntance().hideKeyboard();
        mProduct = getProductFromCollection();
        if (null != mProduct && mProduct.getStock()) {
            ArrayList<CacheOption> options = getCacheOptions();
            if (!checkSelectedAllOption(options)) {
                SimiNotify.getInstance().showToast(
                        SimiTranslator.getInstance().translate("Please select all options"));
                onShowOptionView();
                return;
            }

            mDelegate.showDialogLoading();
            final AddToCartModel model = new AddToCartModel();
            model.setSuccessListener(new ModelSuccessCallBack() {
                @Override
                public void onSuccess(SimiCollection collection) {
                    mDelegate.dismissDialogLoading();
                    int mQty = getCartQtyFromJsonobject(model.getDataJSON());
                    SimiManager.getIntance().onUpdateCartQty(mQty + "");
                    SimiNotify.getInstance().showToast(
                            SimiTranslator.getInstance().translate("Added to Cart"));
                }
            });


            model.setFailListener(new ModelFailCallBack() {
                @Override
                public void onFail(SimiError error) {
                    mDelegate.dismissDialogLoading();
                    String msg = error.getMessage();
                    if (Utils.validateString(msg)) {
                        SimiNotify.getInstance().showToast(msg);
                    }
                }
            });
            model.addBody("product_id", mProduct.getId());
            String productType = mProduct.getType();
            if (Utils.validateString(productType)) {
                model.addBody("product_type", productType);
            }

            int qty = mProduct.getQty();

            model.addBody("product_qty", String.valueOf(qty));

            if (null != options) {
                try {
                    JSONArray array = convertCacheOptionToParams(options);
                    if (array != null && array.length() > 0) {
                        model.addBody("options", array);
                    }
                } catch (JSONException e) {
                    mDelegate.dismissLoading();
                    SimiNotify.getInstance().showNotify("Cannot convert data");
                    e.printStackTrace();
                    return;
                }
            }
            model.request();
        }
    }

    private int getCartQtyFromJsonobject(JSONObject jsonObject) {
        int mQty = 0;
        if (jsonObject != null) {
            if (jsonObject.has("data")) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);
                            if (object.has("product_qty")) {
                                int qty = object.getInt("product_qty");
                                mQty += qty;
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return mQty;
    }

    protected JSONArray convertCacheOptionToParams(
            ArrayList<CacheOption> options) throws JSONException {
        JSONArray array = new JSONArray();
        for (CacheOption cacheOption : options) {
            if (cacheOption.toParameter() != null) {
                array.put(cacheOption.toParameter());
            }
        }
        return array;
    }

    protected boolean checkSelectedAllOption(ArrayList<CacheOption> options) {
        if (getProductFromCollection().getStock()) {
            if (null == options) {
                if (mProduct.getOptions() != null) {
                    options = mProduct.getOptions();

                    for (CacheOption option : options) {
                        if (option.isRequired() && !option.isCompleteRequired()) {
                            onShowOption();
                            return false;
                        }
                    }
                }
            }

            if (!checkSelectedCacheOption(options)) {
                onShowOption();
                return false;
            }
        }
        return true;
    }


    protected boolean checkSelectedCacheOption(ArrayList<CacheOption> options) {
        if (options != null) {
            for (CacheOption cacheOption : options) {
                if (cacheOption.isRequired() && !cacheOption.isCompleteRequired()) {
                    return false;
                }
            }
        }
        return true;

    }

    protected ArrayList<CacheOption> getCacheOptions() {
        ArrayList<CacheOption> options = null;
        if (mOptionView.size() > 0) {
            options = new ArrayList<>();
            for (CacheOptionView option : mOptionView) {
                options.add(option.getCacheOption());
            }
        }

        return options;
    }


    protected void onShowDetail() {
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(Constants.KeyData.PRODUCT, getProductFromCollection());
        InformationFragment fragment = InformationFragment.newInstance(new SimiData(hmData));
//		fragment.setProduct(getProductFromCollection());
        SimiManager.getIntance().addPopupFragment(fragment);
    }

    public void onUpdateTopBottom(ProductModel model) {
        if (statusTopBottom) {
            mModel = model;
            mProduct = getProductFromCollection();
            mDelegate.updateView(model.getCollection());
            onUpdatePriceView();
        }
    }

    protected void onUpdatePriceView() {
        if (null != mProduct) {
            View view = onShowPriceView(mProduct);
            if (null != view) {
                mDelegate.onUpdatePriceView(view);
            }
        }
    }

    protected View onShowPriceView(Product product) {
        LinearLayout ll_price = new LinearLayout(SimiManager.getIntance()
                .getCurrentActivity());
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        mPriceViewZTheme = new ProductPriceViewV03(product);
        View view = mPriceViewZTheme.getViewPrice();
        if (null != view) {
            ll_price.addView(view, params);
        }
        return ll_price;
    }


    protected Product getProductFromCollection() {
        Product product = null;
        ArrayList<SimiEntity> entity = null;
        if (mModel != null && mModel.getCollection() != null)
            entity = mModel.getCollection().getCollection();
        if (null != entity && entity.size() > 0) {
            product = (Product) entity.get(0);
        }

        return product;
    }

    protected int getPosition() {
        if (null != mListID && mListID.size() > 0) {
            for (int i = 0; i < mListID.size(); i++) {
                String id = mListID.get(i);
                if (id.equals(mID)) {
                    return i;
                }
            }
        }

        return -1;
    }


    public void onVisibleTopBottom(boolean isVisible) {
        statusTopBottom = !statusTopBottom;
        mDelegate.onVisibleTopBottom(isVisible);
    }

    public void updateViewPager(VerticalViewPager2 viewpager) {
        mDelegate.updateViewPager(viewpager);
    }


    public void setData(HashMap<String, Object> data) {
        mData = data;
    }

    public OnClickListener getAddToCartListener() {
        return onAddToCartListener;
    }


    public OnTouchListener getMoreListener() {
        return onMoreListener;
    }

    public OnClickListener getOptionListener() {
        return onOptionListener;
    }

    public void setProductDelegate(ProductDelegate delegate) {
        mDelegate = delegate;
    }

    public void setFromScan(boolean fromScan) {
        this.fromScan = fromScan;
    }

    public void setDelegate(ProductDelegate delegate) {
        mDelegate = delegate;
    }
}

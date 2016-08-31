package com.simicart.core.catalog.product.controller;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.delegate.ProductDelegate;
import com.simicart.core.catalog.product.delegate.ProductDetailAdapterDelegate;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.catalog.product.fragment.InformationFragment;
import com.simicart.core.catalog.product.fragment.OptionFragment;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.catalog.product.model.ProductModel;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.options.ProductOptionParentView;
import com.simicart.core.common.price.ProductPriceViewV03;
import com.simicart.core.config.Constants;
import com.simicart.core.style.VerticalViewPager2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("DefaultLocale")
public class ProductDetailParentController extends ProductController implements Serializable {

    protected OnClickListener onTouchAddToCart;
    protected OnTouchListener onTouchDetails;
    protected OnClickListener onTouchOptions;
    protected OnClickListener onDoneClick;
    protected View.OnKeyListener mListenerBack;

    protected Product mProduct;
    protected ProductPriceViewV03 mPriceViewZTheme;
    protected boolean statusTopBottom = true;
    protected ProductDetailAdapterDelegate mAdapterDelegate;
    private boolean checkOptionDerect = false;
    protected boolean fromScan = false;

    public void setAdapterDelegate(ProductDetailAdapterDelegate delegate) {
        mAdapterDelegate = delegate;
    }

    public OnClickListener getTouchAddToCart() {
        return onTouchAddToCart;
    }

    public OnClickListener getOnDoneClick() {
        return onDoneClick;
    }

    public OnTouchListener getTouchDetails() {
        return onTouchDetails;
    }

    public OnClickListener getTouchOptions() {
        return onTouchOptions;
    }

    public void setProductDelegate(ProductDelegate delegate) {
        mDelegate = delegate;
    }

    public void setFromScan(boolean fromScan) {
        this.fromScan = fromScan;
    }

    @Override
    public void onStart() {
        initOnTouchListener();

    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
        onUpdatePriceView();
        initBack();
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initOnTouchListener() {

        onTouchAddToCart = new OnClickListener() {

            @Override
            public void onClick(View v) {
                checkOptionDerect = false;
                onAddToCart();
            }
        };

        onTouchDetails = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // v.setBackgroundColor(Color.GRAY);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        onShowOptionView();
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

        onTouchOptions = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mProduct != null)
                    mProduct.setAddedPriceDependent(false);
                checkOptionDerect = true;
                onShowOption();
            }
        };

        onDoneClick = new OnClickListener() {

            @Override
            public void onClick(View v) {
                SimiManager.getIntance().hideKeyboard();
                SimiManager.getIntance().getManager().popBackStack();
                if (checkOptionDerect == false) {
                    onAddToCart();
                }
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
//        if(backFragment instanceof ProductDetailParentFragment && fromScan == true) {
//            HashMap<String,Object> hmData = new HashMap<>();
//            hmData.put(KeyData.SLIDE_MENU.ITEM_NAME, "Scan Now");
//            SimiEvent.dispatchEvent(com.simicart.core.common.KeyEvent.BAR_CODE.BAR_CODE_ON_BACK, hmData);
//            SimiManager.getIntance().getManager().popBackStack();
//
//            fromScan = false;
//        } else {
//            SimiManager.getIntance().getManager().popBackStack();
//        }
        if (backFragment instanceof OptionFragment) {
            SimiManager.getIntance().getManager().popBackStack();
        } else if (fromScan == true && backFragment instanceof ProductDetailParentFragment) {
            HashMap<String,Object> hmData = new HashMap<>();
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
        mDelegate.onUpdateOptionView(view_option);
    }

    @Override
    protected View onShowOptionView() {
        if (null == mProduct) {
            mProduct = getProductFromCollection();
        }
        mPriceViewZTheme = new ProductPriceViewV03(mProduct);

        ProductOptionParentView option_parent = new ProductOptionParentView(
                mProduct, this);

        mOptionView = option_parent.getOptionView();

        return option_parent.initOptionView();

    }

    protected void onAddToCart() {
        SimiManager.getIntance().hideKeyboard();
        mProduct.setAddedPriceDependent(false);
        if (null != mProduct && mProduct.getStock()) {
            String url = mProduct.getImages()[0];
            addtoCart(url);
        }
    }

    @Override
    protected boolean checkSelectedAllOption() {
        if (getProductFromCollection().getStock()) {
            ArrayList<CacheOption> options = getCacheOptions();

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

    protected void onShowDetail() {
        HashMap<String,Object> hmData = new HashMap<>();
        hmData.put(Constants.KeyData.PRODUCT, getProductFromCollection());
        InformationFragment fragment = InformationFragment.newInstance(new SimiData(hmData));
//		fragment.setProduct(getProductFromCollection());
        SimiManager.getIntance().addPopupFragment(fragment);
    }

    public void onUpdateTopBottom(ProductModel model) {
        Log.e("ProductDetailParentController ", "onUpdateTopBottom 001");
        if (statusTopBottom) {
            Log.e("ProductDetailParentController ", "onUpdateTopBottom 002");
            mModel = model;
            onUpdatePriceView(model);
            mProduct = getProductFromCollection();
            mDelegate.updateView(model.getCollection());
        }
    }

    protected void onUpdatePriceView(ProductModel model) {
        Product product = getProductFromCollection();
        if (null != product) {
            View view = onShowPriceView(product);
            if (null != view) {
                mDelegate.onUpdatePriceView(view);
            }
        }
    }

    @Override
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

    public void onVisibleTopBottom(boolean isVisible) {
        statusTopBottom = !statusTopBottom;
        mDelegate.onVisibleTopBottom(isVisible);
    }

    public void updateViewPager(VerticalViewPager2 viewpager) {
        mDelegate.updateViewPager(viewpager);
    }

}

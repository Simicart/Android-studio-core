package com.simicart.core.catalog.product.block;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.adapter.ProductDetailParentAdapter;
import com.simicart.core.catalog.product.delegate.ProductDelegate;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.CirclePageIndicator;
import com.simicart.core.style.VerticalViewPager2;

import java.util.ArrayList;


public class ProductDetailParentBlock extends SimiBlock implements
        ProductDelegate {
    protected RelativeLayout rltTop;
    protected LinearLayout llBottom;
    protected AppCompatButton btn_option;
    protected AppCompatButton btn_addtocart;
    protected TextView tv_name_product;
    protected LinearLayout ll_price;
    protected Product mProduct;
    protected CirclePageIndicator mIndicator;
    protected TextView tvMore;
    protected ViewPager vpProducts;

    public ProductDetailParentBlock(View view, Context context) {
        super(view, context);
    }


    public void setAddToCartListener(OnClickListener listener) {
        btn_addtocart.setOnClickListener(listener);
    }

    public void setDetailListener(OnTouchListener listener) {
        tvMore.setOnTouchListener(listener);
    }

    public void setOptionListener(OnClickListener listener) {
        btn_option.setOnClickListener(listener);
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        initTop();

        // view pager for list of produt
        vpProducts = (ViewPager) id("vpg_product");

        // indicator
        mIndicator = (CirclePageIndicator) mView.findViewById(Rconfig
                .getInstance().id("indicator"));
        mIndicator.setFillColor(AppColorConfig.getInstance().getKeyColor());

        if (DataLocal.isTablet) {
            mIndicator.setScaleX(1.5f);
            mIndicator.setScaleY(1.5f);
        }
        mIndicator.setOrientation(LinearLayout.VERTICAL);

        initBottom();
    }

    protected void initTop() {
        rltTop = (RelativeLayout) id("rlt_top");
        rltTop.setBackgroundResource(Rconfig.getInstance().drawable(
                "core_backgroud_top_product_detail"));
        rltTop.setBackgroundColor(AppColorConfig.getInstance()
                .getSectionColor());
        rltTop.getBackground().setAlpha(100);

        // name product
        tv_name_product = (TextView) id("tv_name");
        // price
        ll_price = (LinearLayout) id("ll_price");

        // initial More
        tvMore = (TextView) id("tv_more");
        tvMore.setText(SimiTranslator.getInstance().translate("More"));
        tvMore.setTextColor(AppColorConfig.getInstance().getContentColor());

        ImageView imgMore = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("img_more"));
        Drawable icon = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("core_icon_more"));
        icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        imgMore.setImageDrawable(icon);

        View imgSeperate = id("v_separate");
        Drawable icon_img_seprate = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("core_background_right_border"));
        icon_img_seprate.setColorFilter(
                AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        imgSeperate.setBackground(icon_img_seprate);

        TextView tvCalculate = new TextView(mContext);
        tvCalculate.setText("Product");
        int heightName = Utils.calculateHeightTextView(tvCalculate);
        int height = heightName;
        height = Utils.toDp(height);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        rltTop.setLayoutParams(params);

    }


    protected void initBottom() {
        llBottom = (LinearLayout) id("ll_bottom");
        // options
        btn_option = (AppCompatButton) id("btn_option");

        // add to cart
        btn_addtocart = (AppCompatButton) id("btn_addtocart");
    }

    @Override
    public void drawView(SimiCollection collection) {
        if (null != collection) {
            mProduct = getProductFromCollection(collection);
            if (null != mProduct) {
                llBottom.setVisibility(View.VISIBLE);
                rltTop.setVisibility(View.VISIBLE);
                btn_addtocart.setVisibility(View.VISIBLE);
                btn_option.setVisibility(View.VISIBLE);

                showNameProduct();
                showOption();
                showAddToCart();
            }
        }
    }

    protected void showNameProduct() {
        if (null != mProduct) {
            String name_product = mProduct.getName();
            tv_name_product.setTextColor(AppColorConfig.getInstance().getContentColor());
            if (Utils.validateString(name_product)) {
                tv_name_product.setText(name_product.trim());
            }
        }
    }

    protected void showOption() {
        ArrayList<CacheOption> options = mProduct.getOptions();
        if (null == options || options.size() == 0) {
            btn_option.setVisibility(View.GONE);
        } else {
            btn_option.setVisibility(View.VISIBLE);
            btn_option.setText(SimiTranslator.getInstance().translate("Options"));
            btn_option.setTextColor(AppColorConfig.getInstance().getButtonTextColor());
            btn_option.setClickable(true);
            btn_option.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
        }
    }

    protected void showAddToCart() {
        boolean stock = mProduct.getStock();
        if (stock) {
            btn_addtocart.setText(SimiTranslator.getInstance().translate("Add To Cart"));
            btn_addtocart.setTextColor(AppColorConfig.getInstance().getButtonTextColor());
            btn_addtocart.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
        } else {
            btn_addtocart.setText(SimiTranslator.getInstance().translate("Out Stock"));
            btn_addtocart.setTextColor(Color.parseColor("#FFFFFF"));
            btn_addtocart.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground(Color.GRAY));
            btn_addtocart.setClickable(false);
        }
    }

    @Override
    public void onVisibleTopBottom(boolean isVisible) {
        if (isVisible) {
            if (rltTop.getVisibility() == View.VISIBLE
                    && llBottom.getVisibility() == View.VISIBLE) {
                rltTop.setVisibility(View.GONE);
                llBottom.setVisibility(View.GONE);
            } else {
                rltTop.setVisibility(View.VISIBLE);
                llBottom.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onUpdatePriceView(View view) {
        if (null != view) {
            ll_price.removeAllViewsInLayout();
            LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            if (DataLocal.isTablet) {
                params.gravity = Gravity.CENTER_HORIZONTAL;
            } else {
                params.gravity = Gravity.LEFT;
            }
            ll_price.addView(view, params);
        }
    }


    protected Product getProductFromCollection(SimiCollection collection) {
        Product product = null;
        ArrayList<SimiEntity> entity = collection.getCollection();
        if (null != entity && entity.size() > 0) {
            product = (Product) entity.get(0);
        }
        return product;
    }


    @Override
    public void updateViewPager(VerticalViewPager2 viewpager) {
        if (null != mIndicator && null != viewpager
                && null != viewpager.getAdapter()) {
            mIndicator.setViewPager(viewpager);
            mIndicator.setCurrentItem(0);
        }
    }

    @Override
    public void setListenerBack(View.OnKeyListener listenerBack) {
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(listenerBack);
    }

    @Override
    public void updateAdapterProduct(ProductDetailParentAdapter adapter, int currentPosition) {
        vpProducts.setOffscreenPageLimit(3);
        vpProducts.setAdapter(adapter);
        vpProducts.setCurrentItem(currentPosition);
    }

}

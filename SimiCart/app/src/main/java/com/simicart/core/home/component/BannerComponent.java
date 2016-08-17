package com.simicart.core.home.component;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.simicart.core.banner.animation.SliderLayout;
import com.simicart.core.banner.animation.SliderTypes.BaseSliderView;
import com.simicart.core.banner.animation.SliderTypes.DefaultSliderView;
import com.simicart.core.banner.animation.Transformers.BaseTransformer;
import com.simicart.core.banner.entity.BannerEntity;
import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.core.home.delegate.BannerCallBack;

import java.util.ArrayList;

/**
 * Created by frank on 17/08/2016.
 */
public class BannerComponent extends SimiComponent {

    protected SliderLayout mSliderLayout;
    protected BannerCallBack mCallBack;
    protected ArrayList<BannerEntity> mListBanner;
    private String TYPE_PRODUCT = "1";
    private String TYPE_CATEGORY = "2";
    private String TYPE_WEB = "3";

    public BannerComponent(ArrayList<BannerEntity> banners) {
        super();
        mListBanner = banners;
    }


    @Override
    public View createView() {
        rootView = findLayout("core_component_banner");
        mSliderLayout = (SliderLayout) findView("sld_banner");
        if (null != mListBanner && mListBanner.size() > 0) {
            for (int i = 0; i < mListBanner.size(); i++) {
                BannerEntity banner = mListBanner.get(i);
                DefaultSliderView slider = createBannerItem(banner);
                mSliderLayout.addSlider(slider);
            }
            if (mListBanner.size() == 1) {
                mSliderLayout.setDuration(0);
                mSliderLayout.stopAutoCycle();
                mSliderLayout.setPagerTransformer(false, new BaseTransformer() {
                    @Override
                    protected void onTransform(View view, float v) {
                    }
                });
            }
        }

        return rootView;
    }

    protected DefaultSliderView createBannerItem(final BannerEntity banner) {
        DefaultSliderView bannerSlider = new DefaultSliderView(mContext);
        String urlImage = banner.getImage();
        if (Utils.validateString(urlImage)) {
            bannerSlider.image(urlImage).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    if (null != mCallBack) {
                        mCallBack.openBanner(banner);
                    } else {
                        openBanner(banner);
                    }
                }
            });
        }
        if (DataLocal.isTablet) {
            bannerSlider
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);
        } else {
            bannerSlider.setScaleType(BaseSliderView.ScaleType.Fit);
        }

        return bannerSlider;
    }

    protected void openBanner(BannerEntity banner) {
        String type = banner.getType();
        if (Utils.validateString(type)) {
            type = type.trim();
            if (type.equals(TYPE_PRODUCT)) {
                String productId = banner.getProductId();
                if (Utils.validateString(productId)) {
                    openProductDetail(productId);
                }
            } else if (type.equals(TYPE_CATEGORY)) {
                String cateID = banner.getCategoryId();
                String cateName = banner.getCategoryName();
                if (Utils.validateString(cateID)) {
                    if (banner.hasChild()) {
                        openCategoryDetail(cateID, cateName);
                    } else {
                        openCategory(cateID, cateName);
                    }
                }
            } else if (type.equals(TYPE_WEB)) {
                String url = banner.getUrl();
                if (Utils.validateString(url)) {
                    openWebPage(url);
                }
            }
        }

    }

    protected void openProductDetail(String productId) {

    }

    protected void openCategory(String cateID, String cateName) {

    }

    protected void openCategoryDetail(String cateID, String cateName) {

    }

    protected void openWebPage(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        mContext.startActivity(browserIntent);
    }

}

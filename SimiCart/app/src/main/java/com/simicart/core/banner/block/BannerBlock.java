package com.simicart.core.banner.block;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.simicart.core.banner.animation.Animations.DescriptionAnimation;
import com.simicart.core.banner.animation.Indicators.PagerIndicator.IndicatorVisibility;
import com.simicart.core.banner.animation.SliderLayout;
import com.simicart.core.banner.animation.SliderTypes.BaseSliderView;
import com.simicart.core.banner.animation.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.simicart.core.banner.animation.SliderTypes.DefaultSliderView;
import com.simicart.core.banner.delegate.BannerDelegate;
import com.simicart.core.banner.entity.BannerEntity;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

public class BannerBlock extends SimiBlock implements BannerDelegate {

    // protected ViewFlipper mBannerFlipper;
    protected SliderLayout mSliderLayout;
    protected View mView;
    protected Context mContext;
    protected Activity activity;
    private String TYPE_PRODUCT = "1";
    private String TYPE_CATEGORY = "2";
    private String TYPE_WEB = "3";

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public BannerBlock(View view, Context context) {
        this.mView = view;
        this.mContext = context;
    }

    public SliderLayout getSliderLayout() {
        return mSliderLayout;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setRootView(View rootView) {
        mView = rootView;
    }

    @Override
    public void initView() {
        // mBannerFlipper = (ViewFlipper)
        // mView.findViewById(Rconfig.newInstance()
        // .id("banner_slider"));
        mSliderLayout = (SliderLayout) mView.findViewById(Rconfig.getInstance()
                .id("banner_slider"));
    }

    @Override
    public void drawView(SimiCollection collection) {
        if (collection == null) {
            collection = new SimiCollection();
        }
        ArrayList<SimiEntity> listBanner = collection.getCollection();
        if (listBanner == null || listBanner.size() == 0) {
            showBannersFake();
        } else {
            // showBanners(listBanner);
            showBannersNew(listBanner);
        }
    }

    private void showBannersFake() {
        for (int i = 0; i < 3; i++) {
            final BannerEntity bannerEntity = new BannerEntity();
            bannerEntity.setUrl("");
            bannerEntity.setImage("");
            bannerEntity.setType("");
            bannerEntity.setCategoryName("");
            bannerEntity.setCategoryId("");
            bannerEntity.setHasChild("");
            bannerEntity.setProductId("");
            Context context  = SimiManager.getIntance().getCurrentActivity();
            DefaultSliderView textSliderView = new DefaultSliderView(
                    context);
            textSliderView.image(Rconfig.getInstance().drawable("fake_banner"))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new OnSliderClickListener() {

                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            final BannerEntity banner_ad = bannerEntity;
                            onSliderClickListener(banner_ad);
                        }
                    });
            mSliderLayout.addSlider(textSliderView);
            mSliderLayout
                    .setPresetTransformer(SliderLayout.Transformer.Default);
            mSliderLayout.setIndicatorVisibility(IndicatorVisibility.Invisible);
            mSliderLayout.setCustomAnimation(new DescriptionAnimation());
            mSliderLayout.setDuration(3000);
        }
    }

    private void showBannersNew(ArrayList<SimiEntity> listBanner) {

    }

    public void onSliderClickListener(BannerEntity banner_ad) {

    }

}

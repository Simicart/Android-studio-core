package com.simicart.theme.matrixtheme.home.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.simicart.core.banner.entity.BannerEntity;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.listproducts.fragment.ProductListFragment;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;
import com.simicart.theme.matrixtheme.home.adapter.Theme1PagerAdapter;

public class PagerBannerFragment extends SimiFragment {

    //    private String mUrlImage;
//    private String mUrlAd;
    private Context context;
    private BannerEntity bannerEntity;
    private int position;
    private float scale;
    private boolean isBlured;
    private String TYPE_PRODUCT = "1";
    private String TYPE_CATEGORY = "2";
    private String TYPE_WEB = "3";

//	public void setUrlImage(String url) {
//		mUrlImage = url;
//	}

//	public void setUrlAd(String url) {
//		mUrlAd = url;
//	}

//	public void setBannerEntity(BannerEntity bannerEntity) {
//		this.bannerEntity = bannerEntity;
//	}

    //	public static Fragment newInstance(Context context, int pos, float scale,
//			boolean IsBlured) {
//		Bundle b = new Bundle();
//		b.putInt("pos", pos);
//		b.putFloat("scale", scale);
//		b.putBoolean("IsBlured", IsBlured);
//		return Fragment.instantiate(context,
//				PagerBannerFragment.class.getName(), b);
//	}
    public static PagerBannerFragment newInstance(int postion, float scale, boolean isBlured, BannerEntity bannerEntity) {
        PagerBannerFragment fragment = new PagerBannerFragment();
        Bundle bundle = new Bundle();
//        setData(Constants.KeyData.POSITON, postion, Constants.KeyData.TYPE_INT, bundle);
//        setData(Constants.KeyData.SCALE, scale, Constants.KeyData.TYPE_FLOAT, bundle);
//        setData(Constants.KeyData.CHECK_BLURED, isBlured, Constants.KeyData.TYPE_BOOLEAN, bundle);
//	setData(Constants.KeyData.BANNERENTITY, bannerEntity, Constants.KeyData.TYPE_MODEL, bundle);
//        bundle.putSerializable(Constants.KeyData.BANNERENTITY, bannerEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        context = getActivity();

        LinearLayout rootView = (LinearLayout) inflater.inflate(Rconfig
                        .getInstance().layout("theme1_item_viewpager"), container,
                false);

        ImageView imageview = (ImageView) rootView.findViewById(Rconfig
                .getInstance().id("banner"));

        //getdata
        if (getArguments() != null) {
//            position = (int) getData(Constants.KeyData.POSITON, Constants.KeyData.TYPE_INT, getArguments());
//            scale = (float) getData(Constants.KeyData.SCALE, Constants.KeyData.TYPE_FLOAT, getArguments());
//            isBlured = (boolean) getData(Constants.KeyData.CHECK_BLURED, Constants.KeyData.TYPE_BOOLEAN, getArguments());
//            bannerEntity = (BannerEntity) getArguments().getSerializable(Constants.KeyData.BANNERENTITY);
        }
        if (bannerEntity.getImage() != null) {
            DrawableManager.fetchDrawableOnThread(bannerEntity.getImage(),
                    imageview);

        }
        imageview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SimiFragment fragment = null;
                if (bannerEntity.getType() != null) {
                    if (bannerEntity.getType().equals(TYPE_PRODUCT)) {
                        if (bannerEntity.getProductId() != null
                                && !bannerEntity.getProductId().equals("")
                                && !bannerEntity.getProductId().toLowerCase()
                                .equals("null")) {
                            fragment = ProductDetailParentFragment
                                    .newInstance(bannerEntity.getProductId(), null);
//							((ProductDetailParentFragment) fragment)
//									.setProductID(bannerEntity.getProductId());
                            SimiManager.getIntance().addFragment(fragment);
                        }
                    } else if (bannerEntity.getType().equals(TYPE_CATEGORY)) {
//                        if (bannerEntity.getCategoryId() != null
//                                && !bannerEntity.getCategoryId().equals("")
//                                && !bannerEntity.getCategoryId().toLowerCase()
//                                .equals("null")) {
//                            if (bannerEntity.getHasChild() != null
//                                    && !bannerEntity.getHasChild().equals("")
//                                    && !bannerEntity.getHasChild()
//                                    .toLowerCase().equals("null")) {
//                                if (bannerEntity.getHasChild().equals("1")) {
//                                    if (DataLocal.isTablet) {
//                                        fragment = CategoryFragment.newInstance(
//                                                bannerEntity.getCategoryId(), bannerEntity.getCategoryName());
//                                        CateSlideMenuFragment.getIntance()
//                                                .replaceFragmentCategoryMenu(
//                                                        fragment);
//                                    } else {
//                                        fragment = CategoryFragment.newInstance(
//                                                bannerEntity.getCategoryId(), bannerEntity.getCategoryName());
//                                        SimiManager.getIntance().addFragment(
//                                                fragment);
//                                    }
//                                } else {
//                                    fragment = ProductListFragment.newInstance(bannerEntity.getCategoryId(), bannerEntity.getCategoryName(), null, null, null);
//                                    SimiManager.getIntance().addFragment(
//                                            fragment);
//                                }
//                            }
//                        }
                    } else if (bannerEntity.getType().equals(TYPE_WEB)) {
                        if (null != bannerEntity.getUrl()
                                && !bannerEntity.getUrl().equals("")
                                && !bannerEntity.getUrl().equals("null")
                                && URLUtil.isValidUrl(bannerEntity.getUrl())) {
                            try {
                                Intent browserIntent = new Intent(
                                        Intent.ACTION_VIEW, Uri
                                        .parse(bannerEntity.getUrl()));
                                context.startActivity(browserIntent);
                            } catch (Exception e) {

                            }
                        }
                    } else {
                        if (null != bannerEntity.getUrl()
                                && !bannerEntity.getUrl().equals("")
                                && !bannerEntity.getUrl().equals("null")
                                && URLUtil.isValidUrl(bannerEntity.getUrl())) {
                            try {
                                Intent browserIntent = new Intent(
                                        Intent.ACTION_VIEW, Uri
                                        .parse(bannerEntity.getUrl()));
                                context.startActivity(browserIntent);
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }
        });

        MyLinearLayout root = (MyLinearLayout) rootView.findViewById(Rconfig
                .getInstance().id("root"));
//		float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);
//		boolean isBlured = this.getArguments().getBoolean("IsBlured");
        if (isBlured) {
            ViewHelper.setAlpha(root, Theme1PagerAdapter.getMinAlpha());
            ViewHelper.setRotationY(root, Theme1PagerAdapter.getMinDegree());
        }
        return rootView;
    }


}

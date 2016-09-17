package com.simicart.core.catalog.product.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.adapter.TabAdapterFragment;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.product.block.ProductMorePluginBlock;
import com.simicart.core.catalog.product.controller.ProductMorePluginController;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.PagerSlidingTabStrip;

public class InformationFragment extends SimiFragment {

    protected Product mProduct;
    protected View mRootView;
    protected ProductMorePluginBlock mPluginBlock;
    protected ProductMorePluginController mPluginController;

    public static InformationFragment newInstance(SimiData data) {
        InformationFragment fragment = new InformationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        //bundle.putSerializable(Constants.KeyData.PRODUCT, product);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Product getProduct() {
        return mProduct;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(
                Rconfig.getInstance().layout("core_information_layout"),
                container, false);
        Context context = getActivity();

        if (mData != null) {
            mProduct = (Product) getValueWithKey(Constants.KeyData.PRODUCT);
        }
        if (null != mProduct) {
            initView();
            // RelativeLayout ll_plugin = (RelativeLayout) mRootView
            // .findViewById(Rconfig.newInstance().id("more_plugins"));
            mPluginBlock = new ProductMorePluginBlock(mRootView, context);
            mPluginBlock.setProduct(mProduct);
            mPluginBlock.initView();
            if (mPluginController == null) {
                mPluginController = new ProductMorePluginController();
                mPluginController.setDelegate(mPluginBlock);
                mPluginController.setProduct(mProduct);
                mPluginController.onStart();
            } else {
                mPluginController.setProduct(mProduct);
                mPluginController.setDelegate(mPluginBlock);
                mPluginController.onResume();
            }
            mPluginBlock.setListenerMoreShare(mPluginController
                    .getClickerShare());
        }
        return mRootView;
    }

    public void initView() {
        final TabAdapterFragment adapter = new TabAdapterFragment(
                getChildFragmentManager(), mProduct);
        final ViewPager mPager = (ViewPager) mRootView.findViewById(Rconfig
                .getInstance().id("pager"));
        mPager.setAdapter(adapter);

        PagerSlidingTabStrip title_tab = (PagerSlidingTabStrip) mRootView
                .findViewById(Rconfig.getInstance().id("pager_title_strip"));
        title_tab.setTextColor(AppColorConfig.getInstance().getSearchTextColor());
        title_tab.setBackgroundColor(AppColorConfig.getInstance().getSectionColor());
        title_tab.setDividerColor(AppColorConfig.getInstance().getSearchTextColor());
        title_tab.setIndicatorColor(AppColorConfig.getInstance().getKeyColor());
        title_tab.setIndicatorHeight(5);
        title_tab.setAllCaps(false);
        title_tab.setViewPager(mPager);
    }

}

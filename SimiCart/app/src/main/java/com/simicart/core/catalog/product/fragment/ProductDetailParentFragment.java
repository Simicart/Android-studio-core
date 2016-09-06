package com.simicart.core.catalog.product.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.product.adapter.ProductDetailParentAdapter;
import com.simicart.core.catalog.product.block.ProductDetailParentBlock;
import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

public class ProductDetailParentFragment extends SimiFragment {

    protected String mID;
    protected ArrayList<String> mListID;
    protected ProductDetailParentBlock mBlock;
    protected ProductDetailParentController mController;
    protected  boolean isFromScan = false;

    public static ProductDetailParentFragment newInstance(SimiData data) {
        ProductDetailParentFragment fragment = new ProductDetailParentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(
                Rconfig.getInstance().layout("core_product_detail_parent"),
                container, false);
        if (mData != null) {
            mID = (String) getValueWithKey(KeyData.PRODUCT_DETAIL.PRODUCT_ID);
            mListID = (ArrayList<String>) getValueWithKey(KeyData.PRODUCT_DETAIL.LIST_PRODUCT_ID);
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBlock = new ProductDetailParentBlock(rootView, getActivity());
        mBlock.initView();
        if (null == mController) {
            mController = new ProductDetailParentController();
            mController.setDelegate(mBlock);
            mController.setProductDelegate(mBlock);
            mController.setProductId(mID);
            mController.setAdapterDelegate(mBlock);
            if(isFromScan == true) {
                mController.setFromScan(true);
            }
            mController.onStart();
        } else {
            mController.setDelegate(mBlock);
            mController.setProductDelegate(mBlock);
            mController.onResume();
        }

        mBlock.setAddToCartListener(mController.getTouchAddToCart());
        mBlock.setOnDoneOption(mController.getOnDoneClick());
        mBlock.setDetailListener(mController.getTouchDetails());
        mBlock.setOptionListener(mController.getTouchOptions());
        SimiManager.getIntance().setChildFragment(getChildFragmentManager());

        int position = getPosition();
        if (position < 0) {
            mListID = new ArrayList<String>();
            mListID.add(mID);
            position = getPosition();
        }
        final ViewPager pager_parent = (ViewPager) rootView.findViewById(Rconfig
                .getInstance().id("pager_parent"));
        ProductDetailParentAdapter adapter = new ProductDetailParentAdapter(
                SimiManager.getIntance().getChilFragmentManager());
        adapter.setController(mController);
        adapter.setListID(mListID);
        adapter.notifyDataSetChanged();
        pager_parent.setOffscreenPageLimit(3);
        pager_parent.setAdapter(adapter);
        pager_parent.setCurrentItem(position);
        mController.setAdapterDelegate(adapter);
    }


//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//

//
//        if (DataLocal.isTablet) {
//            try {
//                ProductDetailParentAdapterTablet adapter_tablet = new ProductDetailParentAdapterTablet(
//                        SimiManager.getIntance().getChilFragmentManager());
//                adapter_tablet.setListID(mListID);
//                adapter_tablet.setController(mController);
//                adapter_tablet.setProductDelegate(mBlock);
//                pager_parent.setAdapter(adapter_tablet);
//                pager_parent.setCurrentItem(position);
//                adapter_tablet.setCurrentID(mID);
//                pager_parent.setClipToPadding(false);
//                pager_parent.setPageMargin(50);
//
//                pager_parent.setOffscreenPageLimit(3);
//
//                CircleIndicator mIndicator = (CircleIndicator) view
//                        .findViewById(Rconfig.newInstance().id("indicator"));
//                mIndicator.setScaleX(1.5f);
//                mIndicator.setScaleY(1.5f);
//                LayoutParams params = new LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//                String message = getActivity().getString(
//                        Rconfig.newInstance().string("values"));
//
//                if (message.equals("sw800dp")) {
//                    params.leftMargin = Utils.getValueDp(310);
//                } else {
//                    params.leftMargin = Utils.getValueDp(260);
//                }
//                mIndicator.setLayoutParams(params);
//                mController.setAdapterDelegate(adapter_tablet);
//                pager_parent
//                        .setOnPageChangeListener(new OnPageChangeListener() {
//
//                            @Override
//                            public void onPageSelected(int position) {
//                                if (position == mListID.size() - 1) {
//                                    pager_parent.setPadding(
//                                            Utils.getValueDp(50), 0,
//                                            Utils.getValueDp(200), 0);
//                                } else {
//                                    pager_parent.setPadding(
//                                            Utils.getValueDp(200), 0,
//                                            Utils.getValueDp(50), 0);
//                                }
//                            }
//
//                            @Override
//                            public void onPageScrolled(int arg0, float arg1,
//                                                       int arg2) {
//
//                            }
//
//                            @Override
//                            public void onPageScrollStateChanged(int arg0) {
//
//                            }
//                        });
//            } catch (Exception e) {
//                System.err.println("Error Product detail:" + e.getMessage());
//            }
//        } else {

//        }
//    }

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

    public void setIsFromScan(boolean isFromScan) {
        this.isFromScan = isFromScan;
    }

}

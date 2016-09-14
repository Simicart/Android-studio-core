package com.simicart.core.catalog.product.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.product.block.ProductDetailParentBlock;
import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.config.Rconfig;

public class ProductDetailParentFragment extends SimiFragment {

    protected ProductDetailParentBlock mBlock;
    protected ProductDetailParentController mController;
    protected boolean isFromScan = false;

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
                Rconfig.getInstance().layout("core_fragment_product_parent"),
                container, false);
        SimiManager.getIntance().setChildFragment(getChildFragmentManager());

        ProductDetailParentBlock block = new ProductDetailParentBlock(rootView, getActivity());
        block.initView();
        if (null == mController) {
            mController = new ProductDetailParentController();
            mController.setData(mHashMapData);
            mController.setDelegate(block);
            mController.setProductDelegate(block);
            if (isFromScan == true) {
                mController.setFromScan(true);
            }
            mController.onStart();
        } else {
            mController.setDelegate(block);
            mController.setProductDelegate(block);
            mController.onResume();
        }

        block.setAddToCartListener(mController.getAddToCartListener());
        block.setDetailListener(mController.getMoreListener());
        block.setOptionListener(mController.getOptionListener());
        SimiManager.getIntance().setChildFragment(getChildFragmentManager());

        return rootView;
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
//                    params.leftMargin = Utils.toDp(310);
//                } else {
//                    params.leftMargin = Utils.toDp(260);
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
//                                            Utils.toDp(50), 0,
//                                            Utils.toDp(200), 0);
//                                } else {
//                                    pager_parent.setPadding(
//                                            Utils.toDp(200), 0,
//                                            Utils.toDp(50), 0);
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


    public void setIsFromScan(boolean isFromScan) {
        this.isFromScan = isFromScan;
    }

}

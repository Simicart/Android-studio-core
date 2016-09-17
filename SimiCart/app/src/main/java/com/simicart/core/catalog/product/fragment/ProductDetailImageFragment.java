package com.simicart.core.catalog.product.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.TouchImageViewTwo;

@SuppressLint("ClickableViewAccessibility")
public class ProductDetailImageFragment extends SimiFragment {
    protected ProductDetailParentController mParentController;
    private String mURL;

    public static ProductDetailImageFragment newInstance(SimiData data) {
        ProductDetailImageFragment fragment = new ProductDetailImageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setDelegate(ProductDetailParentController delegate) {
        mParentController = delegate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                Rconfig.getInstance().layout("core_product_detail_image"),
                container, false);
        if (getArguments() != null) {
            mURL = (String) getValueWithKey("url");
        }
        try {

            // ImageView
            final TouchImageViewTwo imv_image = (TouchImageViewTwo) rootView
                    .findViewById(Rconfig.getInstance().id(
                            "core_image_zoom_product_img"));

            imv_image.setDelegate(mParentController);

            if (DataLocal.isTablet) {
                WindowManager wm = (WindowManager) getActivity()
                        .getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                int p_widht = Utils.toDp((height * 2) / 3);
                int p_height = Utils.toDp(width);
                LayoutParams params = new LayoutParams(p_widht, p_height);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                imv_image.setLayoutParams(params);
            }

            if (null != mURL) {
                DrawableManager.fetchDrawableOnThreadForDetail(mURL, imv_image);
            }

        } catch (Exception e) {
//			Log.e("ProductDetailImageFragment - OncreateView:", e.getMessage());
        }
        return rootView;
    }
}
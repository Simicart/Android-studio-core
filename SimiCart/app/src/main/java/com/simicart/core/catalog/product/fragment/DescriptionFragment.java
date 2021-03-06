package com.simicart.core.catalog.product.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

@SuppressLint("SetJavaScriptEnabled")
public class DescriptionFragment extends SimiFragment {

    protected String mDescription;

    public static DescriptionFragment newInstance(SimiData data) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp
                * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                Rconfig.getInstance().layout(
                        "core_information_description_layout"), container,
                false);

        if (mData != null) {
            Product mProduct = (Product) getValueWithKey(Constants.KeyData.PRODUCT);
            if (mProduct != null) {
                mDescription = mProduct.getDecripition();
            }
        }

        WebView webView = (WebView) rootView.findViewById(Rconfig.getInstance()
                .id("webview"));
        webView.setBackgroundColor(AppColorConfig.getInstance().getAppBackground());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        if (mDescription.contains("<div")) {
            String text = "<html><body style=\"color:black;font-family:Helvetica;font-size:"
                    + convertDpToPixel(12, getActivity())
                    + "px;font-weight: lighter;\"'background-color:transparent' >"
                    + "<p align=\"justify\" style=\"font-weight: normal\">"
                    + mDescription + "</p>" + "</body></html>";
            // webView.getSettings().setTextZoom(400);
            webView.loadDataWithBaseURL(null, text, "text/html",
                    "charset=UTF-8", null);
        } else {
            webView.getSettings().setTextZoom(300);
            webView.loadDataWithBaseURL(null, mDescription, "text/html",
                    "charset=UTF-8", null);
        }
        return rootView;
    }

}

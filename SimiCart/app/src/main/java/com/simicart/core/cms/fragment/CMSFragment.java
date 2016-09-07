package com.simicart.core.cms.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;

import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class CMSFragment extends SimiFragment {

    protected String mContent;

    public static CMSFragment newInstance(SimiData data) {
        CMSFragment fragment = new CMSFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                Rconfig.getInstance().layout(
                        "core_information_description_layout"), container,
                false);

        if (mHashMapData.containsKey(KeyData.CMS_PAGE.CONTENT)) {
            mContent = (String) mHashMapData.get(KeyData.CMS_PAGE.CONTENT);
        }

        WebView webView = (WebView) rootView.findViewById(Rconfig.getInstance().id("webview"));
        webView.setBackgroundColor(AppColorConfig.getInstance().getAppBackground());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setSupportZoom(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadDataWithBaseURL(
                null,
                StringHTML("<html><body style=\"color:" + AppColorConfig.getInstance().getContentColor() + ";font-family:Helvetica;font-size:40px;\"'background-color:transparent' >"
                        + "<p align=\"justify\">"
                        + mContent
                        + "</p>"
                        + "</body></html>"), "text/html", "charset=UTF-8", null);
        return rootView;
    }

    private String StringHTML(String html) {
        String head = "<head><meta content='text/html; charset=UTF-8' http-equiv='Content-Type' /><style type='text/css'>body{font-size: 16px; font-family: 'Helvetica'}</style></head>";
        String HtmlString = "<html>" + head + "<body>" + html
                + "</body></html>";
        return HtmlString;
    }
}

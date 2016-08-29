package com.simicart.core.base.payment;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.HashMap;

/**
 * Created by frank on 8/28/16.
 */
public class WebviewPaymentComponent extends SimiComponent {

    protected HashMap<String, Object> mData;
    protected WebviewPaymentCallBack mCallBack;
    protected View mLoadingView;
    protected WebView wvPayment;
    protected String mUrl;
    protected String mKeySuccess;
    protected String mKeyFail;
    protected String mKeyError;
    protected String mMesssageSuccess;
    protected String mMessageFail;
    protected String mMessageError;


    public WebviewPaymentComponent(HashMap<String, Object> data, WebviewPaymentCallBack callBack) {
        super();
        mData = data;
        mCallBack = callBack;
    }

    protected void parseData() {

        if (mData.containsKey(KeyData.WEBVIEW_PAYMENT_COMPONENT.URL)) {
            mUrl = (String) mData.get(KeyData.WEBVIEW_PAYMENT_COMPONENT.URL);
        }

        if (mData.containsKey(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_SUCCESS)) {
            mKeySuccess = (String) mData.get(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_SUCCESS);
        }

        if (mData.containsKey(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_FAIL)) {
            mKeyFail = (String) mData.get(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_FAIL);
        }

        if (mData.containsKey(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_ERROR)) {
            mKeyError = (String) mData.get(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_ERROR);
        }

        if (mData.containsKey(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_SUCCESS)) {
            mMesssageSuccess = (String) mData.get(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_SUCCESS);
        }

        if (mData.containsKey(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_FAIL)) {
            mMessageFail = (String) mData.get(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_FAIL);
        }

        if (mData.containsKey(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_ERROR)) {
            mMessageError = (String) mData.get(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_ERROR);
        }

    }

    @Override
    public View createView() {
        rootView = findLayout("core_component_webview_payment");
        wvPayment = (WebView) findView("wv_payment");
        initLoadingView();
        configWebview();
        return rootView;
    }

    protected void initLoadingView() {
        mLoadingView = findLayout("core_base_loading");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLoadingView.setLayoutParams(params);
    }

    protected void configWebview() {
        wvPayment.getSettings().setJavaScriptEnabled(true);
        wvPayment.getSettings().setBuiltInZoomControls(true);
        wvPayment.getSettings().setLoadWithOverviewMode(true);
        wvPayment.getSettings().setUseWideViewPort(true);
        wvPayment.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvPayment.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        if (Utils.validateString(mUrl)) {
            wvPayment.loadUrl(mUrl);
            wvPayment.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    processUrl(url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    dimissLoading();
                }
            });
        }
    }

    protected void processUrl(String url) {
        if (Utils.validateString(mKeySuccess)) {
            if (url.contains(mKeySuccess)) {
                if (null != mCallBack && !mCallBack.onSuccess(url)) {
                    processSuccess();
                }
            }
        }

        if (Utils.validateString(mKeyFail)) {
            if (url.contains(mKeyFail)) {
                if (null != mCallBack && !mCallBack.onFail(url)) {
                    processFail();
                }
            }
        }

        if (Utils.validateString(mKeyError)) {
            if (url.contains(mKeyError)) {
                if (null != mCallBack && !mCallBack.onError(url)) {
                    processError();
                }
            }
        }

    }

    protected void processSuccess() {

    }

    protected void processFail() {

    }

    protected void processError() {

    }

    public void showLoading() {
        wvPayment.addView(mLoadingView);
    }

    public void dimissLoading() {
        wvPayment.removeView(mLoadingView);
    }

}

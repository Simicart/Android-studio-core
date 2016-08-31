package com.simicart.core.base.payment;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.notify.SimiNotify;
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
    protected String mKeyReview;
    protected String mMessageReview;


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

        if (mData.containsKey(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_REVIEW)) {
            mKeyReview = (String) mData.get(KeyData.WEBVIEW_PAYMENT_COMPONENT.KEY_REVIEW);
        }

        if (mData.containsKey(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_REVIEW)) {
            mMessageReview = (String) mData.get(KeyData.WEBVIEW_PAYMENT_COMPONENT.MESSAGE_REVIEW);
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
        Log.e("WebviewPaymentComponent","-----> URL " + mUrl);
        if (Utils.validateString(mUrl)) {
            showLoading();
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
                } else {
                    dimissLoading();
                    wvPayment.stopLoading();
                }
            }
        }

        if (Utils.validateString(mKeyFail)) {
            if (url.contains(mKeyFail)) {
                if (null != mCallBack && !mCallBack.onFail(url)) {
                    processFail();
                } else {
                    dimissLoading();
                    wvPayment.stopLoading();
                }
            }
        }

        if (Utils.validateString(mKeyError)) {
            if (url.contains(mKeyError)) {
                if (null != mCallBack && !mCallBack.onError(url)) {
                    processError();
                } else {
                    dimissLoading();
                    wvPayment.stopLoading();
                }
            }
        }

        if (Utils.validateString(mKeyReview)) {
            if (null != mCallBack && !mCallBack.onReview(url)) {
                processReview();
            } else {
                dimissLoading();
                wvPayment.stopLoading();
            }
        }

    }

    protected void processSuccess() {
        if (Utils.validateString(mMesssageSuccess)) {
            SimiNotify.getInstance().showToast(mMesssageSuccess);
        }
        dimissLoading();
        wvPayment.stopLoading();
        SimiManager.getIntance().backToHomeFragment();
    }

    protected void processFail() {
        if (Utils.validateString(mMessageFail)) {
            SimiNotify.getInstance().showToast(mMessageFail);
        }
        dimissLoading();
        wvPayment.stopLoading();
        SimiManager.getIntance().backToHomeFragment();

    }

    protected void processError() {
        if (Utils.validateString(mMessageError)) {
            SimiNotify.getInstance().showToast(mMessageError);
        }
        dimissLoading();
        wvPayment.stopLoading();
        SimiManager.getIntance().backToHomeFragment();
    }

    protected void processReview() {
        if (Utils.validateString(mMessageReview)) {
            SimiNotify.getInstance().showError(mMessageReview);
        }
        dimissLoading();
        wvPayment.stopLoading();
        SimiManager.getIntance().backToHomeFragment();
    }

    public void showLoading() {
        wvPayment.addView(mLoadingView);
    }

    public void dimissLoading() {
        wvPayment.removeView(mLoadingView);
    }

}

package com.simicart.plugins.klarna.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.plugins.klarna.delegate.KlarnaDelegate;
import com.simicart.plugins.klarna.model.KlarnaModel;

import org.json.JSONArray;

public class KlarnaController extends SimiController {

    protected KlarnaDelegate mDelegate;
    protected JSONArray json;

    public void setKlarnaDelegate(KlarnaDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onStart() {
        requestGetParam();
    }

    protected void requestGetParam() {
        final KlarnaModel model = new KlarnaModel();
        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                json = model.getDataKlarna();
                onLoadWebView();
            }
        });
        model.request();
    }

    protected void onLoadWebView() {
        mDelegate.onLoadWebView(json);
    }

    @Override
    public void onResume() {
        onLoadWebView();

    }

}

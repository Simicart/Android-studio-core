package com.simicart.plugins.klarna.delegate;

import com.simicart.core.base.delegate.SimiDelegate;

import org.json.JSONArray;

public interface KlarnaDelegate extends SimiDelegate {

    public void onLoadWebView(JSONArray json);

}

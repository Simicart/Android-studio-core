package com.simicart.network.request;

import com.simicart.network.callback.RequestCallBack;
import com.simicart.network.response.SimiResponse;

public class SimiJSONRequest extends SimiRequest {

    public SimiJSONRequest(String url, RequestCallBack callBack) {
        super(url, callBack);
        this.url = url;
    }

    String url;

    @Override
    public SimiResponse parseNetworkResponse(SimiNetworkResponse response) {
        if (null != response) {
            byte[] data = response.getData();
            if (null != data && data.length > 0) {
                String content = new String(data);
                SimiResponse coreResponse = new SimiResponse();
                coreResponse.setData(content);
                return coreResponse;
            }
        }
        return null;
    }

    @Override
    public void deliveryCoreResponse(SimiResponse response) {
        if (null != response) {
            if (response.parse()) {
                mRequestCallBack.onCallBack(response, true);
            } else {
                mRequestQueue.finish(this);
                mRequestCallBack.onCallBack(response, false);
            }

        } else {
            mRequestQueue.finish(this);
            mRequestCallBack.onCallBack(response, false);
        }

    }
}

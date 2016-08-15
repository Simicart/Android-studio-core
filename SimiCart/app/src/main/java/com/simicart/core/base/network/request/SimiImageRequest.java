package com.simicart.core.base.network.request;

import com.simicart.core.base.delegate.RequestCallBack;
import com.simicart.core.base.network.response.SimiResponse;

public class SimiImageRequest extends SimiRequest {

	public SimiImageRequest(String url, RequestCallBack delegate) {
		super(url, delegate);
	}

	@Override
	public SimiResponse parseNetworkResponse(SimiNetworkResponse response) {
		byte[] data = response.getData();

		return null;
	}

	@Override
	public void deliveryCoreResponse(SimiResponse response) {
		if (null != response) {
			mDelegate.callBack(response, true);
		} else {
			mDelegate.callBack(response, false);
		}

	}

}

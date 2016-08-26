package com.simicart.core.base.network.request;

import android.util.Log;

import com.simicart.core.base.delegate.RequestCallBack;
import com.simicart.core.base.network.response.SimiResponse;
import com.simicart.core.common.Utils;

public class SimiJSONRequest extends SimiRequest {

	public SimiJSONRequest(String url, RequestCallBack delegate) {
		super(url, delegate);
		this.url = url;
	}

	String url;

	@Override
	public SimiResponse parseNetworkResponse(SimiNetworkResponse response) {
		Log.e("SimiJSONRequest ","parseNetworkResponse 001");
		if (null != response) {
			byte[] data = response.getData();
			if (null != data && data.length > 0) {
				String content = new String(data);
				SimiResponse simiResponse = new SimiResponse();
				simiResponse.setData(content);
				Log.e("SimiJSONRequest ","parseNetworkResponse 002");
				return simiResponse;
			}
		}
		return null;
	}

	@Override
	public void deliveryCoreResponse(SimiResponse response) {
		if (null != response) {
			if (response.parse()) {
				Log.e("SimiJSONRequest ","deliveryCoreResponse 001");
				mCallBack.callBack(response, true);
			} else {
				Log.e("SimiJSONRequest ","deliveryCoreResponse 002");
				mRequestQueue.finish(this);

				String message = response.getMessage();
				if (!Utils.validateString(message) && isShowNotify) {
//					message = Config.getInstance().getText(
//							"Some errors occurred. Please try again later");
				}

				if (isShowNotify) {
//					SimiManager.getIntance().showNotify(message);
				}
				mCallBack.callBack(response, false);
			}

		} else {
			mRequestQueue.finish(this);
			mCallBack.callBack(response, false);
		}

	}
}

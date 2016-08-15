package com.simicart.core.base.delegate;

import com.simicart.core.base.network.response.SimiResponse;

public interface RequestCallBack {
	public  void callBack(SimiResponse simiResponse, boolean isSuccess);
}

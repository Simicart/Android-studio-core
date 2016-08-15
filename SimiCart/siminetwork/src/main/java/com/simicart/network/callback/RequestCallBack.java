package com.simicart.network.callback;

import com.simicart.network.response.SimiResponse;

/**
 * Created by frank on 8/12/16.
 */
public interface RequestCallBack {

    public void onCallBack(SimiResponse response,boolean isSuccess);

}

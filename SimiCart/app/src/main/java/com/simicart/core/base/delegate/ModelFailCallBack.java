package com.simicart.core.base.delegate;

import com.simicart.core.base.network.error.SimiError;

/**
 * Created by frank on 8/12/16.
 */
public interface ModelFailCallBack {
    public void onFail(SimiError error);
}

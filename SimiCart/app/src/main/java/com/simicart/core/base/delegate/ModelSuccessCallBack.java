package com.simicart.core.base.delegate;

import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.response.SimiResponse;

/**
 * Created by frank on 8/12/16.
 */
public interface ModelSuccessCallBack {
    public void onSuccess(SimiCollection collection);
}

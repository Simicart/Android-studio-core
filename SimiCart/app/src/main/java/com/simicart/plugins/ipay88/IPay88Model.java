package com.simicart.plugins.ipay88;

import com.simicart.core.base.model.SimiModel;

/**
 * Created by frank on 30/08/2016.
 */
public class IPay88Model extends SimiModel {
    @Override
    protected void setUrlAction() {
        mUrlAction = "simiipay88/api/update_payment";
    }
}

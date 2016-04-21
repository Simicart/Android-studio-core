package com.simicart.core.splashscreen.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class DeepLinkModel extends SimiModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void paserData() {
        try {
            JSONObject obj = this.getDataJSON().getJSONObject("data");
            collection = new SimiCollection();
            collection.setJSON(mJSON);
            if (null != obj) {
                SimiEntity entity = new SimiEntity();
                entity.setJSONObject(obj);
                collection.addEntity(entity);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void setShowNotifi() {
        isShowNotify = false;
    }

    @Override
    protected void setUrlAction() {
        url_action = Constants.DEEP_LINK;
    }
}

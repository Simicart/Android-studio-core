package com.simicart.core.splashscreen.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

/**
 * Created by frank on 8/16/16.
 */
public class DeepLinkEntity extends SimiEntity {
    protected static DeepLinkEntity instance;

    public static DeepLinkEntity getInstance() {
        if (null == instance) {
            instance = new DeepLinkEntity();
        }
        return instance;
    }

    protected String mID = "";
    protected String mName = "";
    protected boolean hasChild;
    protected int mType = 0;

    private final String id = "id";
    private final String type = "type";
    private final String has_child = "has_child";


    @Override
    public void parse() {

        if (hasKey(id)) {
            mID = getData(id);
        }

        if (hasKey(type)) {
            String linkType = getData(type);
            if (Utils.validateString(linkType)) {
                try {
                    linkType = linkType.trim();
                    int iType = Integer.parseInt(linkType);
                    setType(iType);
                } catch (Exception e) {

                }
            }
        }

        if (hasKey(has_child)) {
            String isChild = getData(has_child);
            if (Utils.TRUE(isChild)) {
                setHasChild(true);
            }
        }

    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }
}

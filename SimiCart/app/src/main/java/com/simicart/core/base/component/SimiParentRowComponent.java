package com.simicart.core.base.component;

import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.common.Utils;

import java.util.ArrayList;

/**
 * Created by frank on 27/08/2016.
 */
public class SimiParentRowComponent extends SimiRowComponent {

    protected ArrayList<SimiRowComponent> mListRow;
    protected int rightMargin = Utils.toDp(5);

    public SimiParentRowComponent(ArrayList<SimiRowComponent> listRow) {
        super();
        mType = TYPE_ROW.PARENT;
        mListRow = listRow;
    }

    @Override
    public View createView() {
        LinearLayout llBody = new LinearLayout(mContext);
        llBody.setOrientation(LinearLayout.HORIZONTAL);
        int weight = mListRow.size();
        llBody.setWeightSum(weight);

        for (int i = 0; i < weight; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            params.rightMargin = rightMargin;
            View view = mListRow.get(i).createView();
            llBody.addView(view, params);
        }
        return llBody;
    }
}

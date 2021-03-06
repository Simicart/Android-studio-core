package com.simicart.plugins.addressautofill.container;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.LinearLayout;

public class MyMapFragmentContainer extends LinearLayout {

    public MyMapFragmentContainer(Context context) {
        super(context);
    }

    public MyMapFragmentContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMapFragmentContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }
}

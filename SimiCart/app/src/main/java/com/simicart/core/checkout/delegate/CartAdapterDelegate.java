package com.simicart.core.checkout.delegate;

import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import java.util.ArrayList;

public interface CartAdapterDelegate {
    public OnTouchListener getOnTouchListener(final int position);

    public OnClickListener getClickQtyItem(final int position, final int qty, final int min, final int max);

    public OnClickListener getClickItemCartListener(final int position, ArrayList<String> listID);
}

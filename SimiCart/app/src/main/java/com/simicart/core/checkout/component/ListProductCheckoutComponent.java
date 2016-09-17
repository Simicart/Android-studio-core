package com.simicart.core.checkout.component;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.checkout.adapter.ListProductCheckoutAdapter;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by Crabby PC on 7/1/2016.
 */
public class ListProductCheckoutComponent extends SimiComponent {

    protected ArrayList<Cart> listQuotes;
    protected String title;

    public ListProductCheckoutComponent(ArrayList<Cart> listQuotes, String title) {
        super();
        this.listQuotes = listQuotes;
        this.title = title;
    }

    @Override
    public View createView() {
        rootView = findLayout("core_component_layout");

        initTitle();

        initListProducts();

        return rootView;
    }

    protected void initTitle() {
        TextView tvTitle = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_title"));
        int bgColor = AppColorConfig.getInstance().getSectionColor();
        tvTitle.setBackgroundColor(bgColor);
        int bgText = AppColorConfig.getInstance().getContentColor();
        tvTitle.setTextColor(bgText);
        if (title != null) {
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    protected void initListProducts() {
        LinearLayout llProducts = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_body_component"));
        RecyclerView rvListProducts = new RecyclerView(mContext);
        rvListProducts.setNestedScrollingEnabled(false);
        LinearLayoutManager llManager = new LinearLayoutManager(mContext);
        rvListProducts.setLayoutManager(llManager);
        ListProductCheckoutAdapter adapter = new ListProductCheckoutAdapter(listQuotes);
        rvListProducts.setAdapter(adapter);
        llProducts.addView(rvListProducts);
    }

}

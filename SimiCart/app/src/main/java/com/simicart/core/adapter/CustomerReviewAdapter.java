package com.simicart.core.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.entity.CustomerReview;
import com.simicart.core.catalog.product.fragment.CustomerReviewMoreFragment;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

public class CustomerReviewAdapter extends ArrayAdapter<CustomerReview> {

    protected Context mContext;
    protected LayoutInflater mInflater;
    private ArrayList<CustomerReview> mCustomerReview;

    public CustomerReviewAdapter(Context context, int resource,
                                 ArrayList<CustomerReview> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.setmCustomerReview(objects);
        this.mInflater = LayoutInflater.from(context);
    }

    public ArrayList<CustomerReview> getmCustomerReview() {
        return mCustomerReview;
    }

    public void setmCustomerReview(ArrayList<CustomerReview> mCustomerReview) {
        this.mCustomerReview = mCustomerReview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = this.mInflater.inflate(
                Rconfig.getInstance().layout(
                        "core_information_customer_review_item"), null);
        final CustomerReview customerReview = getmCustomerReview()
                .get(position);

        RatingBar ratingBar = (RatingBar) convertView.findViewById(Rconfig
                .getInstance().id("rtb_review"));
        ratingBar.setRating(Float.parseFloat(customerReview.getRate()));
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(
                AppColorConfig.getInstance().getKeyColor(), PorterDuff.Mode.SRC_ATOP);

        TextView title = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("tv_reviewTitle"));
        title.setText(customerReview.getTitle());
        title.setTextColor(AppColorConfig.getInstance().getContentColor());

        TextView content = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("tv_reviewContent"));
        content.setTextColor(AppColorConfig.getInstance().getContentColor());
        content.setText(customerReview.getContent());

        TextView date = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("tv_reviewDate"));
        date.setTextColor(AppColorConfig.getInstance().getContentColor());
        date.setText(customerReview.getTime());

        TextView review_customer = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("tv_nameReviewCustomer"));
        review_customer.setTextColor(AppColorConfig.getInstance().getContentColor());
        review_customer.setText(SimiTranslator.getInstance().translate("By") + " "
                + customerReview.getCustomer_name());

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CustomerReviewMoreFragment fragment = CustomerReviewMoreFragment
                        .newInstance(customerReview);
//				fragment.setCustomerReview(customerReview);
//				SimiManager.getIntance().addPopupFragment(fragment);
            }
        });
        return convertView;
    }

}

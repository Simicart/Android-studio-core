package com.simicart.theme.matrixtheme.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.Rconfig;
import com.simicart.theme.matrixtheme.home.entity.OrderProduct;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by frank on 8/20/16.
 */
public class ThemeOneSpotProductAdapter extends RecyclerView.Adapter<ThemeOneSpotProductAdapter.ViewHolder> {

    protected ArrayList<OrderProduct> mListProduct;
    protected Context mContext;
    protected int dimension;

    public ThemeOneSpotProductAdapter(ArrayList<OrderProduct> products) {
        mListProduct = products;
        dimension = Utils.SCREEN_WIDTH / 3;
       // dimension = Utils.toDp(width);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int idView = Rconfig.getInstance().layout("theme_one_adapter_spot_product");
        View itemView = inflater.inflate(idView, null);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimension, dimension);
        params.rightMargin = Utils.toDp(5);
        holder.rltSpotProduct.setLayoutParams(params);

        final OrderProduct product = mListProduct.get(position);

        // spot name
        String name = product.getSpotName();
        if (Utils.validateString(name)) {
            name = name.toUpperCase();
            holder.tvName.setText(name);
        }

        // spot images
        ArrayList<String> images = product.getUrlImage();
        if (null != images && images.size() > 0) {
            int size = images.size();
            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    ImageView imageSpot = addAnImage(images.get(i));
                    holder.vfpImage.addView(imageSpot);
                }
                startAnimation(holder.vfpImage);
            } else {
                ImageView imageSpot = addAnImage(images.get(0));
                holder.vfpImage.addView(imageSpot);
            }
        }

        holder.rltSpotProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> hm = new HashMap<>();
                hm.put(KeyData.CATEGORY_DETAIL.TYPE, ValueData.CATEGORY_DETAIL.CUSTOM);
                hm.put("key", product.getSpotKey());
                hm.put(KeyData.CATEGORY_DETAIL.CATE_NAME, product.getSpotName());
                hm.put(KeyData.CATEGORY_DETAIL.CUSTOM_URL, "themeone/api/get_spot_products");
                SimiManager.getIntance().openCategoryDetail(hm);
            }
        });

    }

    protected ImageView addAnImage(String urlImage) {
        ImageView imgCate = new ImageView(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        imgCate.setLayoutParams(params);
//        int idLogo = Rconfig.getInstance().drawable("default_logo");
//        imgCate.setImageResource(idLogo);
        imgCate.setScaleType(ImageView.ScaleType.FIT_XY);
        DrawableManager.fetchDrawableOnThread(urlImage, imgCate);
        return imgCate;
    }


    protected void startAnimation(ViewFlipper vfpImage) {
        vfpImage.setFlipInterval(4500);
        int idAniDown = Rconfig.getInstance().getId("in_from_down", "anim");
        vfpImage.setInAnimation(mContext, idAniDown);
        int idAniUp = Rconfig.getInstance().getId("out_to_up", "anim");
        vfpImage.setOutAnimation(mContext, idAniUp);
        vfpImage.startFlipping();
    }

    @Override
    public int getItemCount() {
        return mListProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        RelativeLayout rltSpotProduct;
        ViewFlipper vfpImage;

        public ViewHolder(View itemView) {
            super(itemView);
            rltSpotProduct = (RelativeLayout) itemView.findViewById(Rconfig.getInstance().id("rlt_spot_product"));
            vfpImage = (ViewFlipper) itemView.findViewById(Rconfig.getInstance().id("vfp_category"));
            tvName = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_category"));
        }
    }


}

package com.simicart.theme.matrixtheme.home.component;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by frank on 19/08/2016.
 */
public class CateHomeThemeOneComponent extends SimiComponent {

    protected ViewFlipper vfpCate;
    protected TextView tvCatName;
    protected TextView tvMore;
    protected Category mCategory;
    protected Context mContext;

    public CateHomeThemeOneComponent(Category categories) {
        mCategory = categories;
        mContext = SimiManager.getIntance().getCurrentActivity();
    }

    @Override
    public View createView() {
        int idView = Rconfig.getInstance().layout("theme_one_component_cate_home");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        rootView = inflater.inflate(idView, null);

        vfpCate = (ViewFlipper) findView("vfp_category");
        showImageCate();

        // category name
        tvCatName = (TextView) findView("tv_category");
        String name = mCategory.getCategoryName();
        if (Utils.validateString(name)) {
            tvCatName.setText(name);
            tvCatName.setTextColor(Color.parseColor("#000000"));
        }


        // view more
        tvMore = (TextView) findView("tv_viewmore");
        String textViewMore = SimiTranslator.getInstance().translate("View more") + ">>";
        tvMore.setText(textViewMore);
        tvMore.setTextColor(Color.parseColor("#4E4E4E"));

        return rootView;
    }

    protected void showImageCate() {
        ArrayList<String> images = mCategory.getListImage();
        if (null != images && images.size() > 0) {
            if (images.size() > 1) {
                for (int i = 0; i < images.size(); i++) {
                    String urlImage = images.get(i);
                    addAnImage(urlImage);
                }
                startAnimation();
            } else {
                String urlImage = images.get(0);
                addAnImage(urlImage);
            }
        }
    }

    protected void addAnImage(String urlImage) {
        ImageView imgCate = new ImageView(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        imgCate.setLayoutParams(params);
        int idLogo = Rconfig.getInstance().drawable("default_logo");
        imgCate.setImageResource(idLogo);
        imgCate.setScaleType(ImageView.ScaleType.FIT_XY);
        DrawableManager.fetchDrawableOnThread(urlImage, imgCate);
        vfpCate.addView(imgCate);
    }


    protected void startAnimation() {
        vfpCate.setFlipInterval(4500);
        int idAniDown = Rconfig.getInstance().getId("in_from_down", "anim");
        vfpCate.setInAnimation(mContext, idAniDown);
        int idAniUp = Rconfig.getInstance().getId("out_to_up", "anim");
        vfpCate.setOutAnimation(mContext, idAniUp);
        vfpCate.startFlipping();
    }

}

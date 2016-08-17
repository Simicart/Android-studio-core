package com.simicart.core.splashscreen.block;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class SplashBlock {

    protected View rootView;
    protected Context mContext;

    public SplashBlock(View view, Context context) {
        rootView = view;
        mContext = context;
    }

    @SuppressLint("DefaultLocale")
    public void initView() {
        if (!Config.getInstance().isFullSplash()) {
            rootView.setBackgroundColor(Config.getInstance().getColorSplash());
        }

        // label demo
        if (Config.getInstance().isDemoVersion()) {
            createTextDemo();
        }

        // show loading screen
        createLoading();


        // images for logo
        createLogo();

    }

    protected void createTextDemo() {
        String textdemo = "This is a demo version.<br/>This text will be removed from live app.";
        TextView tv_demo = (TextView) rootView.findViewById(Rconfig
                .getInstance().getId(mContext, "tv_demo",
                        "id"));
        tv_demo.setVisibility(View.VISIBLE);
        tv_demo.setText(Html.fromHtml(textdemo));
        tv_demo.setTextColor(Color.parseColor("#000000"));

    }


    protected void createLoading() {
        ProgressBar prg_loading = (ProgressBar) rootView.findViewById(Rconfig
                .getInstance().getId(mContext,
                        "prb_loading", "id"));
        prg_loading.setVisibility(View.VISIBLE);
    }

    protected void createLogo() {
        ImageView img_logo = (ImageView) rootView.findViewById(Rconfig
                .getInstance().getId(mContext, "img_logo",
                        "id"));
        int idResourceSplash = Rconfig.getInstance().drawable("default_splash_screen");
        int reqWidth = 600;
        int reqHeight = 600;
        if (Config.getInstance().isFullSplash()) {
            reqWidth = Utils.SCREEN_WIDTH;
            reqHeight = Utils.SCREEN_HEIGHT;
        }
        Bitmap bitmap = decodeSampledBitmapFromResource(idResourceSplash, reqWidth, reqHeight);
        img_logo.setImageBitmap(bitmap);
    }

    protected Bitmap decodeSampledBitmapFromResource(int resId, int reqWidth, int reqHeight) {

        Resources resources = mContext.getResources();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    protected int calculateInSampleSize(BitmapFactory.Options options,
                                        int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}

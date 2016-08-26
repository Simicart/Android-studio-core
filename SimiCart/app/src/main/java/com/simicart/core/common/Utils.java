package com.simicart.core.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout.LayoutParams;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.GenderConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static boolean ISTABLET = false;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void getDeviceInfor() {
        Activity activity = SimiManager.getIntance().getCurrentActivity();
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SCREEN_WIDTH = size.x;
        SCREEN_HEIGHT = size.y;

        ISTABLET = isTablet(activity);

    }


    @SuppressLint("NewApi")
    public static int generateViewId() {

        if (Build.VERSION.SDK_INT < 17) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1;
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }

    }

    public static Bitmap scaleToFill(Bitmap b, int width, int height) {
        float factorH = height / (float) b.getWidth();
        float factorW = width / (float) b.getWidth();
        float factorToUse = (factorH > factorW) ? factorW : factorH;
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse),
                (int) (b.getHeight() * factorToUse), true);
    }

    public static void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views
        // with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources()
                .getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight
                            - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources()
                .getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public static String getLabelGender(String value) {
        for (GenderConfig genderConfig : ConfigCustomerAddress.getInstance().getGenderConfigs()) {
            if (genderConfig.getValue().equals(value)) {
                return genderConfig.getLabel();
            }
        }
        return "";
    }

    public static String getValueGender(String label) {
        for (GenderConfig genderConfig : ConfigCustomerAddress.getInstance().getGenderConfigs()) {
            if (genderConfig.getLabel().equals(label)) {
                return genderConfig.getValue();
            }
        }
        return "";
    }

    public static final boolean TRUE(String data) {
        if (!Utils.validateString(data)) {
            return false;
        }

        data = data.toLowerCase();

        if (data.equals("no")) {
            return false;
        }

        if (data.equals("0")) {
            return false;
        }

        if (data.equals("false")) {
            return false;
        }


        return true;
    }

    // hideKeyboard
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getRootView().getWindowToken(), 0);
    }


    public static String capitalizes(String source) {
        StringBuilder builder = new StringBuilder();
        source = source.toLowerCase();
        String[] arr = source.split(" ");
        for (String string : arr) {
            String tmp = string.substring(0, 1).toUpperCase()
                    + string.substring(1) + " ";
            builder.append(tmp);
        }
        return builder.toString().trim();
    }

    public static int getValueDp(int value) {
        float unit = SimiManager.getIntance().getCurrentActivity()
                .getResources().getDisplayMetrics().density;
        return (int) (value * unit + 0.5f);
    }

    public static void changeTextview(String color, float size) {

    }

    public static boolean isTablet(Context context) {

        String type_device = context.getString(Rconfig.getInstance().getId(
                context, "type_device", "string"));
        if (type_device.equals("phone")) {
            return false;
        }
        return true;

    }


    public static boolean validateString(String content) {
        if (null == content) {
            return false;
        }
        if (content.equals("")) {
            return false;
        }
        if (content.equals("null")) {
            return false;
        }

        return true;
    }


    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            if (s != null) {
                digest.update(s.getBytes());
            }
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static int toDp(int value) {
        Context context = SimiManager.getIntance().getCurrentActivity();
        float unit = context.getResources().getDisplayMetrics().density;
        int result = (int) (value / unit + 0.5f);
        return result;
    }

    public static int toPixel(int value) {
        DisplayMetrics metrics = SimiManager.getIntance().getCurrentActivity().getResources().getDisplayMetrics();
        int px = value * ((int) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static int getScreenHeight() {
        return SimiManager.getIntance().getCurrentActivity().getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth() {
        return SimiManager.getIntance().getCurrentActivity().getResources().getDisplayMetrics().widthPixels;
    }


}

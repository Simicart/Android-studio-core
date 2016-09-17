package com.simicart.core.base.drawImage;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.widget.ImageView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.network.request.TLSSocketFactory;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by trueplus on 6/1/16.
 */
public class SimiDrawImage {

    protected int mReqWidth;
    protected int mReqHeight;


    public void drawImage(ImageView img, String url) {
        this.drawImage(img, url, 128, 128);
    }

    public void drawImage(final ImageView img, final String url, int reqWidth, int reqHeight) {
        mReqHeight = reqHeight;
        mReqWidth = reqWidth;

//        Bitmap cacheBitmap = getBitmapFromCache(url);
//        if (null != cacheBitmap) {
//            img.setImageBitmap(cacheBitmap);
//            return;
//        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Bitmap bitmap = (Bitmap) message.obj;
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                    addBitmapToCache(url, bitmap);
                } else {
                    Resources resource = SimiManager.getIntance()
                            .getCurrentActivity().getResources();
                    bitmap = BitmapFactory.decodeResource(resource, Rconfig
                            .getInstance().drawable("default_icon"));
                    bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
                    img.setImageBitmap(bitmap);
                }
            }
        };

        getBitmapWithOption(handler, url);
    }

//    protected Bitmap getBitmapFromCache(String url) {
//        String key = getKeyForCache(url);
//        return SimiManager.getIntance().getImageCache().get(key);
//    }

    protected void addBitmapToCache(String url, Bitmap bitmap) {
        String key = getKeyForCache(url);
//        SimiManager.getIntance().getImageCache().put(key, bitmap);

    }

    protected String getKeyForCache(String url) {
        String baseUrl = Config.getInstance().getBaseUrl();
        if (url.contains(baseUrl)) {
            url = url.replace(baseUrl, "");
        }
        return Utils.md5(url);
    }


    public void getBitmapWithOption(final Handler handler, final String url) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromServer(url);
                if (bitmap != null) {
                    Message message = handler.obtainMessage(1, bitmap);
                    handler.sendMessage(message);
                }
            }
        };
        thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
    }

    public Bitmap getBitmapFromServer(String url) {
        Bitmap bitmap = null;
        try {
            URL url_con = new URL(url);
            HttpURLConnection conn = null;
            if (url.contains("https")) {
                conn = (HttpsURLConnection) url_con.openConnection();
                ((HttpsURLConnection) conn)
                        .setSSLSocketFactory(new TLSSocketFactory());
            } else {
                conn = (HttpURLConnection) url_con.openConnection();
            }

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            String secret_key = Config.getInstance().getSecretKey();
            String token = "Bearer " + secret_key;
            conn.setRequestProperty("Authorization", token);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();

            int status = conn.getResponseCode();
            if (status < 300) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                BufferedInputStream bs = new BufferedInputStream(is);
                byte[] buff = new byte[1024];
                int read = 0;
                while ((read = bs.read(buff)) > 0) {
                    bos.write(buff, 0, read);
                    buff = new byte[1024];
                }
                byte[] bytes = bos.toByteArray();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                options.inSampleSize = calculateInSampleSize(options, mReqWidth, mReqHeight);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                return bitmap;
            }
        } catch (Exception e) {

            return null;
        }


        return bitmap;
    }

    protected int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

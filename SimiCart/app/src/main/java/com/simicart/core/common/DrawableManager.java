package com.simicart.core.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.network.request.TLSSocketFactory;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.imagesimicart.SimiImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

public class DrawableManager {

    protected static LruCache<String, Bitmap> mMemoryCache;
    protected static boolean isInitial = false;

    public static void init() {
        if (!isInitial) {
            if (null == mMemoryCache) {
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                final int cacheSize = maxMemory / 8;
                mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                    @Override
                    protected int sizeOf(String key, Bitmap bitmap) {
                        return bitmap.getByteCount() / 1024;
                    }
                };
            }

            isInitial = true;
        }
    }


    /**
     * Draw icon on Slide Menu ( icon of CMS)
     *
     * @param urlString
     * @param imageView
     * @param context
     * @param color
     */

    public static void fetchDrawableIConOnThread(final String urlString,
                                                 final ImageView imageView, final Context context, final int color) {

        init();

        Bitmap cache_bitMap = getBitmapFromMemCache(urlString);

        if (null != cache_bitMap) {
            Resources resource = SimiManager.getIntance().getCurrentActivity()
                    .getResources();
            Drawable drawable = new BitmapDrawable(resource, cache_bitMap);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

            imageView.setImageDrawable(drawable);
            return;
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Bitmap bitmap = (Bitmap) message.obj;
                addBitmapToMemoryCache(urlString, bitmap);
                Resources resource = SimiManager.getIntance()
                        .getCurrentActivity().getResources();
                Drawable drawable = null;
                if (bitmap != null) {
                    drawable = new BitmapDrawable(resource, bitmap);
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                } else {
                    bitmap = BitmapFactory.decodeResource(resource, Rconfig
                            .getInstance().drawable("default_icon"));
                    bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
                    drawable = new BitmapDrawable(context.getResources(),
                            bitmap);
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
                if (null != drawable) {
                    imageView.setImageDrawable(drawable);
                }

            }
        };

        getBitmap(handler, urlString);
    }

    @SuppressWarnings("deprecation")
    public static void fetchDrawableOnThread(final String urlString,
                                             final TextView textview) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Bitmap bitmap = (Bitmap) message.obj;
                if (bitmap != null) {
                    Resources resource = SimiManager.getIntance()
                            .getCurrentActivity().getResources();
                    Drawable drawable = new BitmapDrawable(resource, bitmap);
                    textview.setBackgroundDrawable(drawable);
                } else {
                    textview.setBackgroundResource(Rconfig.getInstance()
                            .drawable("default_icon"));
                }

            }
        };

        getBitmap(handler, urlString);
    }

    public static void fetchDrawableOnThread(final String urlString,
                                             final ImageView imageView) {

        init();

        Bitmap cache_bitMap = getBitmapFromMemCache(urlString);

        if (null != cache_bitMap) {
            imageView.setImageBitmap(cache_bitMap);
            return;
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Bitmap bitmap = (Bitmap) message.obj;
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    addBitmapToMemoryCache(urlString, bitmap);
                } else {
                    Resources resources = SimiManager.getIntance()
                            .getCurrentActivity().getResources();
                    bitmap = BitmapFactory.decodeResource(resources, Rconfig
                            .getInstance().drawable("default_icon"));
                    bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
                    imageView.setImageBitmap(bitmap);
                    bitmap = null;
                }
            }
        };

        getBitmap(handler, urlString);
    }

    /**
     * Draw full image for Product Detail
     *
     * @param urlString
     * @param imageView
     */
    public static void fetchDrawableOnThreadForDetail(final String urlString,
                                                      final ImageView imageView) {

        init();


        Bitmap cache_bitMap = getBitmapFromMemCache(urlString);

        Display display = SimiManager.getIntance().getCurrentActivity()
                .getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int w = (size.x * 4) / 5;
        final int h = (size.y * 4) / 5;

        if (null != cache_bitMap) {
//            Bitmap bMapRotate = Utils.scaleToFill(cache_bitMap, w, h);
            imageView.setImageBitmap(cache_bitMap);
            return;
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Bitmap bitmap = (Bitmap) message.obj;
                if (bitmap != null) {
                    try {
                       // Bitmap bMapRotate = Utils.scaleToFill(bitmap, w, h);
                        imageView.setImageBitmap(bitmap);
                        addBitmapToMemoryCache(urlString, bitmap);
                        bitmap = null;
                     //   bMapRotate = null;
                    } catch (Exception e) {

                    }
                } else {
                    Resources resources = SimiManager.getIntance()
                            .getCurrentActivity().getResources();
                    bitmap = BitmapFactory.decodeResource(resources, Rconfig
                            .getInstance().drawable("default_icon"));
                    Matrix mat = new Matrix();
                    mat.postRotate(-90);
                    Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), mat, true);
                    imageView.setImageBitmap(bMapRotate);
                    bitmap = null;
                }
            }
        };

        getBitmap(handler, urlString);
    }

    /**
     * Draw image for ZTheme
     *
     * @param urlImage
     * @param simiImageView
     */
    public static void fetchDrawableOnThread(final String urlImage,
                                             final SimiImageView simiImageView) {
        init();

        Bitmap cache_bitMap = getBitmapFromMemCache(urlImage);

        if (null != cache_bitMap) {
            simiImageView.setImageBitmap(cache_bitMap);
            return;
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Bitmap bitmap = (Bitmap) message.obj;
                if (bitmap != null) {
                    simiImageView.setImageBitmap(bitmap);
                    addBitmapToMemoryCache(urlImage, bitmap);
                } else {
                    Resources resources = SimiManager.getIntance()
                            .getCurrentActivity().getResources();
                    bitmap = BitmapFactory.decodeResource(resources, Rconfig
                            .getInstance().drawable("default_icon"));
                    bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
                    simiImageView.setImageBitmap(bitmap);
                    bitmap = null;
                }
            }
        };

        getBitmap(handler, urlImage);
    }

    public static void fetchDrawableOnThread(final String urlString,
                                             final ImageView imageView, int reqWidth, int reqHeight) {

        init();

        Bitmap cache_bitMap = getBitmapFromMemCache(urlString);

        if (null != cache_bitMap) {
            imageView.setImageBitmap(cache_bitMap);
            return;
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Bitmap bitmap = (Bitmap) message.obj;
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    addBitmapToMemoryCache(urlString, bitmap);
                } else {
                    Resources resources = SimiManager.getIntance()
                            .getCurrentActivity().getResources();
                    bitmap = BitmapFactory.decodeResource(resources, Rconfig
                            .getInstance().drawable("default_icon"));
                    bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
                    imageView.setImageBitmap(bitmap);
                    bitmap = null;
                }
            }
        };

        getBitmap(handler, urlString,reqWidth,reqHeight);
    }

    public static void getBitmap(final Handler handler, final String urlString, final int reqWidth, final int reqHeight) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = excutePostForBitMap(urlString, reqWidth, reqHeight);
                if (bitmap != null) {
                    Message message = handler.obtainMessage(1, bitmap);
                    handler.sendMessage(message);
                }
            }
        };
        thread.start();
    }

    public static Bitmap excutePostForBitMap(String url, int reqWidth,
                                             int reqHeight) {
        try {
            Bitmap bitMap = null;
            URL url_con = new URL(url);
            HttpURLConnection conn = null;
            if (url.contains("https")) {
                conn = (HttpsURLConnection) url_con.openConnection();
                try {
                    ((HttpsURLConnection) conn)
                            .setSSLSocketFactory(new TLSSocketFactory());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            } else {
                conn = (HttpURLConnection) url_con.openConnection();
            }

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Token", Config.getInstance()
                    .getSecretKey());
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();

            int status = conn.getResponseCode();

            Log.e("DrawableManager ", " URL " + url + " CODE " + status);

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

                if (reqWidth <= 0) {
                    reqWidth = 128;
                }
                if (reqHeight <= 0) {
                    reqHeight = 128;
                }

                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);
                option.inSampleSize = calculateInSampleSize(option, reqWidth,
                        reqHeight);
                option.inJustDecodeBounds = false;
                bitMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        option);
                return bitMap;
            } else {
                Log.e("Drawable Manager ", "STATUS CODE " + status);
                return null;
            }
        } catch (Exception e) {
            Log.e("Drawable Manager ", e.toString());
            return null;
        }

    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

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

    public static void getBitmap(final Handler handler, final String urlString) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = excutePostForBitMap(urlString);
                if (bitmap != null) {
                    Message message = handler.obtainMessage(1, bitmap);
                    handler.sendMessage(message);
                }
            }
        };
        thread.start();
    }

    public static Bitmap excutePostForBitMap(String url) {
        try {
            Bitmap bitMap = null;
            URL url_con = new URL(url);
            HttpURLConnection conn = null;
            if (url.contains("https")) {
                conn = (HttpsURLConnection) url_con.openConnection();
                try {
                    ((HttpsURLConnection) conn)
                            .setSSLSocketFactory(new TLSSocketFactory());
                } catch (KeyManagementException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                conn = (HttpURLConnection) url_con.openConnection();
            }

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Token", Config.getInstance()
                    .getSecretKey());
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
                bitMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // conn.disconnect();
                return bitMap;
            } else {
                Log.e("Drawable Manager ", "STATUS CODE " + status);
                return null;
            }
        } catch (Exception e) {
            Log.e("Drawable Manager ", e.toString());
            return null;
        }

    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {

        String key_md5 = Utils.md5(key);

        if (null != mMemoryCache) {
            if (getBitmapFromMemCache(key_md5) == null) {
                mMemoryCache.put(key_md5, bitmap);
            }
        }
    }


    public static Bitmap getBitmapFromMemCache(String key) {

        String key_md5 = Utils.md5(key);

        return mMemoryCache.get(key_md5);
    }
}

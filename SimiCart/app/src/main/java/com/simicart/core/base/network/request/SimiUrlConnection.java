package com.simicart.core.base.network.request;

import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.network.request.SimiRequest.Method;
import com.simicart.core.config.Config;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SimiUrlConnection {

    // private static SimiUrlConnection instance;
    static final String COOKIES_HEADER = "Set-Cookie";
    static CookieManager cookieManager = new CookieManager();
    static boolean isSet = false;
    protected ByteArrayPool mPool = null;

    public void setPool(ByteArrayPool pool) {
        mPool = pool;
    }

    public SimiUrlConnection() {
        if (!isSet) {
            isSet = true;
            CookieHandler.setDefault(cookieManager);
        }
    }

    public SimiNetworkResponse makeUrlConnection(SimiRequest request) {

        String url = request.getUrl();
        int type = request.getTypeMethod();
        Log.e("SimiUrlConnection ", "URL : " + url);
        if (request.isRedirect()) {
            url = request.getUrlRedirect();
        }

        HttpURLConnection urlConnection = null;
        try {
            URL url_connection = new URL(url);
            if (url.contains("https")) {
                urlConnection = (HttpsURLConnection) url_connection
                        .openConnection();
                try {
                    ((HttpsURLConnection) urlConnection)
                            .setSSLSocketFactory(new TLSSocketFactory());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            } else {
                urlConnection = (HttpURLConnection) url_connection
                        .openConnection();
            }

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            // add Authorization, Content-Type Header
            String token = "";
            if (request.isCloud) {
                token = "Bearer " + Config.getInstance().getSecretCloudKey();
                urlConnection.setRequestProperty("Authorization", token);
            } else {
                urlConnection.setRequestProperty("Token", Config.getInstance().getSecretKey());
            }

            urlConnection.setRequestProperty("Content-Type",
                    "application/json");


            // add header
            HashMap<String, String> headerAddtional = request
                    .getHeader();
            if (null != headerAddtional) {
                for (String key : headerAddtional.keySet()) {
                    urlConnection.setRequestProperty(key,
                            headerAddtional.get(key));
                }
            }


            if (type == Method.GET) {
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(false);
            } else if (type == Method.POST) {
                urlConnection.setRequestMethod("POST");
                JSONObject postBody = request.getBody();
                if (null != postBody) {
                    Log.e("SimiUrlConnection ", "PARAM " + postBody.toString());
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postBody.toString());
                    writer.flush();
                    writer.close();
                    os.close();
                }
            }


            Map<String, List<String>> headerFields = urlConnection
                    .getHeaderFields();

            List<String> locationHeader = headerFields.get("Location");
            if (null != locationHeader) {
                for (String location : locationHeader) {
                    Log.e("SimiUrlConnection ", "LOCATION " + location);
                    SimiManager.getIntance().getRequestQueue()
                            .getNetworkQueue().remove(request);
                    request.setRedirect(true);
                    request.setUrlRedirect(location);
                    SimiManager.getIntance().getRequestQueue().add(request);

                }
            }

            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    HttpCookie httpCookie = HttpCookie.parse(cookie).get(0);
                    if (null != httpCookie) {
                        cookieManager.getCookieStore().add(null, httpCookie);
                    }
                }
            }


            // process response
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != -1) {
                byte[] bytes = dataToBytes(urlConnection);
                if (null == bytes) {
                    bytes = new byte[0];
                }
                return new SimiNetworkResponse(responseCode, bytes);
            }
        } catch (IOException e) {
            Log.e("SimiUrlConnection ", "IOException " + e.getMessage());
        }
        request.cancel(true);
        return null;
    }


    protected byte[] dataToBytes(HttpURLConnection urlConnection) throws IOException {
        int length = urlConnection.getContentLength();
        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(
                mPool, length);
        byte[] buffer = null;
        try {
            InputStream in = urlConnection.getInputStream();
            if (null != in) {
                buffer = mPool.getBuf(1024);
                int count;
                while ((count = in.read(buffer)) != -1) {
                    bytes.write(buffer, 0, count);
                }
                return bytes.toByteArray();
            }
        } finally {
            try {
                urlConnection.getInputStream().close();
            } catch (IOException e) {
            }
            mPool.returnBuf(buffer);
            bytes.close();
        }
        return null;
    }

}

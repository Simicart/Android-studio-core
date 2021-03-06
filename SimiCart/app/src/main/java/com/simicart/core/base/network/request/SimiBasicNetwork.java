package com.simicart.core.base.network.request;

import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

public class SimiBasicNetwork {
    private static int DEFAULT_POOL_SIZE = 4096;

    protected final SimiUrlStack mUrlStack;

    protected final ByteArrayPool mPool;

    public SimiBasicNetwork(SimiUrlStack httpStack) {
        // If a pool isn't passed in, then build a small default pool that will
        // give us a lot of
        // benefit and not use too much memory.
        this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
    }

    /**
     * @param httpStack HTTP stack to be used
     * @param pool      a buffer pool that improves GC performance in copy operations
     */
    public SimiBasicNetwork(SimiUrlStack httpStack, ByteArrayPool pool) {
        mUrlStack = httpStack;
        mPool = pool;
    }

    public SimiNetworkResponse performRequest(SimiRequest request) {
        while (true) {
            SimiNetworkResponse simiResponse = null;
            byte[] responseContents = null;
            try {
                // Gather headers.
                simiResponse = mUrlStack.performRequest(request);
                if (null == simiResponse) {
                    throw new IOException();
                }
                int statusCode = simiResponse.getStatusCode();
                Log.e("SimiBasicNetwork " + request.getUrl(), "status code  "
                        + statusCode);
                // Some responses such as 204s do not have content. We must
                // check.
                if (simiResponse.getData() != null) {
                    responseContents = simiResponse.getData();
                } else {
                    // Add 0 byte response as a way of honestly representing a
                    // no-content request.
                    responseContents = new byte[0];
                }

                if (statusCode == 302 || statusCode == 301) {
                    return new SimiNetworkResponse(statusCode, responseContents);
                }

                if (statusCode < 200 || statusCode > 299) {
                    throw new IOException();
                }
                return new SimiNetworkResponse(statusCode, responseContents);
            } catch (SocketTimeoutException e) {
                Log.e("SimiBasicNetwork " + request.getUrl(),
                        "SocketTimeoutException " + e.getMessage());
                break;
            } catch (MalformedURLException e) {
                Log.e("SimiBasicNetwork " + request.getUrl(),
                        "MalformedURLException " + e.getMessage());
                break;
            } catch (IOException e) {
                Log.e("SimiBasicNetwork " + request.getUrl(), "IOException "
                        + e.getMessage());
                break;
            }
        }
        return null;
    }


//    private byte[] entityToBytes(HttpEntity entity) throws IOException {
//        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(
//                mPool, (int) entity.getContentLength());
//        byte[] buffer = null;
//        try {
//            InputStream in = entity.getContent();
//            if (null != in) {
//                buffer = mPool.getBuf(1024);
//                int count;
//                while ((count = in.read(buffer)) != -1) {
//                    bytes.write(buffer, 0, count);
//                }
//                return bytes.toByteArray();
//            }
//        } finally {
//            try {
//                // Close the InputStream and release the resources by
//                // "consuming the content".
//                entity.consumeContent();
//            } catch (IOException e) {
//                // This can happen if there was an exception above that left the
//                // entity in
//                // an invalid state.
//
//            }
//            mPool.returnBuf(buffer);
//            bytes.close();
//        }
//        return null;
//    }

}

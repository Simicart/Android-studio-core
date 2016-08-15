package com.simicart.core.base.network.request;

import java.util.concurrent.BlockingQueue;

import android.os.Process;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.network.response.SimiResponse;

public class SimiNetworkDispatcher extends Thread {
    protected volatile boolean mQuit = false;
    /**
     * The queue of requests to service.
     */
    protected BlockingQueue<SimiRequest> mQueue;
    /**
     * The network interface for processing requests.
     */
    protected SimiBasicNetwork mNetwork;

    protected SimiExecutorDelivery mDelivery;
    protected SimiNetworkCacheL1 mCache;

    public SimiNetworkDispatcher(BlockingQueue<SimiRequest> queue,
                                 SimiBasicNetwork network, SimiExecutorDelivery delivery,
                                 SimiNetworkCacheL1 cache) {
        mQueue = queue;
        mNetwork = network;
        mDelivery = delivery;
        mCache = cache;
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (true) {
            SimiRequest request = null;
            try {
                request = mQueue.take();
            } catch (InterruptedException e) {
                if (mQuit) {
                    return;
                }
                continue;

            }

            if (request.isCancel()) {
                request.finish();
                continue;
            }
            SimiNetworkResponse netResponse = mNetwork.performRequest(request);
            if (null == netResponse) {
                SimiManager.getIntance().getRequestQueue().getNetworkQueue()
                        .remove(request);
                SimiResponse response = new SimiResponse();
                mDelivery.postResponse(request, response);
                return;
            }

            int statusCode = netResponse.getStatusCode();
            if (statusCode == 302 || statusCode == 301) {
                return;
            }

            SimiResponse response = request.parseNetworkResponse(netResponse);
            if (null == response) {
                SimiManager.getIntance().getRequestQueue().getNetworkQueue()
                        .remove(request);
                response = new SimiResponse();
                mDelivery.postResponse(request, response);
                return;
            }

            if (request.isShouldeCache()) {
                String url_cache = request.getCacheKey();
                response.parse();
                mCache.put(url_cache, response.getDataJSON());
            }

            SimiManager.getIntance().getRequestQueue().getNetworkQueue()
                    .remove(request);
            mDelivery.postResponse(request, response);
        }

    }

}

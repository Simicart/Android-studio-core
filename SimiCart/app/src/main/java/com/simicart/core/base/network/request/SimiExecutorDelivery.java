package com.simicart.core.base.network.request;

import android.os.Handler;

import com.simicart.core.base.network.response.SimiResponse;

import java.util.concurrent.Executor;

public class SimiExecutorDelivery {
    /**
     * Used for posting responses, typically to the main thread.
     */
    protected Executor mResponsePoster;

    /**
     * Creates a new response delivery interface.
     *
     * @param handler {@link Handler} to post responses on
     */
    public SimiExecutorDelivery(final Handler handler) {
        // Make an Executor that just wraps the handler.
        mResponsePoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    /**
     * Creates a new response delivery interface, mockable version for testing.
     *
     * @param executor For running delivery tasks
     */
    public SimiExecutorDelivery(Executor executor) {
        mResponsePoster = executor;
    }

    public void postResponse(SimiRequest request, SimiResponse response) {
        mResponsePoster
                .execute(new ResponseDeliveryRunnable(request, response));
    }

    private class ResponseDeliveryRunnable implements Runnable {
        private final SimiRequest mRequest;
        private final SimiResponse mResponse;

        public ResponseDeliveryRunnable(SimiRequest request,
                                        SimiResponse response) {
            mRequest = request;
            mResponse = response;
        }

        @Override
        public void run() {
            mRequest.deliveryCoreResponse(mResponse);
        }
    }

}

package com.simicart.network.request;

import android.util.Log;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

public class SimiNetwork {

	private static int DEFAULT_POOL_SIZE = 4096;

	protected final SimiUrlStack mUrlStack;

	protected final ByteArrayPool mPool;

	public SimiNetwork(SimiUrlStack httpStack) {
		// If a pool isn't passed in, then build a small default pool that will
		// give us a lot of
		// benefit and not use too much memory.
		this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
	}

	/**
	 * @param urlStack
	 *            HTTP stack to be used
	 * @param pool
	 *            a buffer pool that improves GC performance in copy operations
	 */
	public SimiNetwork(SimiUrlStack urlStack, ByteArrayPool pool) {
		mUrlStack = urlStack;
		mPool = pool;
	}

	public SimiNetworkResponse performRequest(SimiRequest request) {
		while (true) {
			SimiNetworkResponse networkResponse = null;
			byte[] responseContents = null;
			try {
				// Gather headers.
				networkResponse = mUrlStack.performRequest(request);
				if (null == networkResponse) {
					throw new IOException();
				}
				int statusCode = networkResponse.getStatusCode();
				responseContents = networkResponse.getData();

				if (statusCode < 200 || statusCode > 299) {
					throw new IOException();
				}
				return new SimiNetworkResponse(statusCode, responseContents);
			} catch (SocketTimeoutException e) {
				Log.e("SimiNetworkCommon " + request.getUrl(),
						"SocketTimeoutException " + e.getMessage());
				break;
			}  catch (MalformedURLException e) {
				Log.e("SimiNetworkCommon " + request.getUrl(),
						"MalformedURLException " + e.getMessage());
				break;
			} catch (IOException e) {
				Log.e("SimiNetworkCommon " + request.getUrl(), "IOException "
						+ e.getMessage());
				break;
			}
		}
		return null;
	}
}

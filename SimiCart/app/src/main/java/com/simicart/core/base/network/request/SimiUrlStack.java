package com.simicart.core.base.network.request;


public class SimiUrlStack {

	private static int DEFAULT_POOL_SIZE = 4096;

	protected ByteArrayPool mPool = null;

	public SimiUrlStack() {
		this(new ByteArrayPool(DEFAULT_POOL_SIZE));
	}

	public SimiUrlStack(ByteArrayPool pool) {
		mPool = pool;
	}

	public SimiNetworkResponse performRequest(SimiRequest request) {
		SimiUrlConnection urlConnection = new SimiUrlConnection();
		urlConnection.setPool(mPool);
		return urlConnection.makeUrlConnection(request);
	}
}

package com.appserver.cache;

import io.netty.util.internal.ConcurrentSet;

/**
 * ip连接或访问超出规定的
 */
public class IpOutOfFlowCache {

	private final ConcurrentSet<String> OUT_FLOW_IPS = new ConcurrentSet<>();

	private final static IpOutOfFlowCache INSTANCE = new IpOutOfFlowCache();

	private IpOutOfFlowCache() {
	}

	public static IpOutOfFlowCache getInstance() {
		return INSTANCE;
	}

	public boolean contains(String ip) {
		return OUT_FLOW_IPS.contains(ip);
	}

	public boolean add(String ip) {
		return OUT_FLOW_IPS.add(ip);
	}

	public boolean remove(String ip) {
		return OUT_FLOW_IPS.remove(ip);
	}

	public void clear() {
		OUT_FLOW_IPS.clear();
	}

}

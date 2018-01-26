package com.commons.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemProperties {
	
	public static InetAddress getInetAddress() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("unknown host!");
		}
		return null;

	}

	/**
	 * get the ip address
	 * 
	 * @return
	 */
	public static String getHostIp() {
		return getInetAddress().getHostAddress();
	}

	/**
	 * get the host address
	 * 
	 * @return
	 */
	public static String getHostName() {
		return getInetAddress().getHostName();
	}
}

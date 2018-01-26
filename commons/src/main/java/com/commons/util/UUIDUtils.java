package com.commons.util;

import java.util.UUID;

public class UUIDUtils {
	/**
	 * 中间有-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 中间无-分割.
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}

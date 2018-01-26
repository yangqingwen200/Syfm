package com.commons.util;

import java.util.Random;

public class RandomNumber {

	/**
	 * 生成随机数字和字母
	 * 
	 * @param length
	 *            位数长度
	 * @return 随机数 2015年6月25日 下午3:54:36
	 */
	public static String getStringRandom(int length) {

		String val = "";
		Random random = new Random();

		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * 比较三个数字的大小
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 * @author Yang
	 * @version v1.0
	 * @date 2016年11月10日
	 */
	public static int getMax(int a, int b, int c) {
		return Math.max(Math.max(a, b), c);
	}

}

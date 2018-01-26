package com.commons.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 缩略图类（通用） 本java类能将jpg、bmp、png、gif图片文件，进行等比或非等比的大小转换。 具体使用方法 compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
 * 
 * @author Administrator
 *
 *         2015年10月31日 下午12:50:38
 */
public class CompressPic {

	public static long getPicSize(String path) {
		File file = new File(path);
		return file.length();
	}

	/**
	 * 输出和输入目录相同,输入文件名和输出文件名相同
	 * 
	 * @param inputDir
	 *            输入目录
	 * @param inputFileName
	 *            输入文件名
	 * @param width
	 *            输出的图片宽度
	 * @param height
	 *            输出的图片高度
	 * @param gp
	 *            是否等比缩放标记(默认为等比缩放)
	 * @return
	 *
	 * 		2015年10月31日 下午12:45:13
	 */
	public static boolean compressPic(String inputDir, String inputFileName, int width, int height, boolean gp) {
		try {
			// 获得源文件
			File file = new File(inputDir + inputFileName);
			if (!file.exists()) {
				return false;
			}
			Image img = ImageIO.read(file);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				System.out.println(" can't read,retry!" + "<BR>");
				return false;
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (gp) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) img.getWidth(null)) / (double) width + 0.1;
					double rate2 = ((double) img.getHeight(null)) / (double) height + 0.1;
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = width; // 输出的图片宽度
					newHeight = height; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
				ImageIO.write(tag, "jpeg", new File(inputDir + inputFileName));
				tag.flush();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}

	/**
	 * 输出和输入目录不同,输入文件名和输出文件名相同
	 * 
	 * @param inputDir
	 *            输入目录
	 * @param outputDir
	 *            输出目录
	 * @param inputFileName
	 *            输入文件名
	 * @param width
	 *            输出的图片宽度
	 * @param height
	 *            输出的图片高度
	 * @param gp
	 *            是否等比缩放标记(默认为等比缩放)
	 * @return
	 *
	 * 		2015年10月31日 下午12:45:13
	 */
	public static boolean compressPic(String inputDir, String outputDir, String inputFileName, int width, int height, boolean gp) {
		try {
			// 获得源文件
			File file = new File(inputDir + inputFileName);
			if (!file.exists()) {
				return false;
			}
			Image img = ImageIO.read(file);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				System.out.println(" can't read,retry!" + "<BR>");
				return false;
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (gp) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) img.getWidth(null)) / (double) width + 0.1;
					double rate2 = ((double) img.getHeight(null)) / (double) height + 0.1;
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = width; // 输出的图片宽度
					newHeight = height; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
				// JPEGImageEncoder可适用于其他图片类型的转换
				ImageIO.write(tag, "jpeg", new File(outputDir + inputFileName));
				tag.flush();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}

	/**
	 * 输出和输入目录不同,输入文件名和输出文件名也不同
	 * 
	 * @param inputDir
	 *            输入目录
	 * @param outputDir
	 *            输出目录
	 * @param inputFileName
	 *            输入文件名
	 * @param outputFileName
	 *            输出文件名
	 * @param width
	 *            输出的图片宽度
	 * @param height
	 *            输出的图片高度
	 * @param gp
	 *            是否等比缩放标记(默认为等比缩放)
	 * @return
	 *
	 * 		2015年10月31日 下午12:45:13
	 */
	public static boolean compressPic(String inputDir, String outputDir, String inputFileName, String outputFileName, int width, int height, boolean gp) {
		try {
			// 获得源文件
			File file = new File(inputDir + inputFileName);
			if (!file.exists()) {
				return false;
			}
			Image img = ImageIO.read(file);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				System.out.println(" can't read,retry!" + "<BR>");
				return false;
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (gp) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) img.getWidth(null)) / (double) width + 0.1;
					double rate2 = ((double) img.getHeight(null)) / (double) height + 0.1;
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = width; // 输出的图片宽度
					newHeight = height; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
				// JPEGImageEncoder可适用于其他图片类型的转换
				ImageIO.write(tag, "jpeg", new File(outputDir + inputFileName));
				tag.flush();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}

	// main测试
	// compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
	public static void main(String[] arg) {
		// System.out.println("输入的图片大小：" + getPicSize("d:\\test\\a.jpg") / 1024 + "KB");

		// int start = (int) System.currentTimeMillis(); // 开始时间

		// compressPic("d:\\test\\", "d:\\test\\", "a.jpg", "a.jpg", 1000, 1000, true);
		compressPic("d:\\test\\", "a.jpg", 1920, 600, true);

		// int end = (int) System.currentTimeMillis(); // 结束时间
		// int re = end - start; // 但图片生成处理时间

		// System.out.println("输出的图片大小：" + getPicSize("d:\\test\\a.jpg") / 1024 + "KB");
		// System.out.println("总共用了：" + re + "毫秒");
		// comPic("E:\\mydata\\upload");
	}
/*
	public static void comPic(String path) {
		File f = new File(path);
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File file : files) {
				// System.out.println(path + "\\" + file.getName());
				long length = file.length();
				long size = (length / 1024);
				if (size > 100) {
					String filename = file.getName();
					if (filename.endsWith("jpg") || filename.endsWith("jpeg") || filename.endsWith("bmp") || filename.endsWith("png") || filename.endsWith("JPG") || filename.endsWith("JPEG")) {
						System.out.println(size);
						System.out.println(path);
						System.out.println(file.getName());
						compressPic(path + "\\", file.getName(), 480, 800, true);
					}
				}

				comPic(path + "\\" + file.getName());
			}
		} else {

			String name = path.toLowerCase();
			if (name.endsWith("jpg") || name.endsWith("jpeg") || name.endsWith("bmp") || name.endsWith("png")) { // compressPic(); //System.out.println(path); int
				last = path.lastIndexOf("\\");
				String name1 = path.substring(last + 1);
				String inputdisk = path.substring(0, last);
				String outputdisk = "F" + inputdisk.substring(1, last);
				long length = path.length();
				long size = (length);
				if (size > 10) {
					System.out.println(name1);
					System.out.println(inputdisk);
					System.out.println(outputdisk);
					System.out.println(size);
				}
			}

		}
	}*/
}

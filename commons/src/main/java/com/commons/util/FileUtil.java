package com.commons.util;

import com.commons.constant.InitPpConstant;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * 文件处理类
 *
 * auth：Yang
 * 2016年4月5日 下午2:28:26
 */
public class FileUtil {
	
	private final static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	/**
	 * 上传文件
	 * 
	 * @param upFile 文件
	 * @param fileName 文件名
	 * @param folder 文件夹
	 * @param flag 是否需要压缩,true 压缩. false 不压缩
	 * @return
	 * @throws Exception
	 */
	public static String uploadFile(final File upFile, final String fileName, final String folder, final boolean flag) throws Exception {
		InputStream is = null;
		OutputStream os = null;
		StringBuffer fullPath = new StringBuffer("");
		try {
			
			String osName = System.getProperty("os.name"); //得到当前操作系统的名称
			if(osName.contains("Windows")) {
				
				if (upFile != null && upFile.exists()) {
					String root = ServletActionContext.getServletContext().getRealPath("");
					File file = new File(root);
					if (!file.exists() && !file.isDirectory()) {
						file.mkdir();
					}
					is = new FileInputStream(upFile);
					String newFileName = changePicName(fileName);
					
					//判断上传文件的目录是否存在
					File destFileFolder = new File(root + folder);
					if(!destFileFolder.exists() && !destFileFolder.isDirectory()) {
						destFileFolder.mkdirs(); //mkdirs创建多级目录
					}
					
					File destFile = new File(root + folder, newFileName);
					os = new FileOutputStream(destFile);
					byte[] buffer = new byte[1024];
					int length = 0;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
						os.flush();
					}
					
					//压缩图片
					if(flag) {
						CompressPic.compressPic(root + folder + "/", newFileName, InitPpConstant.WIDTH, InitPpConstant.HEIGHT, InitPpConstant.ISEPSCALING);
					}
					fullPath.append(InitPpConstant.LOCAL).append(folder).append("/" + newFileName);
				}
			} else if(osName.contains("Linux")) {
				
				//linux图片上传(放在根路径下的carlifeupload文件夹中carlifeimage.war目录中)
				if (upFile != null && upFile.exists()) {
					
					File file = new File(InitPpConstant.LINUX_FOLDER + folder); //上传路径
					if (!file.exists() && !file.isDirectory()) {
						file.mkdirs();
					}
					is = new FileInputStream(upFile); //得到上传的文件流
					
					String newFileName = changePicName(fileName); //处理文件名
					
					File destFile = new File(InitPpConstant.LINUX_FOLDER + folder, newFileName); //写入文件
					os = new FileOutputStream(destFile);
					byte[] buffer = new byte[1024];
					int length = 0;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
						os.flush();
					}
					
					//压缩图片
					if(flag) {
						CompressPic.compressPic(InitPpConstant.LINUX_FOLDER + folder + "/", newFileName, InitPpConstant.WIDTH, InitPpConstant.HEIGHT, InitPpConstant.ISEPSCALING);
					}
					fullPath.append(InitPpConstant.LINUX_URL).append(folder).append("/" + newFileName);
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// 关闭IO流
			if (os != null) {
				os.close();
				os = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
		}
		return fullPath.toString();
	}


	/**
	 * 删除指定文件夹下面的图片
	 * @param url
	 *
	 * 2015年10月20日 下午3:03:40
	 */
	public static void deletePic(String url) {
		if(url != null && !"".equals(url)) {
			String[] urls = url.split(";");
			for (String string : urls) {
				if(string != null && !"".equals(string)) {
					if(string.indexOf("upload") != -1) {
						String fileUrl = string.substring(string.indexOf("upload"));
						File file = new File(InitPpConstant.LINUX_FOLDER + fileUrl);
						if (file.exists()) {
							file.delete();
						}
					}
				}
			}
		}
	}

	/**
	 * 用时间戳 + 三位随机数 替换文件名字
	 * @param fileName
	 * @return
	 * 
	 *  2015年8月12日 下午11:32:27
	 */
	public static String changePicName(String fileName) {
		String str = RandomStringUtils.randomNumeric(3); //产生三位随机数
		String endFileName = StringUtils.substringAfter(fileName, ".");
		return df.format(new Date()) + str + "." + endFileName;
	}
	
	/**
	  * 创建压缩文件
	 * @param file
	 * @param zipFile
	 * @return
	 */
	public static boolean zipSingleFile(String file, String zipFile) {
       boolean bf = true;
       File f = new File(file);
       FileInputStream in = null;
       FileOutputStream out = null;
       ZipOutputStream zipOut = null;
       
       try {
			if (!f.exists()) {
			     System.out.println("文件不存在");
			     bf = false;
			 } else {
			     File ff = new File(zipFile);
			     if (!f.exists()) {
			         ff.createNewFile();
			     }
			    in = new FileInputStream(file);  // 创建文件输入流对象
			    out = new FileOutputStream(zipFile); // 创建文件输出流对象
			    zipOut = new ZipOutputStream(out); // 创建ZIP数据输出流对象
			    String fileName = file.substring(file.lastIndexOf(File.separator) + 1, file.length());  // 得到文件名称
			    ZipEntry entry = new ZipEntry(fileName);// 创建指向压缩原始文件的入口
			    zipOut.putNextEntry(entry); 
			     // 向压缩文件中输出数据
			     int number = 0;
			     byte[] buffer = new byte[1024];
			     while ((number = in.read(buffer)) != -1) {
			         zipOut.write(buffer, 0, number);
			     }
			 }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			try {
				if(zipOut != null) {
					zipOut.close();
					zipOut = null;
				}
				if(out != null) {
					out.close();
					out = null;
				}
				if(in != null) {
					in.close();
					in = null;
				}
				f.delete(); //删除源文件
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
      return bf;
   }
	
	/**
	 * 检查文件类型允许上传的文件格式
	 * 
	 * @param fileType 文件类型
	 * @param fileName 文件名
	 * @return
	 * 
	 *  2016年8月7日 下午9:52:19
	 */
	public static boolean checkFileExt(String fileType, String fileName) {
		boolean result = true;
		
		//定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		//检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if(!Arrays.<String>asList(extMap.get(fileType).split(",")).contains(fileExt)){
			//out.println(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
			result = false;
		}
		
		return result;
	}
	
}

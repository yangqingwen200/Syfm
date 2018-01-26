package com.console.action.system;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

/**
 * 获取 验证码
 * 
 * @author
 * 
 */
@Controller("system.web.action.randomsAction")
@Scope("prototype")
public class Randoms extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private ByteArrayInputStream inputStream;

	private String str;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}

	/*
	 * 给定范围获得随机颜色
	 */
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}

		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 获取验证码
	 */
	public String execute() throws Exception {
		// 在内存中创建图象
		int width = 85, height = 22;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取图形上下
		Graphics g = image.getGraphics();
		// 生成随机
		Random random = new Random();
		// 设定背景
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		g.setColor(getRandColor(160, 200));
		
		// 随机产生80条干扰线，使图象中的认证码不易被其它程序探测
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.setColor(new Color(random.nextInt(256), random.nextInt(256),random.nextInt(256)));
			g.drawLine(x, y, x + xl, y + yl);
		}
		
		// 取随机产生的认证6位数(数字 + 字母)
		String sRand = "";
		for (int i = 0; i < 6; i++) {
			String tempString = "";
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			//输出字母还是数字
			if( "char".equalsIgnoreCase(charOrNum) ) {
				//输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				tempString = (char)(random.nextInt(26) + temp) + "";
			} else if( "num".equalsIgnoreCase(charOrNum) ) {
				tempString = String.valueOf(random.nextInt(10));
			}
			sRand += tempString;
			g.drawString(tempString.toLowerCase(), 13 * i + 4, 16);

		}
		// 将认证码存入SESSION
		ActionContext.getContext().getSession().put("rand", sRand.toLowerCase());
		// 图象生效
		g.dispose();

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
		ImageIO.write(image, "JPEG", imageOut);
		imageOut.close();
		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());

		this.setInputStream(input);
		return SUCCESS;
	}

}

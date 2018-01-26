package com.front.taglib;

import com.commons.taglib.CommonTag;

import javax.servlet.http.HttpSession;

/**
 * 更多信息的格式化转换器
 */
public class MoreFmtTag extends CommonTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String value; //需要格式化的值
	private String fmtType; //输出格式, 可以为Bootstrap,Easyui标签的css样式
	private Integer subLength; //截取字符串的长度(单元格显示value截取后的内容)

	@Override
	public String bodyMessage(HttpSession session) {
		StringBuffer sb = new StringBuffer();
		if(subLength >= value.length()) {
			sb.append(value);
			return sb.toString();
		}

		if("tooltip".equals(fmtType)) {
			//Bootstrap css标签
			sb.append("<a style=\"cursor: default; color: black\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"" + value + "\">");
			sb.append(value.substring(0, subLength) + "...");
			sb.append("</a>");

		} else if("abbr".equals(fmtType)) {
			//Bootstrap css标签
			sb.append("<abbr title=\"" + value + "\">");
			sb.append(value.substring(0, subLength));
			sb.append("</abbr>");

		} else {
			sb.append(value.substring(0, subLength));
		}

		return sb.toString();
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFmtType() {
		return fmtType;
	}

	public void setFmtType(String fmtType) {
		this.fmtType = fmtType;
	}

	public Integer getSubLength() {
		return subLength;
	}

	public void setSubLength(Integer subLength) {
		this.subLength = subLength;
	}
}

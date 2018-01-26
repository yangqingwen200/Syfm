package com.commons.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * POI导出数据到excel
 * Yang
 *
 * 2015年12月12日 下午9:50:17
 */
public class POIExcel {
	
	/**
	 * 导出数据到excel中,只有一个工作薄 
	 * 注意: excelHeader[]数组和Object[]长度要一致
	 * @param worktableTitle 工作薄的标题
	 * @param excelHeader 表头
	 * @param list 存在内容的Object数组
	 * @param cellTitle 第一行合并单元格内容
	 * @return
	 * Yang
	 * 2016年1月21日 下午3:29:03
	 */
	public static HSSFWorkbook exportExcel(String worktableTitle, String cellTitle, String[] excelHeader, List<Object[]> list) {  
	        HSSFWorkbook wb = new HSSFWorkbook(); 
	        //建立工作薄
	        HSSFSheet sheet = wb.createSheet(worktableTitle);
	        sheet.createFreezePane( 0, 2, 0, 2);//冻结第二行
	       
	        // 设置合并单元格字体   
	        HSSFFont font0 = wb.createFont();   
	        font0.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);   
	        font0.setFontHeightInPoints((short) 15); 
	        
	        //设置列标题的单元格字体
	        HSSFFont font = wb.createFont();
			font.setFontName("黑体");
			font.setFontHeightInPoints((short) 11);//设置字体大小
			
			//设置合并单元格的样式
			HSSFCellStyle style0 = wb.createCellStyle();  
			style0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style0.setFont(font0);
			//如果后续想用\r\n强制换行, 必须先设置为自动换行  
			style0.setWrapText(true); 
			
			//设置列标题样式
	        HSSFCellStyle style = wb.createCellStyle();  
//	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
	        style.setFont(font);

	        //创建第一行
	        HSSFRow row0 = sheet.createRow((int) 0);
	        //设定行高
	        row0.setHeight((short)450); 
	        //合并第一行单元格
	        //参数1：行号, 参数2：起始列号, 参数3：行号, 参数4：终止列号 
	        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, excelHeader.length - 1);  
	        sheet.addMergedRegion(cellRangeAddress);
	        //给第一行赋值
	        HSSFCell cell0 = row0.createCell(0);
	        cell0.setCellStyle(style0);
	        cell0.setCellValue(new HSSFRichTextString(cellTitle)); 
	        
	        //创建第二行
	        HSSFRow row = sheet.createRow((int) 1); 
	        //给第二行赋值(列标题)
	        for (int i = 0; i < excelHeader.length; i++) {  
	            HSSFCell cell = row.createCell(i);
	            String value = excelHeader[i];
	            cell.setCellValue(value);  
	            cell.setCellStyle(style);
	            sheet.autoSizeColumn((short)i);
	        }  
	  
	        //创建第三行--第四行, 并赋值
	        int length = 0;
	        for (int i = 0; i < list.size(); i++) {  
	            row = sheet.createRow(i + 2);  
	            Object[] obj = list.get(i);
	            for(int j=0; j<obj.length; j++) {
	            	if(obj[j] != null) {
	            		length = obj[j].toString().getBytes().length;
	            	}
	            	int colWidth = sheet.getColumnWidth(j);
	            	int col = (length > colWidth/256) ? length*280 : colWidth;
            		sheet.setColumnWidth(j, col);
            		HSSFCell cell = row.createCell(j);
            		if(j == 0) {
            			//第一列一般为序号
            			cell.setCellValue(i + 1); 
            		} else {
            			if(obj[j] instanceof BigDecimal || obj[j] instanceof BigInteger || obj[j] instanceof Integer || 
            					obj[j] instanceof Long || obj[j] instanceof Double || obj[j] instanceof Float) {
            				cell.setCellValue(Double.parseDouble(obj[j].toString())); 
            			} else {
            				cell.setCellValue(obj[j] != null ? obj[j].toString() : "无数据");
            			}
            		}
	            }
	        }  
	        return wb;  
	    }  
	
	/**
	 * 导出数据到excel中,有多个工作薄
	 * 
	 * @param worktableTile 工作表的标题list
	 * @param excelHeader 表头list
	 * @param listContent 存放数据的list
	 * @return
	 * Yang
	 * 2015年12月16日 下午5:56:05
	 */
	public static HSSFWorkbook exportExcelMore(List<String> worktableTile, List<String[]> excelHeader, List<List<Object[]>> listContent) {  
		if(worktableTile == null || excelHeader == null || listContent == null) {
			System.out.println("null");
			return null;
		}
		
		//判断list大小是否相等, 避免出现空的数据或者空工作薄标题或者空表头
		int worktableTileSize = worktableTile.size();
		int excelHeaderSize = excelHeader.size();
		int listContentSize = listContent.size();
		if(worktableTileSize != excelHeaderSize || worktableTileSize != listContentSize || excelHeaderSize != listContentSize) {
			System.out.println("size not equals");
			return null;
		}
		
		HSSFWorkbook wb = new HSSFWorkbook();  
		HSSFFont font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 11);//设置字体大小
		
		for (int k=0; k<worktableTileSize && k<excelHeaderSize && k<listContentSize; k++) {
			HSSFSheet sheet = wb.createSheet(worktableTile.get(k)); 
			HSSFRow row = sheet.createRow((int) 0);  
			HSSFCellStyle style = wb.createCellStyle();  
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT); 
			style.setFont(font);//选择需要用到的字体格式
			
			//设置列标题
			for (int i = 0; i < excelHeader.get(k).length; i++) {  
				HSSFCell cell = row.createCell(i);  
				cell.setCellValue(excelHeader.get(k)[i]);  
				cell.setCellStyle(style);  
			}  
			
			//设置每行的值
			for (int i = 0; i < listContent.get(k).size(); i++) {  
				row = sheet.createRow(i + 1);  
				Object[] obj = listContent.get(k).get(i);
				for(int j=0; j<obj.length; j++) {
					HSSFCell cell = row.createCell(j);
					if(obj[j] instanceof BigDecimal || obj[j] instanceof BigInteger || obj[j] instanceof Integer || obj[j] instanceof Long || obj[j] instanceof Double || obj[j] instanceof Float) {
						cell.setCellValue(Double.parseDouble(obj[j].toString())); 
					} else {
						cell.setCellValue(obj[j] != null ? obj[j].toString() : "无数据");
					}
				}
			}  
		}
		return wb;  
	}  
}

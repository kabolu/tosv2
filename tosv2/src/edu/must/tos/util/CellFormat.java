package edu.must.tos.util;

import jxl.write.WritableCellFormat;

public class CellFormat {

	public static final WritableCellFormat getCellFormat() throws Exception{
		//表格式樣
		WritableCellFormat cellFormat = new WritableCellFormat();
		cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
		cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		cellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		return cellFormat;
	}
}

/**
* Copyright © 1998-2018, Glodon Inc. All Rights Reserved.
*/
package jungle.wind.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import jungle.wind.enums.ExcelFileType;
import jungle.wind.excel.ExcelOption;
import jungle.wind.util.ExcelUtil;

/**
 * Excel工具测试类
 * @author JungleWind
 * @since jdk1.6
 * 2018年12月5日
 *  
 */

public class DemoTest {
	public static void main(String[] args) throws Exception {
		exportExcel();
		importExcel();
	}
	
	public static void exportExcel() throws Exception {
		System.out.println("start export ...");
		ExcelUtil<ExportModel> util = new ExcelUtil<ExportModel>(ExportModel.class);
		ExcelOption excelOption = new ExcelOption("人员销售额汇总", "销售明细", ExcelFileType.XLS);
		FileOutputStream output = new FileOutputStream("E:\\人员销售额汇总.xls");
		util.exportListToExcel(createExportModels(), excelOption, output);
		System.out.println("export success！");
	}
	
	public static void importExcel() throws Exception {
		System.out.println("start import ...");
		ExcelUtil<ImportModel> util = new ExcelUtil<ImportModel>(ImportModel.class);
		FileInputStream input = new FileInputStream("E:\\人员销售额汇总_normal.xls");
		List<ImportModel> result = util.importExcelToList("销售明细1", input);
		for (ImportModel importModel : result) {
			System.out.println(importModel);
		}
		System.out.println("import success！");
	}
	
	public static List<ExportModel> createExportModels() {
		List<ExportModel> models = new ArrayList<ExportModel>();
		try {
			Date joinDate1 = DateUtils.parseDate("2007-03-01", "yyyy-MM-dd");
			Date joinDate2 = DateUtils.parseDate("2007-10-01", "yyyy-MM-dd");
			Date joinDate3 = DateUtils.parseDate("2008-05-01", "yyyy-MM-dd");
			models.add(new ExportModel(1, "小明", "1", QuarterEnum.FIRST_QUARTER, new BigDecimal("100"), joinDate1));
			models.add(new ExportModel(1, "小明", "1", QuarterEnum.SECOND_QUARTER, new BigDecimal("200"), joinDate1));
			models.add(new ExportModel(1, "小明", "1", QuarterEnum.THIRD_QUARTER, new BigDecimal("200"), joinDate1));
			models.add(new ExportModel(1, "小明", "1", QuarterEnum.FOURTH_QUARTER, new BigDecimal("100"), joinDate1));

			models.add(new ExportModel(2, "小红", "0", QuarterEnum.FIRST_QUARTER, new BigDecimal("300"), joinDate2));
			models.add(new ExportModel(2, "小红", "0", QuarterEnum.SECOND_QUARTER, new BigDecimal("200"), joinDate2));
			models.add(new ExportModel(2, "小红", "0", QuarterEnum.THIRD_QUARTER, new BigDecimal("200"), joinDate2));
			models.add(new ExportModel(2, "小红", "0", QuarterEnum.FOURTH_QUARTER, new BigDecimal("500"), joinDate2));

			models.add(new ExportModel(3, "小华", "0", QuarterEnum.FIRST_QUARTER, new BigDecimal("200"), joinDate3));
			models.add(new ExportModel(3, "小华", "0", QuarterEnum.SECOND_QUARTER, new BigDecimal("200"), joinDate3));
			models.add(new ExportModel(3, "小华", "0", QuarterEnum.THIRD_QUARTER, new BigDecimal("200"), joinDate3));
			models.add(new ExportModel(3, "小华", "0", QuarterEnum.FOURTH_QUARTER, new BigDecimal("300"), joinDate3));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return models;
	}
	
	public static List<ImportModel> createImportModels() {
		List<ImportModel> models = new ArrayList<ImportModel>();
		try {
			Date joinDate1 = DateUtils.parseDate("2007-03-01", "yyyy-MM-dd");
			Date joinDate2 = DateUtils.parseDate("2007-10-01", "yyyy-MM-dd");
			Date joinDate3 = DateUtils.parseDate("2008-05-01", "yyyy-MM-dd");
			models.add(new ImportModel(1, "小明", "1", QuarterEnum.FIRST_QUARTER, new BigDecimal("100"), joinDate1));
			models.add(new ImportModel(1, "小明", "1", QuarterEnum.SECOND_QUARTER, new BigDecimal("200"), joinDate1));
			models.add(new ImportModel(1, "小明", "1", QuarterEnum.THIRD_QUARTER, new BigDecimal("200"), joinDate1));
			models.add(new ImportModel(1, "小明", "1", QuarterEnum.FOURTH_QUARTER, new BigDecimal("100"), joinDate1));

			models.add(new ImportModel(2, "小红", "0", QuarterEnum.FIRST_QUARTER, new BigDecimal("300"), joinDate2));
			models.add(new ImportModel(2, "小红", "0", QuarterEnum.SECOND_QUARTER, new BigDecimal("200"), joinDate2));
			models.add(new ImportModel(2, "小红", "0", QuarterEnum.THIRD_QUARTER, new BigDecimal("200"), joinDate2));
			models.add(new ImportModel(2, "小红", "0", QuarterEnum.FOURTH_QUARTER, new BigDecimal("500"), joinDate2));

			models.add(new ImportModel(3, "小华", "0", QuarterEnum.FIRST_QUARTER, new BigDecimal("200"), joinDate3));
			models.add(new ImportModel(3, "小华", "0", QuarterEnum.SECOND_QUARTER, new BigDecimal("200"), joinDate3));
			models.add(new ImportModel(3, "小华", "0", QuarterEnum.THIRD_QUARTER, new BigDecimal("200"), joinDate3));
			models.add(new ImportModel(3, "小华", "0", QuarterEnum.FOURTH_QUARTER, new BigDecimal("300"), joinDate3));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return models;
	}
}

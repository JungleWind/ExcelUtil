package jungle.wind.enums;

/**
 * Excel文件类型
 * </br>后缀.xls是Excel 97-2007的文件
 * </br>后缀.xlsx是Excel 2007以后的文件
 * @author JungleWind
 * @since jdk1.6
 * 2018年11月2日
 *  
 */
public enum ExcelFileType {
	
	XLS("xls"), XLSX("xlsx");
	
	private String desc;

	private ExcelFileType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}

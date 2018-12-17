package jungle.wind.excel;

import jungle.wind.enums.ExcelFileType;

/**
 * Excel选项
 * <p>1、设置导出的标题名称</p>
 * <p>2、设置sheet名称</p>
 * <p>3、设置Excel文件类型：.XLS或.XLSX</p>
 * @author JungleWind
 * @since jdk1.6
 * 2018年11月2日
 *  
 */

public class ExcelOption {
	/** 标题 */
	private String title;
	/** sheet名称 */
	private String sheetName;
	/** Excel文件类型  */
	private ExcelFileType excelFileType = ExcelFileType.XLS;
	
	/**  
	 * ExcelOption    
	 */
	public ExcelOption() {
		super();
	}
	
	/**  
	 * ExcelOption
	 * @param title
	 * @param sheetName
	 * @param excelFileType    
	 */
	public ExcelOption(String title, String sheetName, ExcelFileType excelFileType) {
		super();
		this.title = title;
		this.sheetName = sheetName;
		this.excelFileType = excelFileType;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public ExcelFileType getExcelFileType() {
		return excelFileType;
	}
	public void setExcelFileType(ExcelFileType excelFileType) {
		this.excelFileType = excelFileType;
	} 
	
}

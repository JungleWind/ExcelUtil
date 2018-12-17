package jungle.wind.excel;

/**
 * 记录单元格合并信息
 * @author JungleWind
 * @since jdk1.6
 * 2018年11月21日
 *  
 */

public class PoiModel {
	private int rowNo;
	private String content;
	private String mergeFlagContent;
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMergeFlagContent() {
		return mergeFlagContent;
	}
	public void setMergeFlagContent(String mergeFlagContent) {
		this.mergeFlagContent = mergeFlagContent;
	}
	
}

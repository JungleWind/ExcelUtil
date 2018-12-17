/**
* Copyright © 1998-2018, Glodon Inc. All Rights Reserved.
*/
package jungle.wind.demo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import jungle.wind.annotation.ExcelFiled;

/**
 * 导出Model
 * @author JungleWind
 * @since jdk1.6
 * 2018年12月5日
 *  
 */

public class ExportModel {
	@ExcelFiled(name = "人员编号", merge = true, mergeFlag = "id")
	private Integer id;
	@ExcelFiled(name = "姓名", merge = true, mergeFlag = "id")
	private String name;
	/** 1:男，0：女 */
	@ExcelFiled(name = "性别", convertMethod = "sexConvert", comboMethod = "sexCombo", merge = true, mergeFlag = "id")
	private String sex;
	@ExcelFiled(name = "时间", convertMethod = "quarterConvert", comboMethod = "quarterCombo")
	private QuarterEnum quarter;
	@ExcelFiled(name = "销售额（元）", isSum = true)
	private BigDecimal sales;
	@ExcelFiled(name = "入职时间", convertMethod = "joinDateConvert", merge = true, mergeFlag = "id")
	private Date joinDate;
	
	/**
	 * 季度选择框
	 * @return
	 */
	public String[] quarterCombo() {
		String[] result = new String[QuarterEnum.values().length];
		QuarterEnum[] quarterEnums = QuarterEnum.values();
		for (int i = 0; i < quarterEnums.length; i++) {
			result[i] = quarterEnums[i].getDesc();
		}
		return result;
	}
	
	/**
	 * 导出性别格式化
	 * @return
	 */
	public String sexConvert() {
		if ("1".equals(sex)) {
			return "男";
		} else if ("0".equals(sex)) {
			return "女";
		}
		return null;
	}
	
	/**
	 * 性别选择框
	 * @return
	 */
	public String[] sexCombo() {
		return new String[] {"男", "女"};
	}
	
	/**
	 * 导出部门格式化
	 * @return
	 */
	public String quarterConvert() {
		return quarter.getDesc();
	}
	
	/**
	 * 导出入职时间格式化
	 * @return
	 */
	public String joinDateConvert() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(joinDate);
	}
	
	/**  
	 * ExcelModel    
	 */
	public ExportModel() {
		super();
	}

	/**  
	 * ExcelModel
	 * @param id
	 * @param name
	 * @param sex
	 * @param quarter
	 * @param sales
	 * @param joinDate    
	 */
	public ExportModel(Integer id, String name, String sex, QuarterEnum quarter, BigDecimal sales, Date joinDate) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.quarter = quarter;
		this.sales = sales;
		this.joinDate = joinDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public QuarterEnum getQuarter() {
		return quarter;
	}

	public void setQuarter(QuarterEnum quarter) {
		this.quarter = quarter;
	}

	public BigDecimal getSales() {
		return sales;
	}

	public void setSales(BigDecimal sales) {
		this.sales = sales;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	
}

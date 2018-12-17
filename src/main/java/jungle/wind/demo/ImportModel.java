/**
* Copyright © 1998-2018, Glodon Inc. All Rights Reserved.
*/
package jungle.wind.demo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

import jungle.wind.annotation.ExcelFiled;

/**
 * 导入Model
 * @author JungleWind
 * @since jdk1.6
 * 2018年12月5日
 *  
 */

public class ImportModel {
	@ExcelFiled(name = "人员编号")
	private Integer id;
	@ExcelFiled(name = "姓名")
	private String name;
	/** 1:男，0：女 */
	@ExcelFiled(name = "性别", convertMethod = "sexConvert", convertParameterTypes = {String.class})
	private String sex;
	@ExcelFiled(name = "时间", convertMethod = "quarterConvert", convertParameterTypes = {String.class})
	private QuarterEnum quarter;
	@ExcelFiled(name = "销售额（元）")
	private BigDecimal sales;
	@ExcelFiled(name = "入职时间", convertMethod = "joinDateConvert", convertParameterTypes = {String.class})
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
	public String sexConvert(String sex) {
		if ("男".equals(sex)) {
			return "1";
		} else if ("女".equals(sex)) {
			return "0";
		}
		return null;
	}
	
	/**
	 * 导出部门格式化
	 * @return
	 */
	public QuarterEnum quarterConvert(String quarter) {
		if (quarter.equals(QuarterEnum.FIRST_QUARTER.getDesc())) {
			return QuarterEnum.FIRST_QUARTER;
		} else if (quarter.equals(QuarterEnum.SECOND_QUARTER.getDesc())) {
			return QuarterEnum.SECOND_QUARTER;
		} else if (quarter.equals(QuarterEnum.THIRD_QUARTER.getDesc())) {
			return QuarterEnum.THIRD_QUARTER;
		} else if (quarter.equals(QuarterEnum.FOURTH_QUARTER.getDesc())) {
			return QuarterEnum.FOURTH_QUARTER;
		}
		return null;
	}
	
	/**
	 * 导出入职时间格式化
	 * @return
	 */
	public Date joinDateConvert(String date) {
		try {
			return DateUtils.parseDate(date, "yyyy-MM-dd");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**  
	 * ExcelModel    
	 */
	public ImportModel() {
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
	public ImportModel(Integer id, String name, String sex, QuarterEnum quarter, BigDecimal sales, Date joinDate) {
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

	/** 
	  * {@inheritDoc}   
	  * @see java.lang.Object#toString() 
	  */
	@Override
	public String toString() {
		return "ImportModel [id=" + id + ", name=" + name + ", sex=" + sex + ", quarter=" + quarter + ", sales=" + sales + ", joinDate=" + joinDate + "]";
	}
	
}

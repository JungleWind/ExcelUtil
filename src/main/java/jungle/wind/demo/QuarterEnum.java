/**
* Copyright © 1998-2018, Glodon Inc. All Rights Reserved.
*/
package jungle.wind.demo;

/**
 * 部门枚举
 * @author JungleWind
 * @since jdk1.6
 * 2018年12月5日
 *  
 */

public enum QuarterEnum {
	FIRST_QUARTER("一季度"), SECOND_QUARTER("二季度"), THIRD_QUARTER("三季度"), FOURTH_QUARTER("四季度");
	private String desc;
	
	private QuarterEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}

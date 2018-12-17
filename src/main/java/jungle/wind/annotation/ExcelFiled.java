package jungle.wind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel配置注解
 * 
 * @author JungleWind
 * @since jdk1.6 2018年10月28日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelFiled {
	/**
	 * Excel中的列名
	 * 
	 * @return
	 */
	public abstract String name();

	/**
	 * 列名对应的A,B,C,D...,不指定按照默认顺序排序
	 * 
	 * @return
	 */
	public abstract String column() default "";

	/**
	 * 提示信息
	 * 
	 * @return
	 */
	public abstract String prompt() default "";
	
	/**
	 * 设置只能选择不能输入的方法，该方法必须返回一个String类型数组
	 * 
	 * @return
	 */
	public abstract String comboMethod() default "";
	
	/**
	 * 只能选择不能输入的方法参数类型数组
	 * 
	 * @return
	 */
	public abstract Class<?>[] comboParameterTypes() default {}; 

	/**
	 * 是否导出数据
	 * 
	 * @return
	 */
	public abstract boolean isExport() default true;

	/**
	 * 是否为重要字段（整列标红,着重显示）
	 * 
	 * @return
	 */
	public abstract boolean isMark() default false;

	/**
	 * 是否合计当前列
	 * 
	 * @return
	 */
	public abstract boolean isSum() default false;
	
	/**
	 * 转换方法，值为被注解字段或属性所在类的转换方法名，默认不进行转换
	 * <p> 1、convertMethod为作为导出转换方法时，不应设置convertParameterTypes </p>
	 * <p> 2、convertMethod为作为导入转换方法时，需要设置相应的convertParameterTypes </p>
	 * 
	 * @return
	 */
	public abstract String convertMethod() default "";
	
	/**
	 * 转换方法参数类型数组
	 * 
	 * @return
	 */
	public abstract Class<?>[] convertParameterTypes() default {}; 
	
	/**
	 * 合并列中内容相同行，默认：false
	 * 
	 * @return
	 */
	public abstract boolean merge() default false;
	
	/**
	 * 根据指定字段合并列中内容相同行，默认根据当前行内容进行合并
	 * 
	 * @return
	 */
	public abstract String mergeFlag() default "";
	
}

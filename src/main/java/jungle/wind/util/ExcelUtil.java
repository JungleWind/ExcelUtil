package jungle.wind.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jungle.wind.annotation.ExcelFiled;
import jungle.wind.enums.ExcelFileType;
import jungle.wind.excel.ExcelOption;
import jungle.wind.excel.PoiModel;
import jungle.wind.exception.ExcelException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * excel导入导出工具类 
 * 
 * @author JungleWind
 * @since jdk1.6
 * 2018年10月28日
 *  
 */

public class ExcelUtil<T>{
	// 2007以前版本的excel中每个sheet中最多有65536行,此处设置为65500,方便增加合计行等信息
	private static final int MAX_SHEET_SIZE = 65500;
	
    private Class<T> clazz;  
    
    public ExcelUtil(Class<T> clazz) {  
        this.clazz = clazz;  
    }  
    
    /** 
     * 将excel表单数据源的数据导入到list 
     *  
     * @param sheetName 工作表的名称 （如果不指定名称，默认取第一个sheet）
     * @param output 	java输入流 
     * @throws Exception 
     */  
	public List<T> importExcelToList(String sheetName, InputStream input) throws Exception {  
        List<T> list = new ArrayList<T>(); 
        Workbook book = WorkbookFactory.create(input);
        try {  
        	Sheet sheet = null;
            // 如果指定sheet名,则取指定sheet中的内容.  
            if (StringUtils.isNotBlank(sheetName)) {  
                sheet = book.getSheet(sheetName);
            }  
            // 如果传入的sheet名不存在则默认指向第1个sheet.  
            if (sheet == null) {  
                sheet = book.getSheetAt(0);  
            }  
            // 得到数据的行数  
            int rows = sheet.getLastRowNum();  
            // 有数据时才处理  
            if (rows > 0) {
            	// 第一行为标题行
            	Row titleRow = sheet.getRow(sheet.getFirstRowNum());
                // 得到类的所有field  
                Field[] allFields = clazz.getDeclaredFields();  
                // 定义一个map用于存放列名和field  
                Map<String, Field> fieldsMap = new HashMap<String, Field>();  
                for (int i = 0; i < allFields.length; i++) {
                    // 将有注解的field存放到map中  
                    if (existExcelFiled(allFields[i])) {
                        // 设置类的私有字段属性可访问  
                    	allFields[i].setAccessible(true);  
                    	ExcelFiled excelFiled = allFields[i].getAnnotation(ExcelFiled.class);
                        fieldsMap.put(excelFiled.name(), allFields[i]);  
                    }  
                }  
                // 从第2行开始取数据,默认第1行是表头  
                for (int i = 1; i <= rows; i++) {  
                    // 得到一行中的所有单元格对象.  
                    Row row = sheet.getRow(i);  
                    Iterator<Cell> cells = row.cellIterator();  
                    T entity = null;  
                    int index = 0;  
                    while (cells.hasNext()) {  
                        // 单元格中的内容.  
                    	Cell cell = cells.next();
                    	cell.setCellType(CellType.STRING);
                        String c = cell.getStringCellValue();  
                        if (StringUtils.isBlank(c)) {  
                            continue;  
                        }  
                        // 如果不存在实例则新建  
                        entity = (entity == null ? clazz.newInstance() : entity);  
                        Cell titlCell = titleRow.getCell(index);
                        titlCell.setCellType(CellType.STRING);
                        // 从map中得到对应列的field  
                        Field field = fieldsMap.get(titlCell.getStringCellValue());  
                        if (field == null) {  
                            continue;  
                        }  
                        // 取得类型,并根据对象类型设置值.  
                        Class<?> fieldType = field.getType();  
                        ExcelFiled excelFiled = field.getAnnotation(ExcelFiled.class);
                        if (StringUtils.isNotBlank(excelFiled.convertMethod())) {
							Method convertMethod = clazz.getMethod(excelFiled.convertMethod(), excelFiled.convertParameterTypes());
							field.set(entity, convertMethod.invoke(entity, c));
						} else if (String.class == fieldType) {  
                            field.set(entity, String.valueOf(c));  
                        } else if (BigDecimal.class == fieldType) {  
                            c = c.indexOf("%") != -1 ? c.replace("%", "") : c;  
                            field.set(entity, BigDecimal.valueOf(Double.valueOf(c)));  
                        } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {  
                            field.set(entity, Integer.parseInt(c));  
                        } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {  
                            field.set(entity, Long.valueOf(c));  
                        } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {  
                            field.set(entity, Float.valueOf(c));  
                        } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {  
                            field.set(entity, Short.valueOf(c));  
                        } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {  
                            field.set(entity, Double.valueOf(c));  
                        } else if (Character.TYPE == fieldType) {  
                            if ((c != null) && (c.length() > 0)) {  
                                field.set(entity, Character.valueOf(c.charAt(0)));  
                            }  
                        }
                        index++;  
                    }  
                    if (entity != null) {  
                        list.add(entity);  
                    }  
                }  
            }  
        } catch (Exception e) {
        	throw new Exception(e.toString());
        }
        return list;  
    } 
	
	/**
	 * 将list数据导出到excel
	 * @param list	数据源
	 * @param title	Excel标题
	 * @param sheetName	sheet名称
	 * @param outPut	输出流
	 * @return
	 * @throws Exception 
	 */
	public void exportListToExcel(List<T> list, ExcelOption option, OutputStream output) {
		if (null == list) {
			throw new RuntimeException(new ExcelException("list不能为null"));
		}
		try {
			// 得到所有定义字段
			Field[] allFields = clazz.getDeclaredFields();
			List<Field> fields = new ArrayList<Field>();
			// 导出字段的getXX方法
			List<Method> getMethods = new ArrayList<Method>();
			Map<String, Method> methodMap = new HashMap<String, Method>();
			// 导出字段的convert方法
			Map<String, Method> convertMethodMap = new HashMap<String, Method>();
			// 合并标记字段
			Map<String, Method> mergeFlagMap = new HashMap<String, Method>();
			// 导出字段的combo方法
			Map<String, Method> comboMethodMap = new HashMap<String, Method>();
			
			// 得到所有field并存放到一个list中
			for (Field field : allFields) {
				if (existExcelFiled(field)) {
					ExcelFiled excelFiled = field.getAnnotation(ExcelFiled.class);
					fields.add(field);
					String fildName = field.getName();
					StringBuffer getMethodBuf = new StringBuffer("get");
					getMethodBuf.append(fildName.substring(0, 1).toUpperCase()).append(fildName.substring(1));
					Method getMethod = clazz.getMethod(getMethodBuf.toString(), new Class[]{});
					getMethods.add(getMethod);
					methodMap.put(getMethodBuf.toString(), getMethod);
					if (StringUtils.isNotBlank(excelFiled.convertMethod())) {
						try {
							Method convertMethod = clazz.getMethod(excelFiled.convertMethod(), new Class[]{});
							convertMethodMap.put(excelFiled.convertMethod(), convertMethod);
						} catch (Exception e) {
							String message = assembleNoSuchMethodExceptionMessage(excelFiled.convertMethod(), resolveParameterTypes(new Class[]{}));
							throw new ExcelException(message, e);
						}
					}
					if (StringUtils.isNotBlank(excelFiled.mergeFlag())) {
						try {
							Method mergeFlagMethod = clazz.getMethod(assembleGetMethod(excelFiled.mergeFlag()), new Class[]{});
							mergeFlagMap.put(excelFiled.mergeFlag(), mergeFlagMethod);
						} catch (Exception e) {
							String message = "在类：" + clazz.getName() + " 中没有找到名为[" + excelFiled.mergeFlag() + "]的属性或字段" ;
							throw new ExcelException(message, e);
						}
					}
					if (StringUtils.isNotBlank(excelFiled.comboMethod())) {
						Method comboMethod;
						// 检查方法是否存在
						try {
							comboMethod = clazz.getMethod(excelFiled.comboMethod(), excelFiled.comboParameterTypes());
						} catch (Exception e) {
							String message = assembleNoSuchMethodExceptionMessage(excelFiled.comboMethod(), resolveParameterTypes(excelFiled.comboParameterTypes()));
							throw new ExcelException(message, e);
						}
						// 检查返回值类型
						try {
							Object array = comboMethod.invoke(clazz.newInstance(), new Object[]{});
							@SuppressWarnings("unused")
							String[] strArr = new String[Array.getLength(array)];
						} catch (Exception e) {
							String message = assembleReturnTypeExceptionMessage(excelFiled.comboMethod(), resolveParameterTypes(excelFiled.comboParameterTypes()), "String类型数组");
							throw new ExcelException(message, e);
						}
						comboMethodMap.put(excelFiled.comboMethod(), comboMethod);
					}
				}
			}
			// 产生工作薄对象
			Workbook workbook = null;
			if (ExcelFileType.XLS == option.getExcelFileType()) {
				workbook = new HSSFWorkbook();
			} else if (ExcelFileType.XLSX == option.getExcelFileType()) {
				workbook = new XSSFWorkbook();
			}
			// 取出一共有多少个sheet
			int sheetNums = calcSheetNums(list);
			
			for (int index = 0; index <= sheetNums; index++) {
				// 产生工作表
				Sheet sheet = workbook.createSheet();
				// 设置工作表名称
				workbook.setSheetName(index, option.getSheetName() + (index + 1));
				// 产生一行
				Row row = sheet.createRow(0);
				// 产生一个单元格
				Cell cell = row.createCell(0);
				
				// 居中样式
				CellStyle centerStyle = buildCenterStyle(workbook);
				
				/* 创建标题行 */
				CellStyle titleStyle = buildTitleStyle(workbook);	//标题样式
				cell.setCellStyle(titleStyle);	//设置标题样式
				createTitle(sheet, row, cell, fields.size(), option.getTitle());//创建标题				
				
				/* 创建列头名称 */
				CellStyle normalStyle = buildNormalStyle(workbook);	//普通样式
				CellStyle markStyle = buildMarkStyle(workbook);	//标记样式
				row = sheet.createRow(2);	// 创建第三行（该行为列名称）
				for (int i = 0; i < fields.size(); i++) {
					Field field = fields.get(i);
					ExcelFiled excelFiled = field.getAnnotation(ExcelFiled.class);
					// 根据指定的顺序获得列号
					int colNo = getExcelColNo(i, excelFiled.column());
					// 创建单元格
					cell = row.createCell(colNo);
					// 设置单元格颜色、样式
					if (excelFiled.isMark()) {
						cell.setCellStyle(markStyle);
					} else {
						cell.setCellStyle(normalStyle);
					}
					// 设置列宽
					sheet.setColumnWidth(i, (int) ((excelFiled.name().getBytes().length <= 4 ? 6 : excelFiled.name().getBytes().length) * 1.5 * 256));
					// 设置列中内容为String类型
					cell.setCellType(CellType.STRING);
					// 写入列名
					cell.setCellValue(excelFiled.name());
					// 设置本列只能选择，不能输入
					if (StringUtils.isNoneBlank(excelFiled.comboMethod())) {
						Method comboMethod = comboMethodMap.get(excelFiled.comboMethod());
						Object array = comboMethod.invoke(clazz.newInstance(), new Object[]{});
						
						String[] comboArray = new String[Array.getLength(array)];
						for (int j = 0; j < comboArray.length; j++) {
							comboArray[j] = Array.get(array, j).toString();
						}
						DataValidationHelper helper = sheet.getDataValidationHelper();
						CellRangeAddressList addressList = new CellRangeAddressList(row.getRowNum() + 1, MAX_SHEET_SIZE, colNo, colNo);
						DataValidationConstraint constraint = helper.createExplicitListConstraint(comboArray);
						DataValidation dataValidation = helper.createValidation(constraint, addressList);
						sheet.addValidationData(dataValidation);
					}
				}
				
				/* 创建内容 */
				Map<String, PoiModel> poiModelMap = new HashMap<String, PoiModel>();
				int startRowNo = index * MAX_SHEET_SIZE;	//开始行号
				int endRowNo = Math.min(startRowNo + MAX_SHEET_SIZE, list.size());//结束行号
				// 写入各条记录，每条记录对应excel中的一行
				for (int i = startRowNo; i < endRowNo; i++) {
					int rowNo = i + 3 -startRowNo; 	//标题占2行和列名称占1行，因此需要从[i+3-startRowNo]行开始创建新行
					row = sheet.createRow(rowNo);
					T vo = (T)list.get(i); // 需要导出的对象
					for (int j = 0; j < fields.size(); j++) {
						Field field = fields.get(j);
						field.setAccessible(true);
						ExcelFiled excelFiled = field.getAnnotation(ExcelFiled.class);
						if (excelFiled.isExport()) {
							// 根据指定的顺序获得列号
							int colNo = getExcelColNo(j, excelFiled.column());
							// 创建单元格
							cell = row.createCell(colNo);
							// 设置单元格颜色、样式
							if (excelFiled.isMark()) {
								cell.setCellStyle(markStyle);
							} else {
								cell.setCellStyle(normalStyle);
							}
							
							/* 设置单元格内容 */
							Method method = methodMap.get(assembleGetMethod(field.getName()));
							String fieldValue = "";
							// 当vo中字段的值为null时，invoke方法会抛出NullPointerException，此处对异常做一下处理，防止导出失败
							try {
								fieldValue = method.invoke(vo, new Object[]{}).toString();
							} catch (Exception e) {
								//ignore
							}
							
							if (StringUtils.isNotBlank(excelFiled.convertMethod())) {
								Method convertMethod = convertMethodMap.get(excelFiled.convertMethod());
								String convertValue = convertMethod.invoke(vo, new Object[]{}).toString();
								cell.setCellValue(convertValue);
							} else {
								cell.setCellValue(fieldValue);
							}
							
							/* 合并列 */ 
							if (excelFiled.merge()) {
								cell.setCellStyle(centerStyle);
								String mergeValue = cell.getStringCellValue();
								String flagContent = "";
								if (StringUtils.isNotBlank(excelFiled.mergeFlag())) {
									Method mergeFlagMethod = clazz.getMethod(assembleGetMethod(excelFiled.mergeFlag()), new Class[]{});
									flagContent = mergeFlagMethod.invoke(vo, new Object[]{}).toString();
								}
								PoiModel poiModel = poiModelMap.get(assembleGetMethod(field.getName()));
								if (null == poiModel) {
									poiModel = new PoiModel();
									poiModel.setRowNo(rowNo);
									poiModel.setContent(mergeValue);
									poiModel.setMergeFlagContent(flagContent);
									poiModelMap.put(assembleGetMethod(field.getName()), poiModel);
								} else {
									if (needMerge(poiModel, mergeValue, flagContent, field)) {
										// 合并单元格必须2行或以上
										if (poiModel.getRowNo() != rowNo - 1) {
											sheet.addMergedRegion(new CellRangeAddress(poiModel.getRowNo(), rowNo - 1, colNo, colNo));
										}
										poiModel.setRowNo(rowNo);
										poiModel.setContent(mergeValue);
										poiModel.setMergeFlagContent(flagContent);
										poiModelMap.put(assembleGetMethod(field.getName()), poiModel);
									} else {
										// 最后一行无法比较，直接合并
										if (rowNo == (endRowNo - index * MAX_SHEET_SIZE) + 2) {
											if (poiModel.getRowNo() != rowNo) {
												sheet.addMergedRegion(new CellRangeAddress(poiModel.getRowNo(), rowNo, colNo, colNo));
											}
										}
									}
								}
							}
							
						}
					}
				}
				
				/* 创建合计 */
				Row lastRow = sheet.createRow(sheet.getLastRowNum() + 1);
				for (int i = 0; i < fields.size(); i++) {
					Field field = fields.get(i);
					ExcelFiled excelFiled = field.getAnnotation(ExcelFiled.class);
					if (excelFiled.isSum()) {
						int colNo = getExcelColNo(i, excelFiled.column());
						BigDecimal total = BigDecimal.ZERO;
						for (int j = 3; j <= sheet.getLastRowNum(); j++) {
							Row countRow = sheet.getRow(j);
							if (null != countRow) {
								Cell countCell = countRow.getCell(colNo);
								if (null != countCell && CellType.STRING == countCell.getCellTypeEnum()) {
									if (StringUtils.isNotBlank(countCell.getStringCellValue())) {
										total = total.add(new BigDecimal(countCell.getStringCellValue()));
									}
								}
							}
						}
						Cell sumCell = lastRow.createCell(colNo);
						sumCell.setCellValue("合计：" + total);
					}
				}
			}
			workbook.write(output);
			output.flush();
			output.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 组装返回值类型异常信息
	 * @param methodName
	 * @return
	 */
	private String assembleReturnTypeExceptionMessage(String methodName, String parameterTypes, String returnMsg) {
		StringBuffer message = new StringBuffer("类[" + clazz.getName() + "]中的");
		message.append("方法名[" + methodName + "]，");
		message.append("参数类型[" + parameterTypes + "]");
		message.append("必须返回" + returnMsg);
		return message.toString();
	}
	
	/**
	 * 组装无此方法异常信息
	 * @param methodName
	 * @return
	 */
	private String assembleNoSuchMethodExceptionMessage(String methodName, String parameterTypes) {
		StringBuffer message = new StringBuffer("在类[" + clazz.getName() + "]中，没有找到");
		message.append("方法名[" + methodName + "]，");
		message.append("参数类型[" + parameterTypes + "]");
		message.append("的方法");
		return message.toString();
	}
	
	/**
	 * 解析参数类型
	 * @param parameterTypes
	 * @return
	 */
	private String resolveParameterTypes(Class<?>[] parameterTypes) {
		StringBuffer paramTypeBuffer = new StringBuffer();
		for (int i = 0; i < parameterTypes.length; i++) {
			if (i < parameterTypes.length - 1) {
				paramTypeBuffer.append(parameterTypes[i].getName()).append(";");
			} else {
				paramTypeBuffer.append(parameterTypes[i].getName());
			}
		}
		return paramTypeBuffer.toString();
	}
	
	/**
	 * 是否存在ExcelField注解
	 * @param field
	 * @return
	 */
	private boolean existExcelFiled(Field field) {
		Annotation[] annos = field.getAnnotations();
		for (int i = 0; i < annos.length; i++) {
			if (annos[i].annotationType().equals(ExcelFiled.class)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否需要合并
	 * @param poiModel
	 * @param mergeValue
	 * @param flagContent
	 * @param field
	 * @return
	 */
	private boolean needMerge(PoiModel poiModel, String mergeValue, String flagContent, Field field) {
		ExcelFiled excelFiled = field.getAnnotation(ExcelFiled.class);
		if (StringUtils.isNotBlank(excelFiled.mergeFlag())) {
			if (!poiModel.getMergeFlagContent().equals(flagContent)) {
				return true;
			} 
			return false;
		} 
		return !poiModel.getContent().equals(mergeValue);
	}
	
	/**
	 * 组装getXX方法
	 * @param field
	 * @return
	 */
	private String assembleGetMethod(String fildName) {
		StringBuffer getMethodBuf = new StringBuffer("get");
		getMethodBuf.append(fildName.substring(0, 1).toUpperCase()).append(fildName.substring(1));
		return getMethodBuf.toString();
	}
	
	/**
	 * 构建居中样式
	 * @param workbook
	 * @return
	 */
	private CellStyle buildCenterStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		/* 垂直居中 */
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直
		cellStyle.setAlignment(HorizontalAlignment.CENTER);	//水平
		return cellStyle;
	}
	
	/**
	 * 构建标题样式
	 * @param workbook
	 * @return
	 */
	private CellStyle buildTitleStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		/* 背景颜色 */
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(HSSFColorPredefined.SKY_BLUE.getIndex());
		/* 字体 */
		Font font = workbook.createFont();
		font.setFontName("仿宋_GB2312");
		font.setBold(true);
		font.setFontHeightInPoints((short) 16);
		cellStyle.setFont(font);
		/* 垂直居中 */
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		return cellStyle;
	}
	
	/**
	 * 创建标题行
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param totalCols 总列数
	 * @param title
	 */
	private void createTitle(Sheet sheet, Row row, Cell cell, int totalCols, String title) {
		// 写入标题
		cell.setCellValue(title);
		// 创建第二行（标题行占2行，之后合并为1行）
		row = sheet.createRow(1);
		// 合并标题单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, totalCols - 1));
	}
	
	/**
	 * 构建普通样式
	 * @param workbook
	 * @return
	 */
	private CellStyle buildNormalStyle(Workbook workbook) {
		/* 普通列样式 */
		CellStyle normalCellStyle = workbook.createCellStyle();
		Font normalFont = workbook.createFont();
		normalFont.setFontName("Arail narrow"); //字体
		normalFont.setColor(Font.COLOR_NORMAL); //普通颜色字体
		normalCellStyle.setFont(normalFont);
		return normalCellStyle;
	}
	
	/**
	 * 构建标记样式
	 * @param workbook
	 * @return
	 */
	private CellStyle buildMarkStyle(Workbook workbook) {
		/* 标红列样式 */
		CellStyle redCellStyle = workbook.createCellStyle();
		Font redFont = workbook.createFont();
		redFont.setFontName("Arail narrow"); //字体
		redFont.setColor(Font.COLOR_RED); //字体设置红色
		redCellStyle.setFont(redFont);
		return redCellStyle;
	}
	
	/**
	 * 计算需要多少个sheet
	 * @param list
	 * @return
	 */
	private int calcSheetNums(List<T> list) {
		return (int)Math.ceil(list.size() / MAX_SHEET_SIZE);
	}
	
	/**
	 * 将Excel中A,B,C,D,E...列映射成0,1,2,3...
	 * @param defaultNo
	 * @param excelFiled
	 * @return
	 */
	private int getExcelColNo(int defaultNo, String columnName) {
		if (StringUtils.isNotBlank(columnName)) {
			String colNo = columnName.toUpperCase();
			int count = -1;
			char[] cs = colNo.toCharArray();
			for (int i = 0; i < cs.length; i++) {
				count += (cs[i] - 64) * Math.pow(26, cs.length - 1 -i);
			}
			return count;
		}
		return defaultNo;
	}
	
	/**
	 * 处理Excel名称乱码
	 * @param userAgent	浏览器代理
	 * @param fileName	Excel名称
	 */
	 public static String handleExcelName(String userAgent, String fileName) {
        String result = null;
        try {
			if(StringUtils.contains(userAgent, "MSIE")){//IE浏览器
			    result = URLEncoder.encode(fileName,"UTF8");
			}else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
			    result = new String(fileName.getBytes(), "ISO8859-1");
			}else{
			    result = URLEncoder.encode(fileName,"UTF8");//其他浏览器
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.toString());
		}
        return result;
    }
}

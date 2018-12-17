package jungle.wind.exception;

/**
 * Excel工具异常
 * @author JungleWind
 * @since jdk1.6
 * 2018年11月22日
 *  
 */

public class ExcelException extends Exception{

	private static final long serialVersionUID = 1L;

	/**  
	 * ExcelException    
	 */
	public ExcelException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**  
	 * ExcelException
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace    
	 */
	public ExcelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**  
	 * ExcelException
	 * @param message
	 * @param cause    
	 */
	public ExcelException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**  
	 * ExcelException
	 * @param message    
	 */
	public ExcelException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**  
	 * ExcelException
	 * @param cause    
	 */
	public ExcelException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}

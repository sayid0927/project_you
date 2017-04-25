package com.easemob.easeui;

/**
 
 * @author     dsnx
 * @version    YIBA-O2O 2014-12-23
 * @since      YIBA-O2O
 */
public class AppException extends Exception
{
    
    private static final long serialVersionUID = 1L;
    
    public static final short RUNTIME_ERROR = -100;
    
    private int result;
    
    private String error_msg;
    
    public AppException(int result, String error_msg)
    {
        this.error_msg = error_msg;
        this.result = result;
    }
    
    /**
     * 用于发生运行期异常
     * 
     * @param result
     * @param msg
     * @param e
     */
    public AppException(String error_msg, Exception e)
    {
        super(error_msg, e);
        this.result = RUNTIME_ERROR;
        this.error_msg = error_msg;
    }
    
    /**
     * 用于发生运行期异常
     * 
     * @param result
     * @param msg
     * @param e
     */
    public AppException(String error_msg)
    {
        super(error_msg);
        this.result = RUNTIME_ERROR;
        this.error_msg = error_msg;
    }
    
    public int getResult()
    {
        return result;
    }
    
    public String getErrorMsg()
    {
        return error_msg;
    }
    
    @Override
    public String getMessage()
    {
        return getErrorMsg();
    }
    
}

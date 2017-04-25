package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

/**
 * @author     dsnx
 * @version    YIBA-O2O 2014-12-26
 * @since      YIBA-O2O
 */
public class ResettingPasswordRequest extends BaseRequest
{
	/**
	 * @param mobilePhone 手机号
	 * @param password 密码
	 * @param authCode 验证码
	 */
    public ResettingPasswordRequest(String mobilePhone,String password,String authCode)
    {
        addParams("mobilePhone", mobilePhone);
        addParams("password", password);
        addParams("authCode", authCode);
    }
    
    @Override
    protected void fire(String data)
        throws AppException
    
    {
    }
    
    @Override
    protected String method()
    {
        return "/auth/password";
    }

	@Override
	protected boolean isShowLoadingDialog() {
		// TODO Auto-generated method stub
		return true;
	}
    
}

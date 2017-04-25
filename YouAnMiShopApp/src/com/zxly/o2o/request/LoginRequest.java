package com.zxly.o2o.request;

import com.easemob.chatuidemo.HXConstant;
import com.easemob.easeui.AppException;
import com.easemob.easeui.model.IMUserInfoVO;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.util.DESUtils;
import com.zxly.o2o.util.PreferUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author fengrongjian 2015-2-13
 * @description 登录请求
 */
public class LoginRequest extends BaseRequest {
	public String token;
	public IMUserInfoVO user;

	public LoginRequest(String userName, String password) {
		addParams("clientId", Config.getuiClientId);
		addParams("userName", userName);
		addParams("password", password);
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject jo = new JSONObject(data);
			if(jo.has("token")){
				token = jo.getString("token");
			}
			if (jo.has("signKey")) {
				Config.accessKey = DESUtils.decrypt(jo.getString("signKey"), Config.USER_SIGN_KEY);
			}

			//聊天界面只要有登录我们的后台就可以进去，不用先进行环信的账号登录
			HXConstant.isLoginSuccess = true; //标识登录hx成功

//			String userStr = jo.getString("user");
			user = GsonParser.getInstance().getBean(data, IMUserInfoVO.class);

			PreferUtil.getInstance().setLoginToken(token);
			AppController.getInstance().initHXAccount(user,true);   //登录环信
		} catch (JSONException e) {
			throw new AppException("数据解析异常");
		} catch (Exception e) {
			throw new AppException("解密异常");
		}
	}

	@Override
	protected String method() {
		return "auth/shop/login2";
	}

	@Override
	protected boolean isShowLoadingDialog() {
		return true;
	}
	
}

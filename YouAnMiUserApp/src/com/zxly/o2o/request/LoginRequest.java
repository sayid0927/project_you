package com.zxly.o2o.request;

import com.easemob.chatuidemo.HXConstant;
import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.User;
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
	public User user;

	public LoginRequest(String userName, String password, long shopId) {
		addParams("clientId", Config.getuiClientId);
		addParams("userName", userName);
		addParams("password", password);
		addParams("shopId", shopId);
		//addParams("phoneModel",android.os.Build.MODEL);
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject jo = new JSONObject(data);
			token = jo.getString("token");
			String userStr = jo.getString("user");
			user = GsonParser.getInstance().getBean(userStr, User.class);
			PreferUtil.getInstance().setUserId(user.getId());
			PreferUtil.getInstance().setLoginToken(token);
			Config.accessKey = DESUtils.decrypt(user.getSignKey(), Config.USER_SIGN_KEY);

			//聊天界面只要有登录我们的后台就可以进去，不用先进行环信的账号登录
			HXConstant.isLoginSuccess = true; //标识登录hx成功
			AppController.getInstance().doLoginHX(user,true);
		} catch (JSONException e) {
			throw new AppException("数据解析异常");
		} catch (Exception e) {
			throw new AppException("解密异常");
		}

	}

	@Override
	protected String method() {
		return "/auth/login";
	}

	@Override
	protected boolean isShowLoadingDialog() {
		// TODO Auto-generated method stub
		return true;
	}
	
}

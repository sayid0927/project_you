package com.zxly.o2o.account;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

import com.easemob.easeui.model.IMUserInfoVO;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;

public class Account {
	public static IMUserInfoVO user;
	public static String shopName="";

	public static boolean hasLogin(){
		if (StringUtil.isNull(PreferUtil.getInstance().getLoginToken()) || Account.user == null) {
			return false;
		} else {
			return true;
		}
	}

	public static void saveLoginUser(Context context, IMUserInfoVO user) {
		try {
			FileOutputStream outStream = context.openFileOutput(
					"user", Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(outStream);
			oos.writeObject(user);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static IMUserInfoVO readLoginUser(Context context) {
		IMUserInfoVO loginUser = null;
		try {
			FileInputStream fs = context.openFileInput("user");
			ObjectInputStream ois = new ObjectInputStream(fs);
			loginUser = (IMUserInfoVO) ois.readObject();
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return loginUser;
	}
	
}

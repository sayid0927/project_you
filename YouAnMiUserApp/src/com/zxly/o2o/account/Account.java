package com.zxly.o2o.account;

import android.content.Context;

import com.zxly.o2o.model.AddressCountry;
import com.zxly.o2o.model.AppFeature;
import com.zxly.o2o.model.BankProvince;
import com.zxly.o2o.model.ShopInfo;
import com.zxly.o2o.model.User;
import com.zxly.o2o.model.UserMaintain;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *     @author dsnx  
 * 
 * @version 创建时间：2014-12-31 下午6:17:05    类说明: 用于缓存用户数据
 */
public class Account {
	public static String shareUrl;
	public static AppFeature appFeature;
	public static int orderCount;
	public static User user;
	public static ShopInfo shopInfo;
	public static UserMaintain userMaintainInfo;
	public static ArrayList<AddressCountry> areaList;
	public static ArrayList<BankProvince> bankCodeList;

	public static boolean hasLogin(){
		if (StringUtil.isNull(PreferUtil.getInstance().getLoginToken()) || Account.user == null) {
			return false;
		} else {
			return true;
		}
	}

	public static void saveLoginUser(Context context, User user) {
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

	public static User readLoginUser(Context context) {
		User loginUser = null;
		try {
			FileInputStream fs = context.openFileInput("user");
			ObjectInputStream ois = new ObjectInputStream(fs);
			loginUser = (User) ois.readObject();
			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return loginUser;
	}
	
}

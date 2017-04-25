package com.zxly.o2o.util;

import android.content.res.AssetManager;

import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.AddressCountry;
import com.zxly.o2o.model.BankProvince;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AreaUtil {

	public static ArrayList<BankProvince> getBankCodeFromFile() {
		ArrayList<BankProvince> list = new ArrayList<BankProvince>();
		AssetManager s = AppController.getInstance().getResources().getAssets();
		try {
			InputStream is = s.open("bankcode.json");
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String json = new String(buffer, "utf-8");
			is.close();

			TypeToken<List<BankProvince>> type = new TypeToken<List<BankProvince>>() {
			};
			List<BankProvince> bankCodeList = GsonParser.getInstance()
					.fromJson(json, type);
			if (bankCodeList != null && !bankCodeList.isEmpty()) {
				list.addAll(bankCodeList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<AddressCountry> getAreaFromFile() {
		ArrayList<AddressCountry> list = new ArrayList<AddressCountry>();
		AssetManager s = AppController.getInstance().getResources().getAssets();
		try {
			InputStream is = s.open("address.json");
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String json = new String(buffer, "utf-8");
			is.close();
			
			JSONObject obj = new JSONObject(json);
			String data = obj.getString("data");
			TypeToken<List<AddressCountry>> type1 = new TypeToken<List<AddressCountry>>() {
			};
			List<AddressCountry> addressList = GsonParser.getInstance()
					.fromJson(data, type1);
			if (addressList != null && !addressList.isEmpty()) {
				list.addAll(addressList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}

package com.zxly.o2o.util;

import android.content.res.AssetManager;

import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.AddressCity;
import com.zxly.o2o.model.AddressCountry;
import com.zxly.o2o.model.AddressDistrict;
import com.zxly.o2o.model.AddressProvince;
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
		// TODO Auto-generated method stub
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

	public static String getAddressById(List<AddressCountry> areaList,
			Long provinceId, Long cityId, Long areaId) {
		// TODO Auto-generated method stub
		String provinceName = "";
		String cityName = "";
		String districtName = "";
		ArrayList<AddressProvince> provinceList = (ArrayList<AddressProvince>) areaList.get(0).getPrvs();
		ArrayList<AddressCity> cityList = null;
		ArrayList<AddressDistrict> districtList = null;
		for(int i=0; i<provinceList.size(); i++){
			if(provinceId == Long.parseLong(provinceList.get(i).getProvinceId())){
				provinceName = provinceList.get(i).getProvinceName();
				cityList = (ArrayList<AddressCity>) provinceList.get(i).getCitys();
				break;
			}
		}
		if(cityList == null){
			return provinceName;
		}
		for(int i=0; i<cityList.size(); i++){
			if(cityList.get(i).getCityId() == null){
				districtList = (ArrayList<AddressDistrict>) cityList.get(0).getDistricts();
				break;
			} else if(cityId == Long.parseLong(cityList.get(i).getCityId())){
				cityName = cityList.get(i).getCityName();
				districtList = (ArrayList<AddressDistrict>) cityList.get(i).getDistricts();
				break;
			}
		}
		if(districtList == null){
			return provinceName + cityName;
		}
		for(int i=0; i<districtList.size(); i++){
			if(areaId != null && areaId == Long.parseLong(districtList.get(i).getDistrictId())){
				districtName = districtList.get(i).getDistrictName();
				break;
			}
		}
		return provinceName + cityName + districtName;
	}
	
	public static ArrayList<AddressDistrict> getUsedRegion(Integer prvId,
			Integer cityId) {
		// TODO Auto-generated method stub
		ArrayList<AddressProvince> prvList = (ArrayList<AddressProvince>) Account.areaList
				.get(0).getPrvs();
		ArrayList<AddressCity> cityList = null;
		for (AddressProvince province : prvList) {
			if (prvId == Integer.parseInt(province.getProvinceId())) {
				cityList = (ArrayList<AddressCity>) province.getCitys();
				break;
			}
		}
		if (cityId != null && cityId != 359 && cityId != 360 && cityId != 361 && cityId != 362) {
			for (AddressCity city : cityList) {
				if (cityId == Integer.parseInt(city.getCityId())) {
					return (ArrayList<AddressDistrict>) city.getDistricts();// district可能为空（广东东莞），也可能不为空（广东深圳），
				}
			}
		} else {
			return (ArrayList<AddressDistrict>) cityList.get(0).getDistricts();//cityId为空（上海）
		}
		return null;
	}

}

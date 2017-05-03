/*
 * 文件名：DataUtil.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： DataUtil.java
 * 修改人：wuchenhui
 * 修改时间：2015-2-5
 * 修改内容：新增
 */
package com.zxly.o2o.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.application.Config;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-2-5
 * @since      YIBA-O2O
 */
public class DataUtil {

	
	public static boolean stringIsNull(String str){
		if(str!=null&&!"".equals(str.trim())){
			return false;
		}
		return true;
	}
	
	
	public static boolean arrayIsNull(Object[] array){
		if(array!=null&&array.length>0){
			return false;
		}
		return true;
	}
	
	public static boolean listIsNull(List list){
		if(list!=null&&list.size()>0){
			return false;
		}
		return true;
	}

	public static boolean mapIsNull(Map map){
		if(map!=null&&map.size()>0){
			return false;
		}
		return true;
	}
	public static int getIndex(String strs[],String str){
		if(arrayIsNull(strs)||stringIsNull(str))
			return -1;
		
		for (int i = 0; i < strs.length; i++) {
			if(strs[i].equals(str))
				return i;
		}
		return -1;
	}
	
	public static int getIndex(List<String> strList,String str){
		if(listIsNull(strList)||stringIsNull(str))
			return -1;		
		
		for (int i = 0; i < strList.size(); i++) {
			if(strList.get(i).equals(str))
				return i;
		}
		return -1;
	}
	
	public static <T> T getGsonBean(JSONObject jsonRoot,String tagName,Class<T> cls){
		try {
			if (jsonRoot.has(tagName)) {
				String str = jsonRoot.getString(tagName);
				return (T) GsonParser.getInstance().getBean(str, cls);
			}
		} catch (Exception e) {

		}
		return null;
	}
	
	public static <T> T getGsonList(JSONObject jsonRoot,String tagName,Class<T> cls){
		try {
			if (jsonRoot.has(tagName)) {
				String str = jsonRoot.getString(tagName);
				TypeToken types = new TypeToken() {}.get(cls);
				GsonParser.getInstance().fromJson(str,types);
			}
		} catch (Exception e) {

		}
		return null;
	}
	/**
	 * 在dest中删除src已有的元素
	 * 
	 * 
	 * @param src
	 * @param dest
	 */
	public static void deleteRepeat(List src, List dest) {
		for (Iterator it = src.iterator(); it.hasNext();) {
			Object object =  it.next();
			if (dest.contains(object))
			{
				dest.remove(object);
			}
		}
	}
	public static String encodeBase64(String data){
		if(!DataUtil.stringIsNull(data))
			return new String(android.util.Base64.encode(data.getBytes(), android.util.Base64.DEFAULT));

		return "";
	}

}

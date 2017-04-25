package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.util.AreaUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-1-22
 * @description 收货地址列表网络请求
 */
public class AddressListRequest extends BaseRequest {
	private List<UserAddress> addressList = new ArrayList<UserAddress>();

	public AddressListRequest() {
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			TypeToken<List<UserAddress>> type = new TypeToken<List<UserAddress>>() {
			};
			List<UserAddress> list = GsonParser.getInstance().fromJson(data,
					type);
			if (!listIsEmpty(list)) {
				addressList.addAll(list);
			}
			if (Account.areaList == null) {
				Account.areaList = AreaUtil.getAreaFromFile();
			}
			if(Account.areaList != null && !Account.areaList.isEmpty()) {
				for (int i = 0; i < addressList.size(); i++) {
					addressList.get(i).setPcaName(
							AreaUtil.getAddressById(Account.areaList, addressList
									.get(i).getProvinceId(), addressList.get(i)
									.getCityId(), addressList.get(i).getAreaId()));
				}
			}
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "address/list";
	}

	public List<UserAddress> getAddressList() {
		return addressList;
	}

}

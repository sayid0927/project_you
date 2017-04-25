package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-5-22
 * @description 用户银行卡列表网络请求
 */
public class PayMyBankcardsRequest extends BaseRequest {
	private ArrayList<UserBankCard> userBankcardList = new ArrayList<UserBankCard>();
	private ArrayList<UserBankCard> savingsBankcardList = new ArrayList<UserBankCard>();

	public PayMyBankcardsRequest() {
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			TypeToken<List<UserBankCard>> type = new TypeToken<List<UserBankCard>>() {
			};
			List<UserBankCard> bankcardList = GsonParser.getInstance()
					.fromJson(data, type);
			if (!listIsEmpty(bankcardList)) {
				userBankcardList.addAll(bankcardList);
				for (UserBankCard userBankCard : userBankcardList) {
					if (Constants.TYPE_BANKCARD_SAVINGS == userBankCard.getBankType()) {
						savingsBankcardList.add(userBankCard);
					}
				}
			}
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected boolean isShowLoadingDialog() {
		return super.isShowLoadingDialog();
	}

	@Override
	protected String method() {
		return "pay/my_card_list";
	}

	public ArrayList<UserBankCard> getBankcardList() {
		return userBankcardList;
	}

	public ArrayList<UserBankCard> getSavingsBankcardList() {
		return savingsBankcardList;
	}
}

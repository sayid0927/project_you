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
 * @description 支持银行网络请求
 */
public class PayApplyBankRequest extends BaseRequest {
	private ArrayList<UserBankCard> bankcardsList = new ArrayList<UserBankCard>();
	private ArrayList<UserBankCard> savingList = new ArrayList<UserBankCard>();
	private ArrayList<UserBankCard> creditList = new ArrayList<UserBankCard>();
	private ArrayList<UserBankCard> takeoutSupportList = new ArrayList<UserBankCard>();

	public PayApplyBankRequest() {
	}

	private void filterList(ArrayList<UserBankCard> bankcardList){
		for (UserBankCard userBankCard : bankcardList) {
			if (Constants.TYPE_BANKCARD_SAVINGS == userBankCard.getBankType()) {
				savingList.add(userBankCard);
			} else {
				creditList.add(userBankCard);
			}
		}
		for (UserBankCard userBankCard : savingList) {
			String bankNo = userBankCard.getBankNo();
			int withdrawType = userBankCard.getWithdrawType();
			if (1 == withdrawType) {
				takeoutSupportList.add(userBankCard);
			}
		}
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			TypeToken<List<UserBankCard>> type = new TypeToken<List<UserBankCard>>() {
			};
			List<UserBankCard> bankcardList = GsonParser.getInstance()
					.fromJson(data, type);
			if (!listIsEmpty(bankcardList)) {
				bankcardsList.addAll(bankcardList);
			}
			filterList(bankcardsList);
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "pay/applyRefund";
	}

	public ArrayList<UserBankCard> getBankcardList() {
		return bankcardsList;
	}

	public ArrayList<UserBankCard> getSavingList() {
		return savingList;
	}

	public ArrayList<UserBankCard> getCreditList() {
		return creditList;
	}

	public ArrayList<UserBankCard> getTakeoutSupportList() {
		return takeoutSupportList;
	}
}

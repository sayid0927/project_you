package com.zxly.o2o.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayApplyBankRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;

public class ApplyBanksFragment extends BaseFragment {
	public static final int BANK_SAVINGS_CARD = 0;
	public static final int BANK_CREDIT_CARD = 1;

	private ListView mListView;
	private ObjectAdapter adapter;
	private LoadingView loadingView;
	private boolean isInitData = false;
	private int status = -1;
	private PayApplyBankRequest payApplyBankRequest;
	private ArrayList<UserBankCard> bankcardsList;
	private ArrayList<UserBankCard> savingsList;
	private ArrayList<UserBankCard> creditList;
	private ArrayList<UserBankCard> takeoutSupportList;
	private boolean hasLoad = false;
	private int type;

	public void init(int status, int type) {
		this.status = status;
		this.type = type;
		isInitData = true;
		if (adapter != null) {
			if(!hasLoad){
				adapter.clear();
				loadData(status);
			}
		}
	}

	@Override
	protected void initView() {
		loadingView = (LoadingView) findViewById(R.id.view_loading);
		mListView = (ListView) findViewById(R.id.bank_list);
		if (adapter == null) {
			adapter = new ApplyBankAdapter(getActivity());
		}
		mListView.setAdapter(adapter);
		if (isInitData)
			loadData(status);
	}

	@Override
	protected int layoutId() {
		return R.layout.win_pay_applybanks;
	}

	private void loadData(final int status) {
		if (DataUtil.listIsNull(adapter.getContent()))
			loadingView.startLoading();

		payApplyBankRequest = new PayApplyBankRequest();
		payApplyBankRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
				bankcardsList = payApplyBankRequest.getBankcardList();
				savingsList = payApplyBankRequest.getSavingList();
				creditList = payApplyBankRequest.getCreditList();
				takeoutSupportList = payApplyBankRequest.getTakeoutSupportList();
				if (!DataUtil.listIsNull(bankcardsList)) {
					adapter.clear();
					if (BANK_SAVINGS_CARD == status) {
						if(Constants.TYPE_TAKEOUT == type){
							adapter.addItem(takeoutSupportList, true);
						} else {
							adapter.addItem(savingsList, true);
						}
					} else if (BANK_CREDIT_CARD == status) {
						adapter.addItem(creditList, true);
					}
					loadingView.onLoadingComplete();
				} else {
					loadingView.onDataEmpty();
				}
				hasLoad = true;
			}

			@Override
			public void onFail(int code) {
				if (DataUtil.listIsNull(adapter.getContent()))
					loadingView.onLoadingFail();
				hasLoad = true;
			}
		});
		payApplyBankRequest.start(getActivity());
	}

	/**
	 * 支持银行列表
	 * */
	class ApplyBankAdapter extends ObjectAdapter {

		public ApplyBankAdapter(Context _context) {
			super(_context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflateConvertView();
				holder = new ViewHolder();
				holder.imgBankLogo = (NetworkImageView) convertView
						.findViewById(R.id.img_bank_logo);
				holder.txtBankName = (TextView) convertView
						.findViewById(R.id.txt_bank_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			UserBankCard userBankCard = (UserBankCard) getItem(position);
			holder.imgBankLogo.setDefaultImageResId(R.drawable.img_shortcut_pay);
			holder.imgBankLogo.setImageUrl(userBankCard.getImageBanner(),
					AppController.imageLoader);
			ViewUtils.setText(holder.txtBankName, userBankCard.getBankName());
			return convertView;
		}

		@Override
		public int getLayoutId() {
			return R.layout.item_apply_bank;
		}

		class ViewHolder {
			NetworkImageView imgBankLogo;
			TextView txtBankName;
		}
	}

}

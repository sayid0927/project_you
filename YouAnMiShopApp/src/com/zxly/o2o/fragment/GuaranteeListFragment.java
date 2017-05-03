/*
 * 文件名：MyOrderListFragment.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： MyOrderListFragment.java
 * 修改人：wuchenhui
 * 修改时间：2015-5-27
 * 修改内容：新增
 */
package com.zxly.o2o.fragment;


import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.GuaranteeDetailAct;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.dialog.BaseDialog;
import com.zxly.o2o.model.GuaranteeInfo;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import org.json.JSONObject;

import java.util.List;

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
 * @version    YIBA-O2O 2015-5-27
 * @since      YIBA-O2O
 */
public class GuaranteeListFragment extends BaseFragment implements View.OnClickListener {

	public static final int TYPE_AVAILABLE=1;
	public static final int TYPE_NOT_AVAILABLE=2;
	private PullToRefreshListView mListView;
	private ObjectAdapter adapter;
	private LoadingView loadingView;
	DataListRequest request;
	private int pageIndex=1;
	private int type;
	GuaranteeWarningDialog guaranteeWarningDialog;

	ResponseStateListener dataListresponseStateListener=new ResponseStateListener() {
		@Override
		public void onOK() {
			reFreshUI(TYPE_LOAD_DATA_SUCCESS,pageIndex);
		}

		@Override
		public void onFail(int code) {
			reFreshUI(TYPE_LOAD_DATA_FAILD,pageIndex);
		}
	};


	public static GuaranteeListFragment newInstance(int type){
		GuaranteeListFragment f=new GuaranteeListFragment();
		Bundle args = new Bundle();
		args.putInt("status", 1);
		args.putInt("type",type);
		f.setArguments(args);
		return f;
	}

	
	@Override
	protected void initView(Bundle bundle) {
		loadingView=(LoadingView) findViewById(R.id.view_loading11);
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
        mListView.setIntercept(true);
		ViewUtils.setRefreshText(mListView);

		guaranteeWarningDialog =new GuaranteeWarningDialog();

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
					pageIndex=1;
					loadData(pageIndex);
				} else {
					loadData(pageIndex);
				}
			}
		});
		 
		loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
			@Override
			public void onLoading() {
				loadingView.startLoading();
				loadData(pageIndex);
			}
		});

		type=bundle.getInt("type");
		if (request==null){
			request = new DataListRequest(type);
			request.setOnResponseStateListener(dataListresponseStateListener);
		}

		if (adapter==null){
			adapter = new GuaranteeManageListAdater(getActivity());
		}

//		List<GuaranteeInfo> guaranteeInfoList=new ArrayList<GuaranteeInfo>();
//		for (int i=0;i<9;i++){
//			GuaranteeInfo guaranteeInfo=new GuaranteeInfo();
//			guaranteeInfo.setOrderStatus(i);
//			guaranteeInfo.setCreateTime(System.currentTimeMillis());
//			guaranteeInfo.setOrderNo("11111111111");
//			guaranteeInfo.setPrice(55.55);
//			guaranteeInfo.setContractNum("a10000000000");
//			guaranteeInfo.setPayTime(System.currentTimeMillis());
//
//			GuaranteeInfo.InsuranceProduct insuranceProduct=new GuaranteeInfo.InsuranceProduct();
//			insuranceProduct.setName("666保");
//			guaranteeInfo.setProduct(insuranceProduct);
//
//			GuaranteeInfo.MOrderInfo orderInfo=new GuaranteeInfo.MOrderInfo();
//			orderInfo.setUserName("小B");
//			orderInfo.setUserPhone("13246602006");
//			orderInfo.setPhoneModel("垃圾小米");
//			orderInfo.setPhoneImei("8888888888888888");
//			orderInfo.setPhonePrice(666.01);
//			guaranteeInfo.setOrderInfo(orderInfo);
//
//			guaranteeInfoList.add(guaranteeInfo);
//		}
//
    	mListView.setAdapter(adapter);
//		adapter.addItem(guaranteeInfoList, true);
	}


	public static final int TYPE_LOAD_DATA_SUCCESS=1;
	public static final int TYPE_LOAD_DATA_FAILD=-1;
	private synchronized void reFreshUI(int type,int pageIndex){

		switch (type){

			case TYPE_LOAD_DATA_SUCCESS:
				if(pageIndex==1)
					adapter.clear();

				if(!DataUtil.listIsNull(request.getGuaranteeInfoList())){
					adapter.addItem(request.getGuaranteeInfoList());
					request.setGuaranteeInfoList(null);
					loadingView.onLoadingComplete();
					this.pageIndex++;
				}else {
					if(pageIndex==1){
						loadingView.onDataEmpty("还没有人购买过延保服务呢,要加油哦!",R.drawable.img_default_happy);
					}else {
						ViewUtils.showToast("暂时没有更多了！");
					}

				}

				mListView.onRefreshComplete();
				adapter.notifyDataSetChanged();
				if(request.hasNextPage){
					mListView.setMode(Mode.BOTH);
				} else {
					mListView.setMode(Mode.PULL_FROM_START);
				}
				break;

			case TYPE_LOAD_DATA_FAILD:
				if(adapter.getContent()==null || adapter.getContent().isEmpty()){
					loadingView.onLoadingFail();
				}else {
					ViewUtils.showToast("数据加载失败,请检查你的网络！");
				}
				mListView.onRefreshComplete();
				break;

		}

	}

//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		ViewUtils.showToast(requestCode+"   result="+resultCode);
//	}

//	@Override
//	protected void loadInitData() {
//		if(DataUtil.listIsNull(adapter.getContent())){
//			loadData(1);
//		}
//	}

	@Override
	public void onResume() {
		super.onResume();
		pageIndex=1;
		loadData(1);
	}

	public void loadData(int pageIndex) {
		if(DataUtil.listIsNull(adapter.getContent()))
		  loadingView.startLoading();

		request.addParams("pageIndex", pageIndex);
		request.start(getActivity());
	}

	@Override
	protected int layoutId() {
		return R.layout.tag_listview;
	}


	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {

	}





	static class DataListRequest extends BaseRequest {

		List<GuaranteeInfo> guaranteeInfoList;
		public boolean hasNextPage;

		public DataListRequest(int type){
			addParams("type",type);
			addParams("shopId",Account.user.getShopId());
		}

		public List<GuaranteeInfo> getGuaranteeInfoList() {
			return guaranteeInfoList;
		}

		public void setGuaranteeInfoList(List<GuaranteeInfo> guaranteeInfoList) {
			this.guaranteeInfoList = guaranteeInfoList;
		}

		@Override
		protected void fire(String data) throws AppException {
			try {
				JSONObject jsonRoot = new JSONObject(data);
				if(jsonRoot.has("orders")){
					TypeToken<List<GuaranteeInfo>> types = new TypeToken<List<GuaranteeInfo>>() {};
					guaranteeInfoList= GsonParser.getInstance().fromJson(jsonRoot.getString("orders"), types);
				}

				if(guaranteeInfoList.size()<20){
					hasNextPage = false;
				} else {
					hasNextPage = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String method() {
			return "/insurance/order/mylist";
		}

	}




	public static class GuaranteeWarningDialog extends BaseDialog {


		@Override
		protected void initView() {
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,-2);
			params.setMargins(20,20,20,20);
			contentView.setLayoutParams(params);

			findViewById(R.id.btn_konw).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if(callBack!=null)
						callBack.onCall();
				}

			});
		}

		private CallBack callBack;
		public void show(String info,CallBack callBack){
			this.callBack=callBack;
			((TextView)findViewById(R.id.txt_tips)).setText(info);
			super.show();
		}

		@Override
		protected void doOnDismiss() {
			if(callBack!=null)
				callBack.onCall();
		}

		@Override
		public int getGravity() {
			return Gravity.CENTER;
		}

		@Override
		protected boolean isShowAnimation() {
			return false;
		}

		@Override
		public int getLayoutId() {
			return R.layout.dialog_guaranree_canceled;
		}

	}

	 class GuaranteeManageListAdater extends ObjectAdapter implements View.OnClickListener{

		public GuaranteeManageListAdater(Context _context) {
			super(_context);
		}

		@Override
		public int getLayoutId() {
			return R.layout.item_guarantee;
		}

//		@Override
//		public int getCount() {
//			return 5;
//		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if(convertView==null){
				convertView=inflateConvertView();
				convertView.setId(R.id.convertView);
				convertView.setOnClickListener(this);

				holder=new ViewHolder();
				holder.txtUserInfo= (TextView) convertView.findViewById(R.id.txt_userName);
				holder.txtGuaranteeDesc= (TextView) convertView.findViewById(R.id.txt_guaranteeDesc);
				holder.txtStatus= (TextView) convertView.findViewById(R.id.txt_guaranteeStatus);
				holder.txtCreateTime= (TextView) convertView.findViewById(R.id.txt_createTime);
				holder.bgTop=convertView.findViewById(R.id.bg_top);
				holder.lineBot=convertView.findViewById(R.id.line_bot);

				convertView.setTag(holder);
			}else {
				holder= (ViewHolder) convertView.getTag();
			}

			if(position==0){
				holder.bgTop.setVisibility(View.VISIBLE);
			}else {
				holder.bgTop.setVisibility(View.GONE);
			}

			if(position!=getCount()-1){
				holder.lineBot.setVisibility(View.VISIBLE);
			}else {
				holder.lineBot.setVisibility(View.GONE);
			}

			fillData((GuaranteeInfo) getItem(position), holder);

			return convertView;
		}

		private void fillData(GuaranteeInfo guaranteeInfo,ViewHolder holder){
			holder.guaranteeInfo=guaranteeInfo;
			Spanned _userInfo= Html.fromHtml("<font color=\"#333333\">" +guaranteeInfo.getOrderInfo().getUserName() +
					            "&nbsp;</font><font color=\"#666666\">&nbsp;(&nbsp;"+guaranteeInfo.getOrderInfo().getUserPhone()+"&nbsp;)</font>");
			holder.txtUserInfo.setText(_userInfo);
			holder.txtCreateTime.setText(TimeUtil.formatTimeYMDHMS(guaranteeInfo.getCreateTime()));

			switch (guaranteeInfo.getOrderStatus()){

				case GuaranteeInfo.STATUS_WAIT_FOR_CONFIRN:
					holder.txtStatus.setVisibility(View.VISIBLE);
					holder.txtGuaranteeDesc.setText("申购\"" + guaranteeInfo.getProduct().getName() + "\"");
					break;

				case GuaranteeInfo.STATUS_CANCELED:
					holder.txtStatus.setVisibility(View.GONE);
					holder.txtGuaranteeDesc.setText(guaranteeInfo.getProduct().getName());
					break;

				case GuaranteeInfo.STATUS_REFUSED:
					holder.txtStatus.setVisibility(View.GONE);
					holder.txtGuaranteeDesc.setText(guaranteeInfo.getProduct().getName());
					break;

				case GuaranteeInfo.STATUS_WAIT_FOR_PAY:
					holder.txtStatus.setVisibility(View.GONE);
					holder.txtGuaranteeDesc.setText(guaranteeInfo.getProduct().getName());
					break;

				case GuaranteeInfo.STATUS_IN_REVIEW:
					holder.txtStatus.setVisibility(View.GONE);
					holder.txtGuaranteeDesc.setText(guaranteeInfo.getProduct().getName());
					break;

				case GuaranteeInfo.STATUS_IN_GUARANTEE:
					holder.txtStatus.setVisibility(View.GONE);
					holder.txtGuaranteeDesc.setText(guaranteeInfo.getProduct().getName());
					break;

				case GuaranteeInfo.STATUS_REFUNDED:
					holder.txtStatus.setVisibility(View.GONE);
					holder.txtGuaranteeDesc.setText("\""+guaranteeInfo.getProduct().getName()+"\"已退单");
					break;

				case GuaranteeInfo.STATUS_PAY_TIMEOUT:
					holder.txtStatus.setVisibility(View.GONE);
					holder.txtGuaranteeDesc.setText("\""+guaranteeInfo.getProduct().getName()+"\"支付超时");
					break;

				case GuaranteeInfo.STATUS_OVERDUE:
					holder.txtStatus.setVisibility(View.GONE);
					holder.txtGuaranteeDesc.setText("\""+guaranteeInfo.getProduct().getName()+"\"已过期");
					break;

				default:
					break;

			}


		}

		@Override
		public void onClick(View v) {
			ViewHolder holder= (ViewHolder) v.getTag();

			GuaranteeInfo guaranteeInfo=holder.guaranteeInfo;

			switch (guaranteeInfo.getOrderStatus()){

				case GuaranteeInfo.STATUS_REFUSED:
					guaranteeWarningDialog.show("此单已被您拒绝！",null);
					break;

				case GuaranteeInfo.STATUS_CANCELED:
					guaranteeWarningDialog.show("抱歉,用户已取消了购买申请。",null);
					break;

				default:
					GuaranteeDetailAct.start(guaranteeInfo.getId() + "");
					break;

			}


		}


		 class ViewHolder{

			View bgTop,lineBot;

			TextView txtUserInfo;

			TextView txtStatus;

			TextView txtGuaranteeDesc;

			TextView txtCreateTime;

			GuaranteeInfo guaranteeInfo;

		}



	}





}

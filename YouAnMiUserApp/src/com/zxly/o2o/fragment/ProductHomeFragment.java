package com.zxly.o2o.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.adapter.ProductAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.dialog.SelectChatDialog;
import com.zxly.o2o.model.Filters;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.ProductBrand;
import com.zxly.o2o.model.ProductType;
import com.zxly.o2o.model.SortItem;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.AllProductListInitRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ProductListFilterRequest;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.PullView.OnSelChangeListener;
import com.zxly.o2o.view.SpinnerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-23
 * @since YIBA-O2O
 */
public class ProductHomeFragment extends BaseFragment implements
		OnRefreshListener, OnSelChangeListener {

	private SpinnerView turnType;
	private SpinnerView turnBrand;
	private SpinnerView turnSort;
	private PullToRefreshListView mListView;
	private LoadingView loadingview;
	private TextView txtTitle;
	private View btnChartMassge;
	private ProductAdapter adapter;
	private int index;
	private boolean isLastData, isCondition;

	@Override
	protected void initView() {
		FrameLayout spinnerContent = (FrameLayout) findViewById(R.id.spinner_content);
		turnType = (SpinnerView) findViewById(R.id.turn_type);
		turnBrand = (SpinnerView) findViewById(R.id.turn_brand);
		turnSort = (SpinnerView) findViewById(R.id.turn_sort);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		btnChartMassge=findViewById(R.id.btn_chart_massge);
		loadingview = (LoadingView) findViewById(R.id.view_loading);
		mListView = (PullToRefreshListView) findViewById(R.id.goods_listview);
		adapter = new ProductAdapter(this.getActivity());
		ViewUtils.setRefreshText(mListView);
		mListView.setAdapter(adapter);
		mListView.setDivideHeight(0);
		turnType.setContent(spinnerContent);
		turnBrand.setContent(spinnerContent);
		turnSort.setContent(spinnerContent);
		turnType.setOnSelChangeListener(this);
		turnBrand.setOnSelChangeListener(this);
		turnSort.setOnSelChangeListener(this);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				final NewProduct np = (NewProduct) adapter.getItem(arg2 - 1);
				ProductInfoAct.start(ProductHomeFragment.this.getActivity(),
						np, new ParameCallBack() {

							@Override
							public void onCall(Object object) {
								adapter.getContent().remove(np);
								adapter.notifyDataSetChanged();
							}
						});

			}

		});
		ViewUtils.setVisible(btnChartMassge);
		ViewUtils.setText(txtTitle, "商品");
		turnType.setDefValue(new ProductType(-1, "全部类型"));
		turnBrand.setDefValue(new ProductBrand(-1, "全部品牌"));
		turnSort.setDefValue(new SortItem("默认排序"));
		btnChartMassge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Account.user!=null) {
					new SelectChatDialog().show();
				}else{
					LoginAct.start(getActivity());
				}
			}
		});
		final AllProductListInitRequest productListInitRequest = new AllProductListInitRequest(
				Config.shopId);
		productListInitRequest
				.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
					@Override
					public void onOK() {
						ViewUtils.setVisible(mListView);
						turnType.setData(productListInitRequest
								.getProductTypeList());
						turnBrand.setData(productListInitRequest
								.getProductBrandList());
						turnSort.setData(productListInitRequest
								.getSortItemList());
						if (!productListInitRequest.getProductList().isEmpty()) {
							adapter.addItem(productListInitRequest
									.getProductList());
							adapter.notifyDataSetChanged();
							loadingview.onLoadingComplete();
						} else {
							loadingview.onDataEmpty();
						}

					}

					@Override
					public void onFail(int code) {
						ViewUtils.setGone(mListView);
						loadingview.onLoadingFail();
					}
				});
		loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

			@Override
			public void onLoading() {
				loadingview.startLoading();
				productListInitRequest.start(ProductHomeFragment.this
						.getActivity());
			}
		});
		loadingview.startLoading();
		productListInitRequest.start(ProductHomeFragment.this.getActivity());
	}

	@Override
	protected int layoutId() {
		return R.layout.win_all_product;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void loadData(final int page) {

		List<Filters> filterList = new ArrayList<Filters>();
		ProductType proType = (ProductType) turnType.getSelItem();
		ProductBrand productBrand = (ProductBrand) turnBrand.getSelItem();
		SortItem sortItem = (SortItem) turnSort.getSelItem();

		if (proType.getId() != -1) {
			filterList.add(new Filters(1, proType.getId()));
		}
		if (productBrand.getId() != -1) {
			filterList.add(new Filters(3, productBrand.getId()));
		}
		filterList.add(new Filters(2, Config.shopId));
		Paging paging = new Paging(page, sortItem.getCode());
		final ProductListFilterRequest request = new ProductListFilterRequest(
				paging, filterList);
		request.setTag(this);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
				boolean isEmpty = request.getProductList().isEmpty();
				mListView.onRefreshComplete();
				if (page == 1) {
					if (isEmpty) {
						loadingview.onDataEmpty();
					} else {
						loadingview.onLoadingComplete();
					}
					if (isCondition)// 是否有条件
					{
						isCondition = false;
						adapter.addItem(request.getProductList());
						adapter.notifyDataSetChanged();
						return;
					} else {
						adapter.clear();
					}

				}
				if (page > 1) {
					if (isEmpty) {
						isLastData = true;
						ViewUtils.showToast("亲! 没有更多了");
						return;
					}

				}
				adapter.addItem(request.getProductList());
				adapter.notifyDataSetChanged();

			}

			@Override
			public void onFail(int code) {
				mListView.onRefreshComplete();
			}
		});
		request.start(this);
	}

	@Override
	public void onRefresh(PullToRefreshBase refreshView) {

		if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
			index = 1;

			loadData(index);

		} else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
			// 加载上拉数据
			if (!isLastData) {
				index++;
				loadData(index);
			} else {
				mMainHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						mListView.onRefreshComplete();
					}
				}, 1000);
			}

		}

	}

	@Override
	public void onSelChange() {
		index = 1;
		adapter.clear();
		isCondition = true;
		isLastData=false;
		loadData(index);
	}

}

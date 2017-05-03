package com.zxly.o2o.fragment;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.ProductManageAct;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;


public class ProductPagerFragment extends BaseViewPageFragment implements View.OnClickListener {

	private View btnManage;
	private int index=0;
	@Override
	protected void initView() {
		super.initView();
		btnManage=findViewById(R.id.btn_manage);
		if(Account.user.getRoleType()==1)
		{
			btnManage.setOnClickListener(this);
			ViewUtils.setVisible(btnManage);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		pager.setCurrentItem(index);
	}

	public void open(Activity activity, int index)
	{
		this.index = index;
		//((MainActivity) activity).fragmentContorler.setCurrenuTab(1);
	}

	@Override
	protected String[] tabName() {
		return new String[]{"上架中的商品","未上架的商品"};
	}

	@Override
	protected int layoutId() {
		return R.layout.win_product;
	}

	@Override
	protected List<Fragment> fragments() {
		List<Fragment> list=new ArrayList<Fragment>();
		list.add( ProductGeneralizeFragment.getInstance(1));
		list.add( ProductGeneralizeFragment.getInstance(0));
		return list;
	}



	@Override
	public void onClick(View v) {
		if(btnManage==v)
		{
			ProductManageAct.start(this.getActivity());
		}
	}
}

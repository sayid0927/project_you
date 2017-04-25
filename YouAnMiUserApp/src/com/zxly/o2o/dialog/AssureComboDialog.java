package com.zxly.o2o.dialog;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.AssureChargeAdapter;
import com.zxly.o2o.model.AssureScope;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-3-16
 * @description 担保套餐收费对话框
 */
public class AssureComboDialog extends BaseDialog {
	private ListView mListView;
	private AssureChargeAdapter chargeAdapter;
	private TextView comboName;
	private ParameCallBack callBack;

	public AssureComboDialog() {
		super();
		chargeAdapter = new AssureChargeAdapter(this.context, new CallBack() {

			@Override
			public void onCall() {
				dismiss();
				AssureComboDialog.this.callBack.onCall(chargeAdapter.getAssureCharge());
			}
		});
		mListView.setAdapter(chargeAdapter);
	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_assure_combo;
	}

	@Override
	protected void initView() {
		mListView = (ListView) findViewById(R.id.assure_charge_list);
		mListView.setLayoutParams(new LinearLayout.LayoutParams(-1, DesityUtil.getScreenSizes(getContext())[1]/2));
		comboName = (TextView) findViewById(R.id.assure_combo_name);
	}

	public void show(ParameCallBack callBack, AssureScope scope) {
		super.show();
		this.callBack = callBack;
		ViewUtils.setText(comboName, scope.getName());
		chargeAdapter.clear();
		chargeAdapter.addItem(scope.getCharges(), true);
	}

	@Override
	protected boolean isLimitHeight() {
		return true;
	}
	
}

/*
 * 文件名：ProductAppearanceDialog.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ProductAppearanceDialog.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-19
 * 修改内容：新增
 */
package com.zxly.o2o.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.model.UsedDesc;
import com.zxly.o2o.model.UsedDescParam;
import com.zxly.o2o.model.UsedDescType;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.view.CheckLayout;

import java.util.ArrayList;
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
 * @version    YIBA-O2O 2015-3-19
 * @since      YIBA-O2O
 */
public class ProductDescriptionDialog extends BaseDialog implements OnClickListener{
	private ParameCallBackById callBack;
	private LinearLayout contentView;
	List<UsedDescType> descTypes;
	
	private List<UsedDesc> useddescList= new ArrayList<UsedDesc>();
	public ProductDescriptionDialog() {
		super();
	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_pruduct_description;
	}

	@Override
	protected void initView() {
		contentView=(LinearLayout) findViewById(R.id.layout_container);

	}



	@Override
	protected boolean isLimitHeight() {
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			
			break;
		}

	}

	public View getSingleCheckItem(int checkType,String typeName,List items){
		
		View v=LayoutInflater.from(getContext()).inflate(R.layout.dialog_product_appearance_item,null);
		TextView tv=(TextView) v.findViewById(R.id.single_item_name);
		tv.setText(typeName);
		CheckLayout singleCheckLayout=(CheckLayout) v.findViewById(R.id.single_ckeck_layout);
		singleCheckLayout.initView(checkType, items);
		return v;
		
	}
	
	public void show(ParameCallBackById callBack, List<UsedDescType> descTypes) {
		if(DataUtil.listIsNull(descTypes))
			return;
		this.callBack = callBack;
		this.descTypes=descTypes;
	    for (int i = 0; i < descTypes.size(); i++) {
			contentView.addView(getSingleCheckItem(descTypes.get(i).getType(),descTypes.get(i).getName(), descTypes.get(i).getParams()));
		}
		super.show();
//		labelAdapter=new ProductAppearanceAdaper(getContext());
//		appearanceListView.setAdapter(labelAdapter);
//        labelAdapter.addItem(deprs, true);
	}
	
	
	@Override
	protected void doOnDismiss() {

		getCheckItems();
		callBack.onCall(getLayoutId(),useddescList);
		AppLog.d("onCallDissMiss", "-->call" + useddescList.size());
	}
	
	public void getCheckItems(){
		if(DataUtil.listIsNull(descTypes))
			return;
		for (int i = 0; i <descTypes.size(); i++) {
			UsedDescType ck=descTypes.get(i);
			List<UsedDescParam> params=ck.getParams();
			
			UsedDesc useddesc=new UsedDesc();
			useddesc.setTypeName(ck.getName());
			useddesc.setTypeId(ck.getId());	
			AppLog.d("ckxx", "-->" + ck.getType());
			if(ck.getType()==CheckLayout.SINGLE_CHECK_TYPE)
			for (int j = 0; j <params.size(); j++) {
				if(params.get(j).isCheck()){
					UsedDescParam param=params.get(j);
					useddesc.setTypeName(ck.getName());
					useddesc.setTypeId(ck.getId());
					useddesc.setDescName(param.getName());
					useddesc.setDescId(param.getId()+"");
					break;
				}
			}
			
			if(ck.getType()==CheckLayout.MULTIPLE_CHECK_TYPE){
				for (int j = 0; j <params.size(); j++) {
					if(params.get(j).isCheck()){
						UsedDescParam param=params.get(j);
						if(DataUtil.stringIsNull(useddesc.getDescName())){
							useddesc.setDescName(param.getName());
						}else{
							useddesc.setDescName(useddesc.getDescName()+","+param.getName());
						}
						
						if(DataUtil.stringIsNull(useddesc.getDescId())){
							useddesc.setDescId(param.getId()+"");
						}else{
							useddesc.setDescId(useddesc.getDescId()+","+param.getId());
						}
					}
				}	
			}
		
			if(!DataUtil.stringIsNull(useddesc.getDescId()))
			useddescList.add(useddesc);
		}
		

		
		for (int i = 0; i < useddescList.size(); i++) {
			AppLog.d("useddescList", "size--->" + useddescList.get(i).toString());
		}
	}
	
}

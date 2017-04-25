/*
 * 文件名：SingleCheckLayout.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SingleCheckLayout.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-18
 * 修改内容：新增
 */
package com.zxly.o2o.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.model.CheckItem;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.DesityUtil;

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
 * @version    YIBA-O2O 2015-3-18
 * @since      YIBA-O2O
 */
public class CheckLayout extends LinearLayout implements OnGlobalLayoutListener{
	
	public static final int SINGLE_CHECK_TYPE=1;
	public static final int MULTIPLE_CHECK_TYPE=2;
	
	//单选背景样式：
	public static final int SINGLE_CHECK_LEFT_BG=3;
	public static final int SINGLE_CHECK_MID_BG=4;
	public static final int SINGLE_CHECK_RIGHT_BG=5;
	public static final int SINGLE_CHECK_SINGLE_BG=6;
	
	//多选背景样式：
	public static final int MULTIPLE_CHECK_BG=7;
	List<CheckItem> checkList;
    List<TextView> singleCheckItems=new ArrayList<TextView>();
    List<View> multipleCheckItems=new ArrayList<View>();
    
    public static final float SINGLE_CHECK_ITEM_WITH=72.00f;
    public static final  float SINGLE_CHECK_ITEM_HEIGHT=35.00f;
    
    public static final  float MULTIPLE_CHECK_ITEM_WITH=96.00f;
    public static final  float MULTIPLE_CHECK_ITEM_HEIGHT=35.00f;
	private int lineCount,countPerLine,curentType,layoutWith;
	public CheckLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void initView(int chooseType,List<CheckItem> checkList){
	    curentType=chooseType;
		if(checkList==null||checkList.size()<=0)
			return;
		setOrientation(LinearLayout.VERTICAL);		
		this.checkList=checkList;
		if(chooseType==SINGLE_CHECK_TYPE){
			initSingleChoose();
		}else if(chooseType==MULTIPLE_CHECK_TYPE){
			initMultipleChoose();
		}
		Log.d("mean", "onconstor ->  with="+getWidth() +"  height="+getHeight());
		
//		Log.d("addView", "-->"+DesityUtil.getDrawableSize(getContext().getResources().getDrawable(R.drawable.old), getContext()));
	}
	
	public void initSingleChoose(){	
		lineCount=getRowCount();
		Log.d("countPerLine", "rows=="+lineCount);
		int start=0;  //计算当前在哪一个
		int currentCount=0; //计算这一行共有几个，换行的时候清0
		for (int count = 0; count < lineCount; count++) {
			
			LinearLayout row1=getRow();
			addView(row1);
			
			Log.d("countPerLine", "addLine=="+count);
			//执行换行计数操作
			for (int i = start; i < checkList.size(); i++) {				
				if(currentCount==countPerLine){
					currentCount=0;
					break;
				}
				currentCount++;
				start++;
				TextView checkItem=new TextView(getContext());
				checkItem.setGravity(Gravity.CENTER);
				checkItem.setText(checkList.get(i).getName());
				checkItem.setLayoutParams(new LinearLayout.LayoutParams(DesityUtil.dp2px(getContext(), SINGLE_CHECK_ITEM_WITH),
						                  DesityUtil.dp2px(getContext(), SINGLE_CHECK_ITEM_HEIGHT)));
				singleCheckItems.add(checkItem);
				row1.addView(checkItem);
				setSingleCheckBg(i, checkItem);
				initSingleCheckItemListener(i, checkItem);
			}
		}
		
	}
	
	
	
	public void initMultipleChoose(){	
		lineCount=getRowCount();
		Log.d("countPerLine", "rows=="+lineCount);
		int start=0;
		int currentCount=0;
		for (int count = 0; count < lineCount; count++) {
			LinearLayout row1=getRow();
			addView(row1);
			Log.d("countPerLine", "addLine=="+count);
			for (int i = start; i < checkList.size(); i++) {				
				if(currentCount==countPerLine){
					currentCount=0;
					break;
				}
				currentCount++;
				start++;
				View checkItem=getMultipleCheckItem(checkList.get(i).getName());
				multipleCheckItems.add(checkItem);
				row1.addView(checkItem);
				setItemBg(i, checkItem, MULTIPLE_CHECK_BG);
				initMultipleCheckItemListener(i, checkItem);
			}
		}
		
	}
	

	


	public View getMultipleCheckItem(String name){
		LinearLayout lv=new LinearLayout(getContext());
		lv.setGravity(Gravity.CENTER_VERTICAL);
		lv.setLayoutParams(new LinearLayout.LayoutParams(DesityUtil.dp2px(getContext(), MULTIPLE_CHECK_ITEM_WITH), 
				           DesityUtil.dp2px(getContext(), MULTIPLE_CHECK_ITEM_HEIGHT)));
		lv.setOrientation(LinearLayout.HORIZONTAL);	
		
		LayoutParams ivParams=new LinearLayout.LayoutParams(-2,-2);
		ivParams.setMargins(0, 0,DesityUtil.dp2px(getContext(), 5),0);
		ImageView iv=new ImageView(getContext());
		iv.setLayoutParams(ivParams);
		iv.setBackgroundResource(R.drawable.mutiple_choose);
		iv.setId(1000);
		lv.addView(iv);
		
		TextView itemName=new TextView(getContext());
		itemName.setTextAppearance(getContext(), R.style.text_14_light_grey);
		itemName.setText(name);		
		itemName.setGravity(Gravity.CENTER);
		itemName.setSingleLine(true);
		itemName.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
		itemName.setPadding(0, 0,DesityUtil.dp2px(getContext(), 10),0);
		lv.addView(itemName);
		
		
		return lv;
		
	}
	
	
	public int getRowCount(){		
		int count=checkList.size();
		int rowCount=1;
		int[] sizes=DesityUtil.getScreenSizes(getContext());

		layoutWith=sizes[0]-DesityUtil.dp2px(getContext(),70);
		
		int singleWith=0;
		if(curentType==SINGLE_CHECK_TYPE){
			singleWith=DesityUtil.dp2px(getContext(),SINGLE_CHECK_ITEM_WITH);					
		}else if(curentType==MULTIPLE_CHECK_TYPE){
			singleWith=DesityUtil.dp2px(getContext(),MULTIPLE_CHECK_ITEM_WITH);			
		}
		
		countPerLine=layoutWith/singleWith;	
		Log.d("countPerLine", "--->"+countPerLine);
		if(count<=countPerLine){
			return rowCount;
		}
		rowCount=count/countPerLine;
		if(count%countPerLine>0){
			rowCount=rowCount+1;
		}				
		return rowCount;
	}
	
	public void setSingleCheckBg(int position,TextView checkItem){
		int count=checkList.size();
		if(position==0){
			if(count==1){
				setItemBg(position, checkItem, SINGLE_CHECK_SINGLE_BG);
			}else{
				setItemBg(position, checkItem, SINGLE_CHECK_LEFT_BG);
			}				 
		}else if(count!=1&&position>0&&position<count-1){
           
			if(lineCount>1){
				
				for (int i = 1; i <= lineCount; i++) {
					 //判断是否换行后的第一个
					if(position==(countPerLine*(i-1))){
						setItemBg(position, checkItem, SINGLE_CHECK_LEFT_BG);
						return;
					}
					 //判断是否换行后的最右边的
					if(position==(countPerLine*i-1)){
						setItemBg(position, checkItem, SINGLE_CHECK_RIGHT_BG);
						return;
					}
				}				
			}
			//都不是的话就是中间背景
			setItemBg(position, checkItem, SINGLE_CHECK_MID_BG);
		}else if(count!=1&&position==count-1){
			if(count%countPerLine==1){
				setItemBg(position, checkItem, SINGLE_CHECK_SINGLE_BG);
			}else{
				setItemBg(position, checkItem, SINGLE_CHECK_RIGHT_BG);	
			}
			
		}
	}
	
	
	private void setItemBg(int position,View checkItem,int bgType){
		CheckItem item=checkList.get(position);		
		switch (bgType) {
		case SINGLE_CHECK_LEFT_BG:
			if(!item.isCheck()){
				((TextView)checkItem).setTextAppearance(getContext(),R.style.text_14_light_grey);
				checkItem.setBackgroundResource(R.drawable.check_left);
			}else{
				((TextView)checkItem).setTextAppearance(getContext(),R.style.text_14_white);
				checkItem.setBackgroundResource(R.drawable.check_left_press);
			}			
			break;
		case SINGLE_CHECK_MID_BG:
			if(!item.isCheck()){
				((TextView)checkItem).setTextAppearance(getContext(),R.style.text_14_light_grey);
				checkItem.setBackgroundResource(R.drawable.check_middle);
			}else{
				((TextView)checkItem).setTextAppearance(getContext(),R.style.text_14_white);
				checkItem.setBackgroundResource(R.drawable.check_middle_press);
			}				
			break;
		case SINGLE_CHECK_RIGHT_BG:
			if(!item.isCheck()){
				((TextView)checkItem).setTextAppearance(getContext(),R.style.text_14_light_grey);
				checkItem.setBackgroundResource(R.drawable.check_right);
			}else{
				((TextView)checkItem).setTextAppearance(getContext(),R.style.text_14_white);
				checkItem.setBackgroundResource(R.drawable.check_right_press);
			}				
			break;
		case SINGLE_CHECK_SINGLE_BG:
			if(!item.isCheck()){
				checkItem.setBackgroundResource(R.drawable.check_single);
			}else{
				checkItem.setBackgroundResource(R.drawable.check_single_press);
			}				
			break;
			
			//多选背景
		case MULTIPLE_CHECK_BG:
			if(!item.isCheck()){
				checkItem.findViewById(1000).setBackgroundResource(R.drawable.mutiple_choose);	
			}else{
				checkItem.findViewById(1000).setBackgroundResource(R.drawable.mutiple_choose_press);
			}							
			break;
			
		default:
			break;
			
		}
		

	}
	

	
	public void initSingleCheckItemListener(final int position,final TextView checkItem){
		
		checkItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {			
				CheckItem itemInfo=checkList.get(position);
				if(itemInfo.isCheck()){
					Log.d("check", "取消选择 自己 ："+checkItem.getText().toString());
					itemInfo.setCheck(false);
				}else{
					itemInfo.setCheck(true);	
					Log.d("check", "选择  ："+checkItem.getText().toString());
				}
				setSingleCheckBg(position, checkItem);
				

				for (int i = 0; i < checkList.size(); i++) {
					if(i!=position&&checkList.get(i).isCheck()){
						Log.d("check", "取消选择  ："+singleCheckItems.get(i).getText().toString()  +"  i=="+i +"  pos=="+position);
						checkList.get(i).setCheck(false);
						setSingleCheckBg(i, singleCheckItems.get(i));
						return;
					}
				}				
			}
			
		});
		
		
	}
	
	
	private void initMultipleCheckItemListener(final int position, final View checkItem) {
		checkItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CheckItem itemInfo=checkList.get(position);
				if(itemInfo.isCheck()){
					itemInfo.setCheck(false);
				}else{
					itemInfo.setCheck(true);
				}
				setItemBg(position, checkItem, MULTIPLE_CHECK_BG);				
			}
		});		
	}
	
	
	public LinearLayout getRow(){
		LinearLayout row=new LinearLayout(getContext());
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1, -2);
		params.setMargins(0, 5, 0, 5);
		row.setLayoutParams(params);
	//	row.setGravity(Gravity.CENTER);	
		row.setOrientation(LinearLayout.HORIZONTAL);
		return row;
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		Log.d("mean", "onmean ->  with="+widthMeasureSpec +"  height="+heightMeasureSpec);
		Log.d("mean", "onmean ->  with="+getWidth() +"  height="+getHeight());
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("mean", "ondraw ->  with="+getWidth() +"  height="+getHeight());
		super.onDraw(canvas);
	}

	@Override
	public void onGlobalLayout() {
//		int  = getWidth();
//		int tmpH = getHeight();
//		if (width != tmpW || height != tmpH) {
//			width = tmpW;
//			height = tmpH;
//			show();
//		}
//		
	}
	
	
	

    
}

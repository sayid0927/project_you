/*
 * 文件名：ProductTypeDialog.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ProductTypeDialog.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-19
 * 修改内容：新增
 */
package com.zxly.o2o.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.PhoneUtil;
import com.zxly.o2o.util.ViewUtils;

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
public class PhoneDialog extends BaseDialog implements OnClickListener{

    PhoneNunberAdapter phoneAdapter;

    private ListView listPhone;


    @Override
    public int getLayoutId() {
        return R.layout.dialog_phonenumber;
    }

    @Override
    protected void initView() {
        listPhone= (ListView) findViewById(R.id.listview_phone_number);
        phoneAdapter=new PhoneNunberAdapter(getContext());
        listPhone.setAdapter(phoneAdapter);
    }




    public void show(List<String> phones) {
        phoneAdapter.clear();
        phoneAdapter.addItem(phones, true);
        super.show();
    }


    @Override
    protected boolean isLimitHeight() {
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_call :

                break;

            default :
                break;
        }

    }



    static class PhoneNunberAdapter extends ObjectAdapter implements  OnClickListener{


        private  Context context;

        public PhoneNunberAdapter(Context _context) {
            super(_context);
            this.context=_context;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_phonenumber;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            String phoneNumber= (String) getItem(position);
            ViewHolder holder=null;
            if(convertView==null){
                convertView=inflateConvertView();
                holder=new ViewHolder();
                holder.txtPhoneNumber= (TextView) convertView.findViewById(R.id.txt_phoneNumber);
                holder.botLine=convertView.findViewById(R.id.line);
                convertView.setOnClickListener(this);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }

            holder.txtPhoneNumber.setText(phoneNumber);
            holder.phoneNumber=phoneNumber;

            if(position==getCount()-1){
                ViewUtils.setGone(holder.botLine);
            }else{
                ViewUtils.setVisible(holder.botLine);
            }

            return convertView;
        }

        @Override
        public void onClick(View v) {
            ViewHolder holder= (ViewHolder) v.getTag();
            PhoneUtil.openPhoneKeyBord(holder.phoneNumber,context);
        }

        static class ViewHolder{
            TextView txtPhoneNumber;
            View botLine;
            String phoneNumber;
        }
    }


}

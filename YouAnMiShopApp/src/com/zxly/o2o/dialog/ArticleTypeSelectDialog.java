package com.zxly.o2o.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.ArticleType;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;


public class ArticleTypeSelectDialog extends BaseDialog implements View.OnClickListener{


    public static final String TYPE_CANCLE_DIALOG=-1000+"";

    List<ArticleType> articleTypes;

    ParameCallBack callBack;

    TypeAdapter adapter;

    GridView gridView;

    private ArticleType curSelectType;

    public void setArticleTypes(List<ArticleType> articleTypes) {
        this.articleTypes = articleTypes;
    }

    public void setCallBack(ParameCallBack callBack) {
        this.callBack = callBack;
    }

    public void setCurSelectType(ArticleType curSelectType) {
        this.curSelectType = curSelectType;
    }

    @Override
    protected void initView() {
        gridView= (GridView) findViewById(R.id.gridView);
        adapter=new TypeAdapter(context);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void doOnDismiss() {
        if(callBack!=null)
            callBack.onCall(TYPE_CANCLE_DIALOG);
    }

    @Override
    public void show() {
        super.show();
        if(!DataUtil.listIsNull(articleTypes)){
            adapter.clear();
            adapter.addItem(articleTypes,true);
            super.show();
        }else {
            ViewUtils.showToast("暂时没有更多分类!");
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_choose_article_type;
    }

    @Override
    public void onClick(View v) {

    }



    private class TypeAdapter extends ObjectAdapter implements View.OnClickListener{

        public TypeAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_share;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null) {
                convertView=inflateConvertView();
                holder=new ViewHolder();
                holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
                holder.txtName.setGravity(Gravity.CENTER);
                holder.txtName.setBackgroundResource(R.drawable.bg_choose_article_normal);
                LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(DesityUtil.dp2px(context,100),DesityUtil.dp2px(context,30));
                holder.txtName.setLayoutParams(params);
                holder.txtName.setOnClickListener(this);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }

            ArticleType articleType= (ArticleType) getItem(position);

            if (articleType.getId().equals(curSelectType.getId())){
                holder.txtName.setBackgroundResource(R.drawable.bg_choose_article_press1);
                holder.txtName.setTextColor(0xffffffff);
            }else {
                holder.txtName.setBackgroundResource(R.drawable.bg_choose_article_normal);
                holder.txtName.setTextColor(0xff051b28);
            }

            //holder.txtName.setText(StringUtil.trimFromStr(articleType.getCodeName(),5));
            holder.txtName.setText(articleType.getCodeName());
            holder.txtName.setTag(articleType);
            return convertView;
        }

        @Override
        public void onClick(View v) {
           ArticleType articleType= (ArticleType) v.getTag();
            if(callBack!=null){
                callBack.onCall(articleType);
            }

            curSelectType=articleType;
            dialog.dismiss();
        }


        private class ViewHolder{
            TextView txtName;
        }

    }

}

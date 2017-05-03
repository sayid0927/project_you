package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.activity.StrategyDetailAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.MakeMoneyArticle;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2015/12/15.
 */
public class MakeMoneyAdapter extends ObjectAdapter implements View.OnClickListener {
    public MakeMoneyAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_zqgl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.txtTitle= (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtIncomeTitle=(TextView)convertView.findViewById(R.id.txt_income_title);
            holder.txtContent=(TextView)convertView.findViewById(R.id.txt_content);
            convertView.setOnClickListener(this);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        MakeMoneyArticle article= (MakeMoneyArticle) getItem(position);
        ViewUtils.setText(holder.txtTitle,article.getTitle());
        ViewUtils.setText(holder.txtIncomeTitle," "+article.getIncomeTitle());
        ViewUtils.setText(holder.txtContent,article.getContent());
        holder.makeMoneyArticle= (MakeMoneyArticle) getItem(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder= (ViewHolder) v.getTag();
        StrategyDetailAct.start(AppController.getInstance().getTopAct(),"赚钱攻略",holder.makeMoneyArticle);
        UmengUtil.onEvent(context,new UmengUtil().MONEY_GUIDE_CLICK,null);
    }


    class ViewHolder{
        TextView txtTitle,txtIncomeTitle,txtContent;
        MakeMoneyArticle makeMoneyArticle;
    }
}

package com.zxly.o2o.adapter;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dsnx on 2015/7/9.
 */
public class CustomSetComissionProductAdapter extends ObjectAdapter {
    private Set<Long> errorList=new HashSet<Long>();

    public CustomSetComissionProductAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_custom_set_comission_product;
    }


    public Set<Long> getErrorList() {
        return errorList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null)
        {
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.itemIcon= (NetworkImageView) convertView.findViewById(R.id.img_item_icon);
            holder.imgPlotFlag= (ImageView) convertView.findViewById(R.id.img_plot_flag);
            holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtPrice= (TextView) convertView.findViewById(R.id.txt_price);
            holder.txtOldPrice= (TextView) convertView.findViewById(R.id.txt_old_price);
            holder.txtScale= (TextView) convertView.findViewById(R.id.txt_scale);
            holder.editComisson= (EditText) convertView.findViewById(R.id.edit_comisson);
            holder.lineBottom=convertView.findViewById(R.id.line_bottom);
            convertView.setTag(holder);
        }else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        final Product product= (Product) getItem(position);

        holder.itemIcon.setDefaultImageResId(R.drawable.product_def);
        holder.itemIcon.setImageUrl(product.getHeadUrl(),
                AppController.imageLoader);
        float curPrice = product.getPrice()-product.getPreference();
        ViewUtils.setText(holder.txtName, product.getName());
        if(product.getPreference()>0)
        {
            ViewUtils.strikeThruText(holder.txtOldPrice,  product.getPrice());
        }
        holder.txtScale.setText(product.getRate()+"%");
        if(product.getComission()>0)
        {
            holder.editComisson.setText(product.getComission()+"");
        }

        holder.editComisson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0)
                {
                    try {
                        float comission=Float.valueOf(s.toString());
                        DecimalFormat df = new DecimalFormat("#0.000");
                        float scale=Float.valueOf( df.format(comission/product.getPrice()*100));
                        product.setComission(comission);
                        product.setRate(scale);
                        holder.txtScale.setText(scale + "%");
                        if(comission>product.getPrice()||comission<=0)
                        {
                            holder.editComisson.setBackgroundResource(R.drawable.red_edit_gb);
                            errorList.add(product.getId());
                        }else
                        {
                            holder.editComisson.setBackgroundResource(R.drawable.edit_gb);
                            errorList.remove(product.getId());
                        }
                    }catch (NumberFormatException e)
                    {

                    }



                }else
                {
                    product.setRate(0f);
                    holder.txtScale.setText("");
                    product.setComission(0f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ViewUtils.setTextPrice(holder.txtPrice, curPrice);
        if (position == getCount() - 1) {
            ViewUtils.setVisible(holder.lineBottom);

        } else {
            ViewUtils.setInvisible(holder.lineBottom);
        }

        return convertView;
    }
    class ViewHolder{
        NetworkImageView itemIcon;
        ImageView imgPlotFlag;
        EditText editComisson;
        TextView txtName,txtPrice,txtOldPrice,txtScale;
        View lineBottom;
    }
}

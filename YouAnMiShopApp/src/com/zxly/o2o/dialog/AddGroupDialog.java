package com.zxly.o2o.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxly.o2o.model.MenberGroupModel;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.view.MListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/8/31.
 * 用户备注页面 选择分组弹框
 */
public class AddGroupDialog extends BaseDialog{
    //默认选中的选项
    private final List<MenberGroupModel> chooseGroup;
    private CallBack callBack;
    private List<String> list;
    private MListView listView;
    private  List<MenberGroupModel> chooseGroups=new ArrayList<MenberGroupModel>();
    private List<MenberGroupModel> diffGroup;

    public AddGroupDialog(List<MenberGroupModel> diffGroup, List<MenberGroupModel> chooseGroup) {
        this.diffGroup=diffGroup;
        this.chooseGroup=chooseGroup;
        chooseGroups.addAll(chooseGroup);
    }

    @Override
    protected void initView() {
        diffGroup=new ArrayList<MenberGroupModel>();
        ScrollView scroll_view= (ScrollView) findViewById(R.id.scrollview);
//        scroll_view.setVerticalScrollBarEnabled(false);
        listView = (MListView) findViewById(R.id.listview);
        listView.setAdapter(new MyAdapter());
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 获取用户选中的组名
                callBack.onCall();
                dismiss();
            }
        });
        findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(CallBack callBack) {
        super.show();
        this.callBack=callBack;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_add_group;
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return diffGroup.size();
        }

        @Override
        public Object getItem(int position) {
            return diffGroup.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.item_add_group,parent,false);
                viewHolder.container= (RelativeLayout) convertView.findViewById(R.id.container);
                viewHolder.tv_groupname= (TextView) convertView.findViewById(R.id.tv_groupname);
                viewHolder.group_count= (TextView) convertView.findViewById(R.id.group_count);
                viewHolder.img_check= (ImageView) convertView.findViewById(R.id.img_check);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            if(chooseGroups.contains(diffGroup.get(position))){
                viewHolder.img_check.setImageResource(R.drawable.icon_check_press);
            }else{
                viewHolder.img_check.setImageResource(R.drawable.icon_check_normal);
            }
            viewHolder.tv_groupname.setText(diffGroup.get(position).getName());
            viewHolder.group_count.setText("("+diffGroup.get(position).getMemberCount()+")");
            viewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(chooseGroups.contains(diffGroup.get(position))){
                        viewHolder.img_check.setImageResource(R.drawable.icon_check_normal);
                        chooseGroups.remove(diffGroup.get(position));
                    }else{
                        viewHolder.img_check.setImageResource(R.drawable.icon_check_press);
                        chooseGroups.add(diffGroup.get(position));
                    }
                }
            });
            return convertView;
        }

        class ViewHolder{
            RelativeLayout container;
            TextView tv_groupname;
            TextView group_count;
            ImageView img_check;
        }
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void setHeightAndWidth() {
        WindowManager.LayoutParams lp;
        lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
    }

    public List<MenberGroupModel> getChooseGroup(){
        return chooseGroups;
    }

    @Override
    protected boolean isShowAnimation() {
        return false;
    }
}

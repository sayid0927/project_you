package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.MyPromotionMember;
import com.zxly.o2o.model.User;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PromotionDetailRequest;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.SideBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-12-7
 * @description 推广人详细信息界面
 */
public class PromotionDetailAct extends BasicAct implements
        OnClickListener {
    private ListView listView;
    private Context context;
    private MemberAdapter adapter;
    private User user;
    private TextView txtUserName, txtPromotionCode;
    private CircleImageView imgUserHead;
    private ImageView imgGender;
    private ArrayList<MyPromotionMember> myPromotionMembersList = new ArrayList<MyPromotionMember>();
    private SideBar sideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_promotion_detail);
        context = PromotionDetailAct.this;
        initViews();
        getPromotionDetail();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, PromotionDetailAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        imgUserHead = (CircleImageView) findViewById(R.id.img_user_head);
        imgGender = (ImageView) findViewById(R.id.img_gender);
        txtUserName = (TextView) findViewById(R.id.txt_user_name);
        txtPromotionCode = (TextView) findViewById(R.id.txt_promotion_code);

        listView = (ListView) findViewById(R.id.list_my_members);
        sideBar = (SideBar) findViewById(R.id.side_bar);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });
        adapter = new MemberAdapter(context, myPromotionMembersList);
        listView.setAdapter(adapter);
    }

    private void getPromotionDetail() {
        final PromotionDetailRequest promotionDetailRequest = new PromotionDetailRequest(Account.user.getId());
        promotionDetailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                user = promotionDetailRequest.getUser();
                if (user != null) {
                    ViewUtils.setText(txtUserName, user.getNickName() + " " + user.getUserName());
                    if (user.getGender() == 1) {
                        imgGender.setBackgroundResource(R.drawable.icon_man);
                    } else if (user.getGender() == 2) {
                        imgGender.setBackgroundResource(R.drawable.icon_woman);
                    }
                    if (user.getThumHeadUrl() == null) {
                        imgUserHead.setImageResource(R.drawable.personal_default_head);
                    } else {
                        imgUserHead.setImageUrl(user.getThumHeadUrl(), R.drawable.personal_default_head);
                    }
                    ViewUtils.setText(txtPromotionCode, "推广码：" + user.getPromotionCode());
                }
                myPromotionMembersList = promotionDetailRequest.getMembersList();
                if (myPromotionMembersList != null && !myPromotionMembersList.isEmpty()) {
                    ViewUtils.setVisible(findViewById(R.id.txt_my_members));
                    ViewUtils.setVisible(sideBar);
                    adapter.updateListView(myPromotionMembersList);
                } else {
                    ViewUtils.setGone(findViewById(R.id.txt_my_members));
                    ViewUtils.setGone(sideBar);
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        promotionDetailRequest.start(this);
    }

    class MemberAdapter extends BaseAdapter implements SectionIndexer {
        private List<MyPromotionMember> promotionMemberList = null;
        private Context mContext;

        public MemberAdapter(Context mContext, List<MyPromotionMember> promotionMemberList) {
            this.mContext = mContext;
            this.promotionMemberList = promotionMemberList;
        }

        public void updateListView(List<MyPromotionMember> promotionMemberList) {
            this.promotionMemberList = promotionMemberList;
            notifyDataSetChanged();
        }

        public int getCount() {
            return this.promotionMemberList.size();
        }

        public Object getItem(int position) {
            return promotionMemberList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup arg2) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_my_promotion_member, null);
                viewHolder.txtLetter = (TextView) view
                        .findViewById(R.id.txt_letter);
                viewHolder.viewLine = view.findViewById(R.id.view_line);
                viewHolder.imgMemberHead = (NetworkImageView) view
                        .findViewById(R.id.img_member_head);
                viewHolder.txtMemberName = (TextView) view
                        .findViewById(R.id.txt_member_name);
                viewHolder.txtMemberPhone = (TextView) view.findViewById(R.id.txt_member_phone);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            final MyPromotionMember myPromotionMember = promotionMemberList.get(position);
            int section = getSectionForPosition(position);

            if (position == getPositionForSection(section)) {
                viewHolder.txtLetter.setVisibility(View.VISIBLE);
                viewHolder.viewLine.setVisibility(View.VISIBLE);
                viewHolder.txtLetter.setText(myPromotionMember.getLetter());
            } else {
                viewHolder.txtLetter.setVisibility(View.GONE);
                viewHolder.viewLine.setVisibility(View.GONE);
            }

            String memberName = myPromotionMember.getName();
            if (memberName != null) {
                ViewUtils.setText(viewHolder.txtMemberName, memberName);
            } else {
                ViewUtils.setText(viewHolder.txtMemberName, "");
            }

            viewHolder.imgMemberHead.setDefaultImageResId(R.drawable.personal_default_head);
            viewHolder.imgMemberHead.setImageUrl(myPromotionMember.getThumHeadUrl(),
                    AppController.imageLoader);

            ViewUtils.setText(viewHolder.txtMemberPhone, myPromotionMember.getUserName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EaseConstant.startIMUserDetailInfo(myPromotionMember.getUserId(), false,
                            PromotionDetailAct.this, "个人信息", 1, null);
                }
            });
            return view;
        }

        class ViewHolder {
            TextView txtLetter;
            View viewLine;
            NetworkImageView imgMemberHead;
            TextView txtMemberName;
            TextView txtMemberPhone;
        }

        public int getSectionForPosition(int position) {
            return promotionMemberList.get(position).getLetter().charAt(0);
        }

        public int getPositionForSection(int section) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = promotionMemberList.get(i).getLetter();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public Object[] getSections() {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

}

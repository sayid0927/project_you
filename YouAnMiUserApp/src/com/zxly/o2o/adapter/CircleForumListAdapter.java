package com.zxly.o2o.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.widget.SwipeLayout;
import com.zxly.o2o.activity.ForumAllListAct;
import com.zxly.o2o.activity.ForumGuideAct;
import com.zxly.o2o.fragment.CircleMainPageFragment;
import com.zxly.o2o.model.CircleForumVO;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ForumToConcernRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/15.
 */
public class CircleForumListAdapter extends ObjectAdapter {
    protected Set<SwipeLayout> mShownLayouts = new HashSet<SwipeLayout>();
    private boolean isGuideActivity;
    private boolean operate1, operate2;
    private int recommendPosition = -1;
    private CallBack callBack;

    public CircleForumListAdapter(Context _context, int recommendPosition,CallBack callBack) {
        super(_context);
        this.recommendPosition = recommendPosition;
        this.callBack=callBack;
        if (_context instanceof ForumGuideAct) {
            isGuideActivity = true;
        }
    }

    public void setrecommendPosition(int rec) {
        recommendPosition = rec;
    }

    @Override
    public int getLayoutId() {
        return R.layout.circle_row_forum;
    }

    private static class ViewHolder {
        NetworkImageView avatar;
        TextView title;
        TextView attentionCount;
        ImageView rightBtn;
        TextView subHead;
        TextView headerView;
        TextView swView2;
        TextView swView1;
        SwipeLayout sLayout;
    }

    //        @Override
    //        public int getCount() {
    //            return super.getCount()==0?5:super.getCount();
    //        }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflateConvertView();

            holder.sLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
            holder.sLayout.addSwipeListener(new SwipeMemory());
            mShownLayouts.add(holder.sLayout);

            holder.avatar = (NetworkImageView) convertView.findViewById(R.id.avatar);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.headerView =
                    (TextView) convertView.findViewById(R.id.header);
            holder.subHead = (TextView) convertView.findViewById(R.id.sub_head);
            holder.swView1 = (TextView) convertView.findViewById(R.id.sw_btn1);
            holder.swView2 = (TextView) convertView.findViewById(R.id.sw_btn2);
            holder.rightBtn = (ImageView) convertView.findViewById(R.id.right_btn);
            holder.attentionCount =
                    (TextView) convertView.findViewById(R.id.title_right);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CircleForumVO forumVO = (CircleForumVO) getItem(position);

        if (position == 0 && forumVO.getName().contains(Constants.FORUM_CIRCLE_TITLE)) {
            ImageUtil.setImage(holder.avatar, "", R.drawable.logo, null);
        } else {
            ImageUtil.setImage(holder.avatar, forumVO.getImageUrl(), R.drawable.ease_default_avatar, null);
        }
        holder.subHead.setText(forumVO.getContent());
        holder.title.setText(forumVO.getName());
        holder.attentionCount.setText("关注度:" + forumVO.getConcernAmount());

        String header = forumVO.getInitialLetter();
        if (isGuideActivity) {
            holder.sLayout.setSwipeEnabled(false);
            holder.rightBtn.setImageResource(forumVO.isCheck() ? R.drawable.ease_check_press : R.drawable
                    .ease_check_normal);
        } else {
            holder.sLayout.setSwipeEnabled(true);

            if ((position >= recommendPosition && recommendPosition != -1) ||
                    context instanceof ForumAllListAct || (position == 0 && header.contains("推荐"))) {
                holder.sLayout.setSwipeEnabled(false);
                holder.rightBtn.setVisibility(View.VISIBLE);
                holder.rightBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!operate1) {
                            operate1 = true;
                            concernRequest(forumVO, position, 1);
                        }
                    }
                });
            } else {
                holder.sLayout.setSwipeEnabled(position != 0);
                holder.rightBtn.setVisibility(View.GONE);
            }
        }


        if (TextUtils.isEmpty(header)) {
            holder.headerView.setVisibility(View.GONE);
        } else {
            holder.headerView.setVisibility(View.VISIBLE);
            holder.headerView.setText(header);
            if (header.contains("我的")) {
                holder.headerView
                        .setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconfont_qz_wodequanzi, 0, 0, 0);
            } else {
                holder.headerView
                        .setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconfont_qz_tuijian, 0, 0, 0);
            }
        }

        if (forumVO.getIsTop() > 0) {
            convertView.setBackgroundResource(R.color.grey_f5f3f3);
            setSwView(holder.swView1, "取消置顶", R.drawable.ease_icon_x);
        } else {
            convertView.setBackgroundResource(R.color.white);
            setSwView(holder.swView1, "置顶", R.drawable.icon_up);
        }

        holder.swView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!operate2) {
                    operate2 = true;
                    concernRequest(forumVO, position, 2);
                }
            }
        });
        holder.swView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                callBack.onCall();
                if (forumVO.getIsTop() == 1) {
                    CircleForumVO circleForumVO;
                    for (int i = 1; i < getCount(); i++) {
                        circleForumVO = (CircleForumVO) getItem(i);
                        if (circleForumVO.getIsTop() != 1) {
                            if (forumVO.getConcernTime() > circleForumVO.getConcernTime()) {
                                addItem(forumVO, false, i);
                                break;
                            } else if (i == getCount() - 1) {
                                addItem(forumVO, false);
                                break;
                            }

                        }
                    }
                    forumVO.setIsTop(0);
                    deleteItem(position);
                } else {
                    forumVO.setIsTop(1);
                    addItem(forumVO, false, 1);
                    deleteItem(position + 1);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }


    private void concernRequest(final CircleForumVO vo, final int position, int action) {
        ForumToConcernRequest f = new ForumToConcernRequest(vo.getId(), action);
        f.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (operate1) {
                    operate1 = false;
                    ViewUtils.showToast("成功关注“"+vo.getName()+"”!");
                } else if (operate2) {
                    operate2 = false;
                    ViewUtils.showToast("取消关注成功");
                }
                CircleMainPageFragment.isReLoad = true;
                if(callBack!=null)
                callBack.onCall();
            }

            @Override
            public void onFail(int code) {
                operate1 = false;
            }
        });
        f.start();
    }

    private void setSwView(TextView swView, String text, int pic) {
        swView.setText(text);
        swView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, pic, 0, 0);
    }

    class SwipeMemory implements SwipeLayout.SwipeListener {

        @Override
        public void onStartOpen(SwipeLayout layout) {
            closeAllExcept(layout);

        }

        @Override
        public void onOpen(SwipeLayout layout) {
            closeAllExcept(layout);
        }

        @Override
        public void onStartClose(SwipeLayout layout) {

        }

        @Override
        public void onClose(SwipeLayout layout) {

        }

        @Override
        public void onUpdate(SwipeLayout layout, int leftOffset,
                             int topOffset) {

        }

        @Override
        public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

        }
    }

    public void closeAllExcept(SwipeLayout layout) {
        for (SwipeLayout s : mShownLayouts) {
            if (s != layout) {
                s.close();
            }
        }
    }

}

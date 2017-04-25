package com.easemob.easeui.adapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.HXModel;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.utils.PreferenceManager;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.model.IMUserInfoVO;
import com.easemob.easeui.request.EaseDeleteFriendRequest;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.utils.GsonParser;
import com.easemob.easeui.widget.SwipeLayout;
import com.easemob.util.EMLog;
import com.google.gson.reflect.TypeToken;

public class EaseContactAdapter extends ArrayAdapter<EaseYAMUser> implements SectionIndexer {
    private static final String TAG = "ContactAdapter";
    List<String> list;
    public static List<EaseYAMUser> unRegistList = new ArrayList<EaseYAMUser>();
    public static HashMap<Long, Double> commissionMap = new HashMap<Long, Double>();
    private LayoutInflater layoutInflater;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private int res;
    private MyFilter myFilter;
    protected Set<SwipeLayout> mShownLayouts = new HashSet<SwipeLayout>();
    private boolean isShowCheck;


    public EaseContactAdapter(Context context, int resource, List<EaseYAMUser> objects) {
        super(context, resource, objects);
        this.res = resource;
        layoutInflater = LayoutInflater.from(context);
        unRegistList = new HXModel(context).getUnRegistList();


    }

    private class ViewHolder {
        NetworkImageView avatar;
        TextView nameView;
        TextView nameRight;
        TextView nameLeft;
        TextView signature;
        TextView headerView;
        ImageView checkIcon;
        TextView swView;
        SwipeLayout sLayout;
    }

    public void isShowCheckIcon(boolean mIsShowCheck) {
        isShowCheck = mIsShowCheck;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (res == 0) {
                convertView = layoutInflater.inflate(R.layout.ease_row_contact, null);
            } else {
                convertView = layoutInflater.inflate(res, null);
            }

            holder.avatar = (NetworkImageView) convertView.findViewById(R.id.avatar);
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.nameRight = (TextView) convertView.findViewById(R.id.name_right);
            holder.nameLeft = (TextView) convertView.findViewById(R.id.name_left);
            holder.signature = (TextView) convertView.findViewById(R.id.signature);
            holder.headerView = (TextView) convertView.findViewById(R.id.header);
            holder.swView = (TextView) convertView.findViewById(R.id.sw_btn);
            holder.sLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
            if (EaseConstant.shopID < 0) {

                //设置CheckIcon
                if (isShowCheck) {
                    holder.checkIcon = (ImageView) convertView.findViewById(R.id.check_icon);
                    holder.checkIcon.setVisibility(View.VISIBLE);

                } else {  //在选择联系人界面不需要侧滑
                    holder.sLayout.addSwipeListener(new SwipeMemory());
                    mShownLayouts.add(holder.sLayout);
                }
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final EaseYAMUser user;


        if (position >= super.getCount()) { /*未注册*/
            if (isShowCheck) {
                holder.checkIcon.setImageResource(R.drawable.ease_check_normal);
            }
            holder.nameLeft.setText("未注册");
            user = unRegistList.get(position - super.getCount());
            holder.nameView.setText(user.getFirendsUserInfo().getImei());
            holder.signature.setText("");
            holder.sLayout.setSwipeEnabled(false);
            convertView.findViewById(R.id.contact_yj_layout).setVisibility(View.GONE);
            EaseConstant.setImage(holder.avatar, user.getFirendsUserInfo().getThumHeadUrl(), R.drawable
                    .ease_default_avatar, null);
        } else {/*已经注册*/
            user = getItem(position);
            String header = user.getFirendsUserInfo().getInitialLetter();

            if (position == 0 || header != null && user.getFirendsUserInfo().getIsTop() != 1 &&
                    !header.equals(getItem(position - 1).getFirendsUserInfo().getInitialLetter())) {
                if (TextUtils.isEmpty(header)) {
                    holder.headerView.setVisibility(View.GONE);
                } else {
                    holder.headerView.setVisibility(View.VISIBLE);
                    if(EaseConstant.shopID>0&&user.getFirendsUserInfo().getIsTop()==1){
                        header="业务员";
                    }
                    holder.headerView.setText(header);
                }
            } else {
                holder.headerView.setVisibility(View.GONE);
            }

            //设置头像和昵称
            holder.nameView.setText(user.getFirendsUserInfo().getNickname());
            holder.nameLeft.setText(user.getFirendsUserInfo().getGroupName());
            EaseConstant.setImage(holder.avatar, user.getFirendsUserInfo().getThumHeadUrl(), R.drawable
                    .ease_default_avatar, null);

            //设置签名
            if (EaseConstant.shopID > 0) {
                convertView.findViewById(R.id.contact_yj_layout).setVisibility(View.GONE);
                holder.signature.setText(user.getFirendsUserInfo().getSignature());
            } else if (EaseConstant.shopID < 0) {
                convertView.findViewById(R.id.contact_yj_layout).setVisibility(View.VISIBLE);
                holder.signature.setText(user.getFirendsUserInfo().getUserName());
            }


            if (primaryColor != 0) {
                holder.nameView.setTextColor(primaryColor);
            }
            if (primarySize != 0) {
                holder.nameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
            }
//            if (initialLetterBg != null) {
//                holder.headerView.setBackgroundDrawable(initialLetterBg);
//            }
//            if (initialLetterColor != 0) {
//                holder.headerView.setTextColor(initialLetterColor);
//            }


            //设置佣金
            if (EaseConstant.shopID < 0) {  //商户端
                if (user.getFirendsUserInfo().getId() != 0) {
                    long id = user.getFirendsUserInfo().getId();
                    double commission;
                    if(commissionMap.containsKey(id)) {
                        commission = commissionMap.get(id);
                        ((TextView) convertView.findViewById(R.id.yj_sum)).setText("￥" + commission);

                    }else{
                        ((TextView) convertView.findViewById(R.id.yj_sum)).setText("￥" + 0.0);
                    }
                }

                //设置CheckIcon
                if (isShowCheck) {
                    holder.sLayout.setSwipeEnabled(false);
                    holder.checkIcon.setImageResource(
                            user.isCheck() ? R.drawable.ease_check_press : R.drawable
                                    .ease_check_normal);
                } else {
                    holder.sLayout.setSwipeEnabled(true);
                    //设置侧滑栏
                    if (header.contains("★")) {
                        setSwView(holder.swView, "取消置顶", "#eeeeee", "#ff5f19", R.drawable.ease_qxzd_btn);
                    } else {
                        setSwView(holder.swView, "置顶", "#ff5f19", "#eeeeee", R.drawable.ease_zd_btn);
                    }

                    holder.swView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (holder.swView.getText().equals("取消置顶")) {
                                user.getFirendsUserInfo().setIsTop(0);
                                user.getFirendsUserInfo().setInitialLetter("");
                                user.getFirendsUserInfo().setInitialLetter(
                                        EaseCommonUtils.setUserInitialLetter(user.getFirendsUserInfo())
                                                .getInitialLetter());
                            } else {
                                user.getFirendsUserInfo().setIsTop(1);
                                user.getFirendsUserInfo().setInitialLetter("★置顶标记");
                            }
                            sortList();
                        }
                    });
                }

            } else if (EaseConstant.shopID > 0) { //用户端

                //设置是不是黑名单
                holder.nameRight.setText(user.getIsBlack() == 2 ? "已加入黑名单" : "");

                /*置顶的业务员不能进行侧滑操作*/
                if (user.getFirendsUserInfo().getIsTop() == 1) {
                    holder.sLayout.setSwipeEnabled(false);
                } else {
                    holder.sLayout.setSwipeEnabled(true);
                }
                setSwView(holder.swView, "删除", "#eeeeee", "#ff5f19", R.drawable.ease_icon_x);

                /*删除好友并刷新adapter*/
                holder.swView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.swView.setClickable(false);
                        EaseDeleteFriendRequest easeDeleteFriendRequest = new EaseDeleteFriendRequest(user
                                .getFirendsUserInfo().getId());
                        easeDeleteFriendRequest.setOnResponseStateListener(
                                new HXNormalRequest.ResponseStateListener() {
                                    @Override
                                    public void onOK() {
                                        clear();
                                        addAll(HXHelper.yamContactList);
                                        notifyDataSetChanged();
                                        EMChatManager.getInstance().deleteConversation(
                                                HXApplication.getInstance().parseUserFromID(user
                                                        .getFirendsUserInfo().getId(), HXConstant.TAG_USER),
                                                false, true);
                                        holder.swView.setClickable(true);
                                    }

                                    @Override
                                    public void onFail(int code) {
                                        holder.swView.setClickable(true);
                                    }
                                });
                        easeDeleteFriendRequest.start();
                    }
                });
            }
        }

        if (isShowCheck) {
            if (user.isCheck()) {
                holder.checkIcon.setImageResource(
                        user.isCheck() ? R.drawable.ease_check_press : R.drawable
                                .ease_check_normal);
            }
        }


        return convertView;
    }

    private void setSwView(TextView swView, String text, String textColor, String bgColor, int pic) {
        swView.setText(text);
        swView.setTextColor(Color.parseColor(textColor));
        swView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, pic, 0, 0);
        swView.setBackgroundColor(Color.parseColor(bgColor));
    }

    public void sortList() {
        EaseContactAdapter.this.sort(new Comparator<EaseYAMUser>() {

            @Override
            public int compare(EaseYAMUser lhs, EaseYAMUser rhs) {
                if (1 == lhs.getFirendsUserInfo().getIsTop()) {
                    if (1 == rhs.getFirendsUserInfo().getIsTop()) {
                        if (EaseConstant.shopID > 0) {  //兼容用户app  用户app的业务员列表没有nickname
                            return lhs.getFirendsUserInfo().getInitialLetter()
                                    .compareTo(rhs.getFirendsUserInfo().getInitialLetter());
                        } else {
                            return lhs.getFirendsUserInfo().getNickname()
                                    .compareTo(rhs.getFirendsUserInfo().getNickname());
                        }
                    } else {
                        return -1;
                    }
                } else if (1 == rhs.getFirendsUserInfo().getIsTop()) {
                    return 1;
                } else {
                    if (lhs.getFirendsUserInfo().getInitialLetter()
                            .equals(rhs.getFirendsUserInfo().getInitialLetter())) {
                        return lhs.getFirendsUserInfo().getNickname()
                                .compareTo(rhs.getFirendsUserInfo().getNickname());
                    } else {
                        if ("#".equals(lhs.getFirendsUserInfo().getInitialLetter())) {
                            return 1;
                        } else if ("#".equals(rhs.getFirendsUserInfo().getInitialLetter())) {
                            return -1;
                        }
                        return lhs.getFirendsUserInfo().getInitialLetter()
                                .compareTo(rhs.getFirendsUserInfo().getInitialLetter());
                    }
                }

            }
        });
    }

    @Override
    public EaseYAMUser getItem(int position) {
        if (position >= super.getCount()) {
            return unRegistList.get(position - super.getCount());
        } else {
            return super.getItem(position);
        }
    }

    @Override
    public int getCount() {
        if (EaseConstant.isRegistMembers && EaseConstant.shopID > 0) {
            return super.getCount();
        } else {
            return super.getCount() + unRegistList.size();
        }
    }

    @Override
    public int getPositionForSection(int section) {
        return positionOfSection.get(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        list = new ArrayList<String>();
        list.add(getContext().getString(R.string.search_header));
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 1; i < count; i++) {

            String letter = getItem(i).getFirendsUserInfo().getInitialLetter();
            EMLog.d(TAG, "contactadapter getsection getHeader:" + letter + " name:" +
                    getItem(i).getFirendsUserInfo().getUsername());
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter(HXHelper.yamContactList);
        }
        return myFilter;
    }

    protected class MyFilter extends Filter {
        List<EaseYAMUser> mOriginalList = null;

        public MyFilter(List<EaseYAMUser> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mOriginalList == null) {
                mOriginalList = new ArrayList<EaseYAMUser>();
            }
            EMLog.d(TAG, "contacts original size: " + mOriginalList.size());
            EMLog.d(TAG, "contacts copy size: " + HXHelper.yamContactList.size());

            if (prefix == null || prefix.length() == 0) {
                results.values = HXHelper.yamContactList;
                results.count = HXHelper.yamContactList.size();
            } else {
                String prefixString = prefix.toString();
                final int count = HXHelper.yamContactList.size();
                final ArrayList<EaseYAMUser> newValues = new ArrayList<EaseYAMUser>();
                for (int i = 0; i < count; i++) {
                    final EaseYAMUser user = HXHelper.yamContactList.get(i);
                    String username = user.getFirendsUserInfo().getUserName();
                    String nickname = user.getFirendsUserInfo().getNickname();
                    if (username == null) {
                        username = "";
                    }
                    if (nickname == null) {
                        nickname = "";
                    }

                    if (username.contains(prefixString) || nickname.contains
                            (prefixString)) {
                        newValues.add(user);
                    }
                    //                    else {
                    //                        final String[] words = username.split(" ");
                    //
                    //                        // Start at index 0, in case valueText starts with space(s)
                    //                        for (String word : words) {
                    //                            if (word.startsWith(prefixString)) {
                    //                                newValues.add(user);
                    //                                break;
                    //                            }
                    //                        }
                    //                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            EMLog.d(TAG, "contacts filter results size: " + results.count);
            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                                                   FilterResults results) {
            clear();
            addAll((List<EaseYAMUser>) results.values);
            EMLog.d(TAG, "publish contacts filter results size: " + results.count);
            if (results.count > 0) {
                if (!"".equals(constraint)) {
                    unRegistList.clear();
                }
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }


    @Override
    public void notifyDataSetChanged() {
        synchronized (this) {
            if (EaseConstant.ADD_FRIEND_SUSSECC == 1) {
                EaseConstant.ADD_FRIEND_SUSSECC = 0;
                clear();
//                                    addAll(HXHelper.yamContactList);
            }
        }
        super.notifyDataSetChanged();

    }

    protected int primaryColor;
    protected int primarySize;
    protected Drawable initialLetterBg;
    protected int initialLetterColor;

    public EaseContactAdapter setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
        return this;
    }


    public EaseContactAdapter setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
        return this;
    }

    public EaseContactAdapter setInitialLetterBg(Drawable initialLetterBg) {
        this.initialLetterBg = initialLetterBg;
        return this;
    }

    public EaseContactAdapter setInitialLetterColor(int initialLetterColor) {
        this.initialLetterColor = initialLetterColor;
        return this;
    }

    public void closeAllExcept(SwipeLayout layout) {
        for (SwipeLayout s : mShownLayouts) {
            if (s != layout) {
                s.close();
            }
        }
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
        public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

        }

        @Override
        public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

        }

    }

}

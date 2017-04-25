package com.easemob.easeui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.chatuidemo.utils.PreferenceManager;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.ui.EaseSelectMembersActivity;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshBase;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshListView;

public class EaseContactList extends RelativeLayout implements EasePullToRefreshBase.OnRefreshListener {
    protected static final String TAG = EaseContactList.class.getSimpleName();

    protected Context context;
    protected EasePullToRefreshListView listView;
    protected EaseContactAdapter adapter;
    protected EaseSidebar sidebar;

    protected int primaryColor;
    protected int primarySize;
    protected boolean showSiderBar;
    protected Drawable initialLetterBg;
    protected EaseMyFlipperView viewContainer;

    private EaseCallBack easeCallBack;
    private TextView membersCount;

    public void setViewContainer(EaseMyFlipperView viewContainer) {
        this.viewContainer = viewContainer;
    }


    public void setCallBack(EaseCallBack callBack) {
        easeCallBack = callBack;
    }


    static final int MSG_UPDATE_LIST = 0;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_LIST:

                    Boolean isNeedFilter = (Boolean) msg.obj;
                    if (adapter != null) {
                        if (HXHelper.yamContactList.size() == 0) {
                            if (HXHelper.getInstance().getYAMContactList() == null || HXHelper.getInstance()
                                    .getYAMContactList().size() == 0) {
                                adapter.clear();
                            } else {
                                adapter.clear();
                                adapter.addAll(HXHelper.yamContactList);
                            }
                        } else if (adapter.getCount() == 0) {
                            adapter.addAll(HXHelper.yamContactList);
                        } else {
                            if (isNeedFilter != null && isNeedFilter) {
                                List<EaseYAMUser> copyUserList = new ArrayList<EaseYAMUser>();
                                for (EaseYAMUser user : HXHelper.yamContactList) {
                                    if (user.isCheck()) {
                                        copyUserList.add(user);
                                    }
                                }
                                adapter.clear();
                                EaseConstant.users.clear();
                                adapter.addAll(copyUserList);
                            } else {
                                adapter.clear();
                                adapter.addAll(HXHelper.yamContactList);
                            }

                        }

                        adapter.notifyDataSetChanged();
                        //商户端统计人数
                        if (membersCount != null && getEaseContactAdapter() != null) {
                            membersCount.setText(
                                    String.valueOf(getEaseContactAdapter().getCount()));
                        }
                        if (viewContainer != null) {
                            if (getEaseContactAdapter().getCount() == 0) {
                                viewContainer.setDisplayedChild(EaseMyFlipperView.NODATA);
                            } else {
                                viewContainer.setDisplayedChild(EaseMyFlipperView.LOADSUCCESSFUL, false);
                            }
                        }

                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    protected int initialLetterColor;


    public EaseContactList(Context context) {
        super(context);
        init(context, null);
    }

    public EaseContactList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EaseContactList(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseContactList);
        primaryColor = ta.getColor(R.styleable.EaseContactList_ctsListPrimaryTextColor, 0);
        primarySize = ta.getDimensionPixelSize(R.styleable.EaseContactList_ctsListPrimaryTextSize, 0);
        showSiderBar = ta.getBoolean(R.styleable.EaseContactList_ctsListShowSiderBar, true);
        initialLetterBg = ta.getDrawable(R.styleable.EaseContactList_ctsListInitialLetterBg);
        initialLetterColor = ta.getColor(R.styleable.EaseContactList_ctsListInitialLetterColor, 0);
        ta.recycle();


        LayoutInflater.from(context).inflate(R.layout.ease_widget_contact_list, this);
        listView = (EasePullToRefreshListView) findViewById(R.id.list);
        setListView();
        sidebar = (EaseSidebar) findViewById(R.id.sidebar);
        if (!showSiderBar) {
            sidebar.setVisibility(View.GONE);
        }
    }

    /*
     * init view
     */
    public void init(List<EaseYAMUser> contactList) {
        List<EaseYAMUser> copyUserList = new ArrayList<EaseYAMUser>();
        copyUserList.addAll(contactList);

        adapter = new EaseContactAdapter(context, 0, copyUserList);
        adapter.setPrimaryColor(primaryColor).setPrimarySize(primarySize).setInitialLetterBg(initialLetterBg)
                .setInitialLetterColor(initialLetterColor);
        listView.setAdapter(adapter);
        if (showSiderBar) {
            sidebar.setListView(listView);
        }
    }


    public void setIsShowCheckIcon(boolean isShowCheckIcon) {
        adapter.isShowCheckIcon(isShowCheckIcon);
    }


    public void refresh(TextView tvMembersCount) {
        membersCount = tvMembersCount;
        listView.onRefreshComplete();
        Message msg = handler.obtainMessage(MSG_UPDATE_LIST);
        handler.sendMessage(msg);
    }

    public void refresh(boolean isNeedFilter) {
        handler.obtainMessage(MSG_UPDATE_LIST, isNeedFilter).sendToTarget();
    }

    public void filter(CharSequence str) {
        adapter.getFilter().filter(str);
    }

    public EasePullToRefreshListView getListView() {
        return listView;
    }

    public EaseContactAdapter getEaseContactAdapter() {
        return adapter;
    }

    public void setShowSiderBar(boolean showSiderBar) {
        if (showSiderBar) {
            sidebar.setVisibility(View.VISIBLE);
        } else {
            sidebar.setVisibility(View.GONE);
        }
    }

    protected void setListView() {
        EaseConstant.setRefreshText(listView);
        listView.setOnRefreshListener(this);
        listView.setIntercept(true);
    }

    @Override
    public void onRefresh(EasePullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == EasePullToRefreshBase.Mode.PULL_FROM_START) { // 加载下拉数据
            if (System.currentTimeMillis() - PreferenceManager.getInstance().getMistiming() >
                    1000 * 60 * 5) {
                PreferenceManager.getInstance().setMistiming(System.currentTimeMillis());
                if (easeCallBack != null) {
                    easeCallBack.onCall();
                }
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                }, 500);

            }
        }
    }

}

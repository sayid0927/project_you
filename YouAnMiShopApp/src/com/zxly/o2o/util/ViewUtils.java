package com.zxly.o2o.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.easeui.widget.viewpagerindicator.PagerSlidingTabStrip;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.pullrefresh.PullToRefreshAdapterViewBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.shop.R;

import java.util.ArrayList;
import java.util.List;

public class ViewUtils {


    public static void showToast(String msg) {
        if (StringUtil.isNull(msg)) {
            return;
        }
        Context context = AppController.getInstance();
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView toastText = (TextView) toastView.findViewById(R.id.toast_text);
        toastText.setText(msg);
        Toast toast = new Toast(context);
        int cha = DesityUtil.dp2px(context, 32);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, cha);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastView);
        toast.show();
    }

    public static void showLongToast(String msg) {
        if (StringUtil.isNull(msg))
            return;
        Context context = AppController.getInstance();
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView toastText = (TextView) toastView.findViewById(R.id.toast_text);
        toastText.setText(msg);
        Toast toast = new Toast(context);
        int cha = DesityUtil.dp2px(context, 32);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, cha);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.show();
    }

    /**
     * 横穿文字画线
     *
     * @param tv
     * @param str
     */
    public static void strikeThruText(TextView tv, String str) {
        if (tv != null && !StringUtil.isNull(str)) {
            tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setText(str);
        }

    }

    public static void startActivity(Intent intent, Activity curAct) {
        if (intent != null) {
            curAct.startActivity(intent);
            curAct.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        }
    }

    public static void startActivityForResult(Intent intent, Activity curAct,int requestCode) {
        if (intent != null) {
            curAct.startActivityForResult(intent, requestCode);
            curAct.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        }
    }

    public static void setImmerseLayout(Window window,Context context, View titleView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = ViewUtils.getStatusBarHeight(context);
            titleView.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    public static void setImmerseLayout(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setRefreshText(PullToRefreshListView mListView) {
        setRefreshListText(mListView);
    }
    public static void setRefreshListText(PullToRefreshBase mListView) {
        mListView.setMode(Mode.BOTH);
        mListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        mListView.getLoadingLayoutProxy(false, true).setReleaseLabel("上拉加载");
        mListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        mListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新");
        mListView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
    }

    public static void setRefreshListFromStartText(PullToRefreshBase mListView) {
        mListView.setMode(Mode.PULL_FROM_START);
        mListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        mListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新");
        mListView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
    }

    public static void setRefreshBaseText(PullToRefreshAdapterViewBase mPullToRefreshView) {
        mPullToRefreshView.setMode(Mode.BOTH);
        mPullToRefreshView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshView.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        mPullToRefreshView.getLoadingLayoutProxy(false, true).setReleaseLabel("上拉加载");
        mPullToRefreshView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        mPullToRefreshView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新");
        mPullToRefreshView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
    }


    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    public static void setTabsValue(PagerSlidingTabStrip tabs) {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线颜色
        tabs.setDividerColor(Color.parseColor("#d9d9d9"));
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, AppController.displayMetrics));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, AppController.displayMetrics));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, AppController.displayMetrics));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#dd2727"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#dd2727"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(R.drawable.background_tab);
        tabs.initTabStyles();
    }

    static public void setText(View v, Object o) {
        if (v == null || o == null) {
            return;
        }
        if (!(v instanceof TextView)) {
            return;
        }
        TextView t = (TextView) v;
        if (t == null || o == null) {
            return;
        }
        if (o instanceof String) {
            t.setText((String) o);
        } else {
            t.setText(o.toString());
        }

    }

    public static void setTextPrice(TextView tv, float price) {
        if (tv == null) {
            return;
        }
        tv.setText("￥" + StringUtil.getFormatPrice(price));
    }

    /**
     * 横穿文字画线
     *
     * @param tv
     * @param price
     */
    public static void strikeThruText(TextView tv, float price) {
        if (tv != null) {
            tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setText("￥" + StringUtil.getFormatPrice(price));
        }


    }

    public static String getText(View parent, int viewId) {
        if (parent == null) {
            return "";
        }
        View v = parent.findViewById(viewId);
        if (v == null) {
            return "";
        }
        if (!(v instanceof TextView)) {
            return "";
        }
        return ((TextView) v).getText().toString().trim();
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

    static public void setGone(View v) {
        if (v == null) {
            return;
        }
        v.setVisibility(View.GONE);
    }

    static public void setInvisible(View v) {
        if (v == null) {
            return;
        }
        v.setVisibility(View.INVISIBLE);
    }

    static public void setGone(View v, int resId) {
        View view = v.findViewById(resId);
        if (view == null) {
            return;
        }
        view.setVisibility(View.GONE);
    }

    static public boolean isVisible(View v) {
        return v.getVisibility() == View.VISIBLE;
    }

    static public void setVisible(View v, int resId) {
        View view = v.findViewById(resId);
        if (view == null) {
            return;
        }
        view.setVisibility(View.VISIBLE);
    }

    static public void setVisible(View v) {
        if (v == null) {
            return;
        }
        v.setVisibility(View.VISIBLE);
    }

    public static void share(Context ctx, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        ctx.startActivity(Intent.createChooser(shareIntent, "分享"));
    }


    public void ShareIntent(Context ctx, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        List<ResolveInfo> resInfo = ctx.getPackageManager().queryIntentActivities(intent, 0);

        if (!resInfo.isEmpty()) {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            for (ResolveInfo info : resInfo) {
                Intent targeted = new Intent(Intent.ACTION_SEND);
                targeted.setType("text/plain");
                ActivityInfo activityInfo = info.activityInfo;
                if (info.activityInfo.packageName
                        .contains("tencent") || info.activityInfo.packageName.contains("weibo")) {
                    targeted.putExtra(Intent.EXTRA_TEXT, content);
                    targeted.setPackage(activityInfo.packageName);
                    targetedShareIntents.add(targeted);
                }
            }

            Intent chooserIntent = targetedShareIntents.size() > 0 ? Intent
                    .createChooser(targetedShareIntents.remove(0), "分享") : null;
            if (chooserIntent != null) {
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                        targetedShareIntents.toArray(new Parcelable[]{}));
                try {
                    ctx.startActivity(chooserIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    showToast("没有相关的分享软件");
                }
            }
        } else {
            showToast("没有相关的分享软件");
        }
    }

    public static void setLastTime(TextView uploadTime, Long createTime) {
        uploadTime.setText(StringUtil.getShortTime(createTime));
    }


}

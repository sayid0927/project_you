package com.zxly.o2o.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshAdapterViewBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.pullrefresh.PullToRefreshScrollView;
import com.zxly.o2o.pullrefresh.PullToRefreshWebView;

public class ViewUtils {


    public static void showToast(String msg) {
        if (StringUtil.isNull(msg))
            return;
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

    /**
     * 显示进度条
     *
     * @param context       环境
     * @param title         标题
     * @param message       信息
     * @return
     */
    public static ProgressDialog showProgress(Context context, CharSequence title, CharSequence message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        // dialog.setDefaultButton(false);
        dialog.show();
        return dialog;
    }

    public static void startActivity(Intent intent, Activity curAct) {
        if (intent != null) {
            curAct.startActivity(intent);
            curAct.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        }
    }
    public  static void startActivityDeviceManager(Activity curAct) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) curAct.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName cm = new ComponentName(curAct, MyDeviceAdmin.class);
        if (!devicePolicyManager.isAdminActive(cm)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cm);
            curAct.startActivity(intent);
        }
    }

    /**
     * 支持HTML 字体设置不需要用多个TextView
     *
     * @param v
     * @param richText
     */
    static public void setRichText(TextView v, String richText) {
        if (v == null || richText == null)
            return;
        v.setText("");
        appendRichText(v, richText);
    }

    static public void appendRichText(TextView v, String richText) {
        if (v == null || richText == null)
            return;
        StringBuilder buf = new StringBuilder(richText);
        while (true) {
            int start = buf.indexOf("&");
            int end = -1;
            if (start != -1)
                end = buf.indexOf("&", start + 1);
            // 有转义符
            if (start != -1 && end != -1) {
                v.append(Html.fromHtml(buf.substring(0, start)));
                String img = buf.substring(start + 1, end);
                boolean zoom = true;
                if (img.length() > 0 && img.charAt(0) == '!') {
                    zoom = false;
                    img = img.substring(1, img.length());
                }
                String text = "&" + img + "&";
                Drawable d = AppController.getInstance().getDrawable(img);
                if (d != null) {

                    ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                    SpannableString tmp = new SpannableString(text);
                    tmp.setSpan(span, 0, text.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    v.append(tmp);
                }
                buf.delete(0, end + 1);
            }
            // 无转义符
            else {
                v.append(Html.fromHtml(buf.toString()));
                break;
            }
        }
    }

    public static void setRefreshText(PullToRefreshListView mListView) {
        mListView.setMode(Mode.BOTH);
        mListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        mListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        mListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                AppController.getInstance().getString(R.string.pull_to_refresh_down));
        mListView.getLoadingLayoutProxy(true, false).setReleaseLabel(
                AppController.getInstance().getString(R.string.pull_to_releaseL_down));

    }

    public static void setRefreshText(PullToRefreshAdapterViewBase mPullToRefrehView) {
        mPullToRefrehView.setMode(Mode.BOTH);
        mPullToRefrehView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefrehView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefrehView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        mPullToRefrehView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        mPullToRefrehView.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                AppController.getInstance().getString(R.string.pull_to_refresh_down));
        mPullToRefrehView.getLoadingLayoutProxy(true, false).setReleaseLabel(
                AppController.getInstance().getString(R.string.pull_to_releaseL_down));

    }

    public static void setRefreshText(PullToRefreshScrollView mScrollView) {
        mScrollView.setMode(Mode.BOTH);
        mScrollView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mScrollView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mScrollView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        mScrollView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        mScrollView.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                AppController.getInstance().getString(R.string.pull_to_refresh_down));
        mScrollView.getLoadingLayoutProxy(true, false).setReleaseLabel(
                AppController.getInstance().getString(R.string.pull_to_releaseL_down));

    }

    public static void setRefreshText(PullToRefreshWebView mWebView) {
        mWebView.setMode(Mode.BOTH);
        mWebView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mWebView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mWebView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        mWebView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        mWebView.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                AppController.getInstance().getString(R.string.pull_to_refresh_down));
        mWebView.getLoadingLayoutProxy(true, false).setReleaseLabel(
                AppController.getInstance().getString(R.string.pull_to_releaseL_down));

    }


    static public void setText(View v, Object o) {
        if (v == null || o == null)
            return;
        if (!(v instanceof TextView))
            return;
        TextView t = (TextView) v;
        if (t == null || o == null)
            return;
        if (o instanceof String) {
            t.setText((String) o);
        } else
            t.setText(o.toString());

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
        if (parent == null)
            return "";
        View v = parent.findViewById(viewId);
        if (v == null)
            return "";
        if (!(v instanceof TextView))
            return "";
        return ((TextView) v).getText().toString().trim();
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

    static public void setGone(View v) {
        if (v == null)
            return;
        v.setVisibility(View.GONE);
    }

    static public void setInvisible(View v) {
        if (v == null)
            return;
        v.setVisibility(View.INVISIBLE);
    }

    static public void setGone(View v, int resId) {
        View view = v.findViewById(resId);
        if (view == null)
            return;
        view.setVisibility(View.GONE);
    }
    static public void setDrawableLeft(TextView textView ,Drawable drawable)
    {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }
    static public void setDrawableRight(TextView textView ,Drawable drawable)
    {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    static public boolean isVisible(View v) {
        return v.getVisibility() == View.VISIBLE;
    }

    static public void setVisible(View v, int resId) {
        View view = v.findViewById(resId);
        if (view == null)
            return;
        view.setVisibility(View.VISIBLE);
    }

    static public void setVisible(View v) {
        if (v == null)
            return;
        v.setVisibility(View.VISIBLE);
    }

    public static void share(Context ctx, String content, Uri uri) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (uri != null) {
            //uri 是图片的地址
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
            // 当用户选择短信时使用sms_body取得文字
            shareIntent.putExtra("sms_body", content);
        } else {
            shareIntent.setType("text/plain");
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        // 自定义选择框的标题
        // startActivity(Intent.createChooser(shareIntent, "邀请好友"));
    }

    public static void share(Context ctx, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        ctx.startActivity(Intent.createChooser(shareIntent, "分享"));
    }

    public static void setLastTime(TextView uploadTime, Long createTime) {
        uploadTime.setText(StringUtil.getShortTime(createTime));
    }


}

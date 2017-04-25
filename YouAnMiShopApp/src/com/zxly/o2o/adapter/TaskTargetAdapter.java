package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.activity.MakeCommissionAct;
import com.zxly.o2o.activity.PromotionArticleAct;
import com.zxly.o2o.activity.QrCodePromotionAct;
import com.zxly.o2o.model.TaskInfo;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MProgressBar;

import java.text.DecimalFormat;

/**
 * Created by kenwu on 2015/12/17.
 */
public class TaskTargetAdapter extends ObjectAdapter {

    public static final int TYPE_APP_DOWNLOAD = 1;
    public static final int TYPE_ARTICLE_PROMOTION = 4;
    public static final int TYPE_PRODUCT_PROMOTION_BROWSER = 2;
    public static final int TYPE_PRODUCT_PROMOTION_COUNT = 3;
    private boolean isGuideShow = true;
    private CallBack callBack;
    private Context context;
    private Activity activity;

    public TaskTargetAdapter(Context _context, CallBack callBack, Activity activity) {
        super(_context);
        this.context = _context;
        this.callBack = callBack;
        this.activity = activity;
    }

    public void setGuideShow(boolean isGuideShow) {
        this.isGuideShow = isGuideShow;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_task_target_guide;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.imgTypeIcon = (ImageView) convertView.findViewById(R.id.img_type_icon);
            holder.txtTaskName = (TextView) convertView.findViewById(R.id.txt_taskName);
            holder.txtAllTask = (TextView) convertView.findViewById(R.id.txt_allTask);
            holder.txtFinishTask = (TextView) convertView.findViewById(R.id.txt_finishTask);
            holder.txtFinishRate = (TextView) convertView.findViewById(R.id.txt_finishRate);
            holder.txtTaskProgress = (TextView) convertView.findViewById(R.id.txt_taskProgress);
            holder.mProgressBar = (MProgressBar) convertView.findViewById(R.id.progress_bar);
            holder.txtUnit = (TextView) convertView.findViewById(R.id.txt_unit);
            holder.btnPromotionNow = (TextView) convertView.findViewById(R.id.btn_promotion_now);
            holder.viewResult = convertView.findViewById(R.id.view_result);
            holder.txtLeftTime = (TextView) convertView.findViewById(R.id.txt_left_time);
            holder.viewGuide = convertView.findViewById(R.id.view_guide);
            holder.imgHand = (ImageView) convertView.findViewById(R.id.img_hand);
            holder.txtFinish = (TextView) convertView.findViewById(R.id.txt_finish);
            holder.contentView = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        fillData((TaskInfo) getItem(position), holder);

        if (isGuideShow) {
            ViewUtils.setVisible(holder.viewGuide);
            ViewUtils.setVisible(holder.imgHand);
        } else {
            ViewUtils.setGone(holder.viewGuide);
            ViewUtils.setGone(holder.imgHand);
        }
        holder.viewGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onCall();
                setGuideShow(false);
                notifyDataSetChanged();
            }
        });

        final TaskInfo taskInfo = (TaskInfo) getItem(position);
        holder.viewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onCall();
                setGuideShow(false);
                notifyDataSetChanged();

                if (taskInfo.getType() == TYPE_ARTICLE_PROMOTION) {
                    PromotionArticleAct.start(activity, 2);
                } else if (taskInfo.getType() == TYPE_PRODUCT_PROMOTION_BROWSER || taskInfo.getType() == TYPE_PRODUCT_PROMOTION_COUNT) {
                    MakeCommissionAct.start(activity, 1);
                } else if (taskInfo.getType() == TYPE_APP_DOWNLOAD) {
                    QrCodePromotionAct.start(activity);
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskInfo.getType() == TYPE_ARTICLE_PROMOTION) {
                    PromotionArticleAct.start(activity, 2);
                } else if (taskInfo.getType() == TYPE_PRODUCT_PROMOTION_BROWSER || taskInfo.getType() == TYPE_PRODUCT_PROMOTION_COUNT) {
                    MakeCommissionAct.start(activity, 1);
                } else if (taskInfo.getType() == TYPE_APP_DOWNLOAD) {
                    QrCodePromotionAct.start(activity);
                }
            }
        });

        return convertView;
    }

    private void fillData(TaskInfo task, ViewHolder holder) {
        String typeName;
        Spanned allTask = null;
        int targetValue = task.getTargetValue();
        int finishValue = task.getFinishValue();
        int type = task.getType();
        if (type == TYPE_APP_DOWNLOAD) {
            holder.imgTypeIcon.setImageResource(R.drawable.icon_promotion_app);
            holder.txtTaskName.setText("APP推荐下载");
            holder.txtUnit.setText("(次)");
            typeName = "下载量";
            holder.mProgressBar.setRoundProgressColor(context.getResources().getColor(R.color.orange_fc911f));
            allTask = Html.fromHtml("<font color=\"#999999\">" + typeName + "</font><font color=\"#fc911f\">&nbsp;" + finishValue + "</font>");
            holder.btnPromotionNow.setBackgroundResource(R.drawable.bg_btn_promotion_app);
            holder.btnPromotionNow.setTextColor(context.getResources().getColor(R.color.orange_fc911f));
            holder.viewResult.setBackgroundResource(R.drawable.bg_task_yellow);
        } else if (type == TYPE_ARTICLE_PROMOTION) {
            holder.imgTypeIcon.setImageResource(R.drawable.icon_promotion_article);
            holder.txtTaskName.setText("文章推广");
            holder.txtUnit.setText("(次)");
            typeName = "浏览量";
            holder.mProgressBar.setRoundProgressColor(context.getResources().getColor(R.color.blue_7199e2));
            allTask = Html.fromHtml("<font color=\"#999999\">" + typeName + "</font><font color=\"#7199e2\">&nbsp;" + finishValue + "</font>");
            holder.btnPromotionNow.setBackgroundResource(R.drawable.bg_btn_promotion_article);
            holder.btnPromotionNow.setTextColor(context.getResources().getColor(R.color.blue_7199e2));
            holder.viewResult.setBackgroundResource(R.drawable.bg_task_blue);
        } else if (type == TYPE_PRODUCT_PROMOTION_BROWSER) {
            holder.imgTypeIcon.setImageResource(R.drawable.icon_promotion_product);
            holder.txtTaskName.setText("商品推广");
            holder.txtUnit.setText("(次)");
            typeName = "浏览量";
            holder.mProgressBar.setRoundProgressColor(context.getResources().getColor(R.color.green_67c136));
            allTask = Html.fromHtml("<font color=\"#999999\">" + typeName + "</font><font color=\"#67c136\">&nbsp;" + finishValue + "</font>");
            holder.btnPromotionNow.setBackgroundResource(R.drawable.bg_btn_promotion_product);
            holder.btnPromotionNow.setTextColor(context.getResources().getColor(R.color.green_67c136));
            holder.viewResult.setBackgroundResource(R.drawable.bg_task_green);
        } else if (type == TYPE_PRODUCT_PROMOTION_COUNT) {
            holder.imgTypeIcon.setImageResource(R.drawable.icon_promotion_product);
            holder.txtTaskName.setText("商品推广");
            holder.txtUnit.setText("(件)");
            typeName = "推广量";
            holder.mProgressBar.setRoundProgressColor(context.getResources().getColor(R.color.green_67c136));
            allTask = Html.fromHtml("<font color=\"#999999\">" + typeName + "</font><font color=\"#67c136\">&nbsp;" + finishValue + "</font>");
            holder.btnPromotionNow.setBackgroundResource(R.drawable.bg_btn_promotion_product);
            holder.btnPromotionNow.setTextColor(context.getResources().getColor(R.color.green_67c136));
            holder.viewResult.setBackgroundResource(R.drawable.bg_task_green);
        }

        int leftValue = targetValue - finishValue;
        if (leftValue < 0) {
            leftValue = 0;
        }
        if (leftValue == 0) {
            ViewUtils.setGone(holder.txtLeftTime);
            if (targetValue == 0) {
                ViewUtils.setVisible(holder.txtFinishRate);
                ViewUtils.setVisible(holder.txtFinishTask);
                holder.txtFinish.setBackgroundResource(0);
                ViewUtils.setGone(holder.txtFinish);
                ViewUtils.setText(holder.btnPromotionNow, "立即推广");
            } else {
                ViewUtils.setGone(holder.txtFinishRate);
                ViewUtils.setGone(holder.txtFinishTask);
                ViewUtils.setVisible(holder.txtFinish);
                holder.txtFinish.setBackgroundResource(R.drawable.bg_task_finish);
                ViewUtils.setText(holder.txtFinish, "全部完成");
                ViewUtils.setText(holder.btnPromotionNow, "再次推广");
            }
        } else {
            ViewUtils.setVisible(holder.txtLeftTime);
            if (type == TYPE_PRODUCT_PROMOTION_COUNT) {
                ViewUtils.setText(holder.txtLeftTime, "剩余" + leftValue + "件未完成");
            } else {
                ViewUtils.setText(holder.txtLeftTime, "剩余" + leftValue + "次未完成");
            }
            ViewUtils.setVisible(holder.txtFinishRate);
            ViewUtils.setVisible(holder.txtFinishTask);
            holder.txtFinish.setBackgroundResource(0);
            ViewUtils.setGone(holder.txtFinish);
            ViewUtils.setText(holder.btnPromotionNow, "立即推广");
        }

        String finishRate;
        float rate;
        if (targetValue > 0 && finishValue > 0) {
            rate = (float) finishValue / (float) targetValue;
            finishRate = getRateNew(rate);
        } else {
            finishRate = "0%";
        }
        holder.txtFinishRate.setText(finishRate);
        holder.txtAllTask.setText(allTask);
        holder.mProgressBar.setMax(targetValue);
        holder.mProgressBar.setProgress(finishValue);
        holder.txtTaskProgress.setText(finishValue + "/" + targetValue);
    }

    static class ViewHolder {
        ImageView imgTypeIcon;
        TextView txtTaskName;
        TextView txtTaskProgress;
        TextView txtFinishTask, txtFinishRate;
        TextView txtAllTask;
        TextView txtUnit;
        TextView txtLeftTime;
        MProgressBar mProgressBar;
        View contentView;
        View viewResult;
        TextView btnPromotionNow;
        View viewGuide;
        ImageView imgHand;
        TextView txtFinish;
    }

    private String getRateNew(float rate){
        float percent = rate * 100;
        int intPercent = (int) percent;
        return intPercent+"%";
    }

    private String getRate(float rate) {
        float percent = rate * 100;
        if (percent > 0.1) {
//            if(percent>=100){
//                return "100%";
//            }
            int intPercent = (int) percent;
            if (intPercent == percent) {
                return intPercent + "%";
            } else {
                DecimalFormat df = new DecimalFormat("#0.0");
                return df.format(percent) + "%";
            }

        }
        return "0.1%<";
    }

}

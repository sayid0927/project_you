/*
 * 文件名：BasicMyCircleThirdActivity.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： BasicMyCircleThirdActivity.java
 * 修改人：Administrator
 * 修改时间：2015-1-10
 * 修改内容：新增
 */
package com.zxly.o2o.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ForumCommunityDetailAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.GetPictureDialog;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.MyCirCleObject;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.model.UserInfoVO;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.ArticleCollectRequst;
import com.zxly.o2o.request.ArticleOperateRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FileUploadRequest;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.request.TopicPraiseRequest;
import com.zxly.o2o.thread.UploadImageTask;
import com.zxly.o2o.util.CallBackWithParam;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.ShakeAnimation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO 添加类的一句话简单描述。
 * <p/>
 * TODO 详细描述
 * <p/>
 * TODO 示例代码
 * <p/>
 * <pre>
 * </pre>
 *
 * @author huangbin
 * @version YIBA-O2O 2015-1-10
 * @since YIBA-O2O
 */
public abstract class MyCircleThirdActAssi extends BasicMyCircleAct implements OnClickListener {
    protected Uri fileUri;
    protected File photo;
    protected ObjectAdapter imageAdapter;
    protected ShakeAnimation getPicBtn;
    protected ImageView picCancel;
    protected View HeaderView, footView;
    private InputMethodManager imm;
    protected Animation animation;
    protected EditText publishForumContent;

    protected int actionType = 0; // 默认-1； 发布成功-2 ；大于 0表示正在操作中;
    protected long parentId = -1;
    protected long detailId;
    private ShareDialog dialog;
    protected static CallBackWithParam callBack;
    protected int countreply;  //评论次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_mycircle_basic_third_layout);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInflater = getLayoutInflater();
        rootLayout = (RelativeLayout) findViewById(R.id.mycircle_third_root_layout);

        findViewById(R.id.mycircle_third_title_back_icon).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mycircle_third_title_back_icon:
                finish();
                break;

            case R.id.reply_pic_select_btn:
                if (fileUri == null) {
                    new GetPictureDialog(false).show(mMainHandler);
                } else if (photo != null) {
                    Intent intent = new Intent(this, TouchImageViewAct.class);
                    intent.putExtra("file_path", photo.getPath());
                    intent.putExtra("file_is_local", true);
                    startActivity(intent);
                }
                break;

            case R.id.forum_publish_pic_clean_btn:
                mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                break;

            case R.id.mycircle_third_top_title_publish_btn:
                if (actionType == -1 || actionType == -2) {
                    if (publishForumContent.getText().toString().trim().length() == 0 ) {
                        ViewUtils.showToast("内容不能为空!");
                    } else {
                        actionType = Constants.ACTION_TYPE_PUBLISH;
                        myUploadRequest();
                    }
                }
                break;

            case R.id.mycircle_third_top_title_collect_btn:
                if ((actionType == -1 || actionType == -2) && MyCircleRequest.shopArticle != null) {
                    ArticleCollectRequst articleCollectRequst;
                    if (MyCircleRequest.shopArticle.getIsCollected() == 1) {
                        actionType = Constants.ACTION_TYPE_UNCOLLECT;
                        articleCollectRequst = new ArticleCollectRequst(detailId, 2);
                    } else {
                        actionType = Constants.ACTION_TYPE_COLLECT;
                        articleCollectRequst = new ArticleCollectRequst(detailId, 1);
                    }
                    articleCollectRequst.setOnResponseStateListener(responseStateListener);
                    articleCollectRequst.start(this);
                }
                break;

            case R.id.mycircle_third_top_title_share_btn:
                if(dialog==null){
                    dialog=new ShareDialog();
                }
                dialog.show( "《" + MyCircleRequest.shopArticle.getTitle() + "》","", StringUtil.appendUrlArgs(MyCircleRequest.shopArticle.getUrl(),new String[]{"isShare=1"}) ,"",null);
                break;

            case R.id.detail_upicon: // 点赞按钮
                if (actionType == -1 || actionType == -2) {
                    if (MyCircleRequest.shopArticle != null) {
                        if (MyCircleRequest.shopArticle.getIsOppose() == 0) {
                            if(MyCircleThirdActAssi.callBack!=null) {
                                MyCircleThirdActAssi.callBack.onCall(0);
                            }
                            if (MyCircleRequest.shopArticle.getIsPraise() != 1) {
                                actionType = Constants.ACTION_TYPE_UP;
                                Oprate(2);
                            } else {
                                actionType = Constants.ACTION_TYPE_CANCEL_UP;
                                Oprate(4);
                            }
                        } else {
                            ViewUtils.showToast("您已踩,不能再次操作");
                        }
                    } else if (MyCircleRequest.shopTopic != null) {
                        if (MyCircleRequest.shopTopic.getIsPraise() != 1) {
                            actionType = Constants.ACTION_TYPE_UP;
                            Oprate(2);
                        } else {
                            actionType = Constants.ACTION_TYPE_CANCEL_UP;
                            Oprate(4);
                        }
                    }
                }
                break;

            case R.id.article_detail_downicon: // 点踩按钮
                if ((actionType == -1 || actionType == -2) && MyCircleRequest.shopArticle != null) {
                    if (MyCircleRequest.shopArticle.getIsPraise() == 0) {
                        if (MyCircleRequest.shopArticle.getIsOppose() != 1) {
                            actionType = Constants.ACTION_TYPE_DOWN;
                            Oprate(1);
                        } else {
                            actionType = Constants.ACTION_TYPE_CANCEL_DOWN;
                            Oprate(3);
                        }
                    } else {
                        ViewUtils.showToast("您已顶,不能再次操作");
                    }
                }
                break;

            case R.id.comment_sendbtn: // 帖子&文章的评论按钮
                if (actionType == -1 || actionType == -2) {
                    if (publishForumContent.getText().toString().trim().length() == 0 && photo == null) {
                        ViewUtils.showToast("内容不能为空!");
                    } else {
                        actionType = Constants.ACTION_TYPE_COMMENT;
                        myUploadRequest();

                    }
                }
                break;

            case R.id.forum_community_detail_mainuser_photo:
                //                Intent intent2 = new Intent(this, TouchImageViewAct.class);
                //                intent2.putExtra("file_path", MyCircleRequest.shopTopic.getUserVO().getThumHeadUrl());
                //                intent2.putExtra("file_is_local", false);
                //                startActivity(intent2);
                break;

            case R.id.detail_loadall:
                boolean isNeedShowAll = ((ForumCommunityDetailAdapter) objectAdapter).getIsNeedShowAll();
                if (!isNeedShowAll) {
                    ((TextView) findViewById(R.id.detail_loadall)).setText("只看我");
                } else {
                    ((TextView) findViewById(R.id.detail_loadall)).setText("载入全部楼层");
                }
                ((ForumCommunityDetailAdapter) objectAdapter).setIsNeedShowAll(!isNeedShowAll);
                break;

            case R.id.reply_refresh_btn:
                page = 1;
                loadData();
                break;

        }
    }

    private void myUploadRequest() {
        imm.hideSoftInputFromWindow(publishForumContent.getWindowToken(), 0);

        Map<String, Object> sendParams = new HashMap<String, Object>();
        sendParams.put("content", publishForumContent.getText() + "");

        if (pageType == Constants.FORUM_COMMUNITY_DETAIL) {
            sendParams.put("topicId", detailId + "");
            startUploadRequest("/user/circle/replyAndroid", sendParams);
        } else if (pageType == Constants.ARTICLE_DETAIL && MyCircleRequest.shopArticle != null) {
            if (Account.user != null) {
                sendParams.put("articleId", MyCircleRequest.shopArticle.getId() + "");
                startUploadRequest("/article/reply", sendParams);
            } else {
                ViewUtils.showToast("您还没有登录");
            }
        } else if (pageType == Constants.FORUM_PUBLISH) {
            sendParams.put("shopId", Config.shopId + "");
            startUploadRequest("/user/circle/publish", sendParams);
        }
    }

    private void startUploadRequest( String requestUrl,  Map<String, Object> sendParams) {
        if (imageAdapter != null) {
            sendParams.put("shopId", String.valueOf(Config.shopId));
            sendParams.put("isShopTopic", String.valueOf(getIntent().getByteExtra("isShop", (byte) -1)));
            sendParams.put("circleId", String.valueOf(getIntent().getLongExtra("circleId", 0)));

            //noinspection unchecked
            new UploadImageTask(requestUrl,mMainHandler,sendParams).execute
                    (imageAdapter.getContent());

        } else {
            sendParams.put("replyerId", String.valueOf(Account.user==null?0:Account.user.getId()));
            sendParams.put("isShopTopic", String.valueOf(getIntent().getByteExtra("isShop", (byte) -1)));
            if (parentId != -1) {
                sendParams.put("parentId", String.valueOf(parentId));
            } else {
                cleanAtWho();
            }

            new FileUploadRequest(photo, sendParams,
                    requestUrl, mMainHandler).startUpload();
        }
    }

    private void Oprate(int operate) {
        // 1===表示踩，2===表示点赞 , 3====表示取消点踩，4====表示取消点赞
        BaseRequest OperateRequst = null;
        if (pageType == Constants.ARTICLE_DETAIL) {
            OperateRequst =
                    new ArticleOperateRequest(detailId, MyCircleRequest.shopArticle.getPlatformArticleId(),
                            operate);
        } else if (pageType == Constants.FORUM_COMMUNITY_DETAIL) {
            OperateRequst = new TopicPraiseRequest(detailId, operate);
        }
        if (OperateRequst != null) {
            OperateRequst.setOnResponseStateListener(responseStateListener);
            OperateRequst.start(this);
        }
    }

    BaseRequest.ResponseStateListener responseStateListener = new BaseRequest.ResponseStateListener() {
        @Override
        public void onOK() {
            // ViewUtils.showToast("操作成功");
            long count = 0;

            switch (actionType) {
                case Constants.ACTION_TYPE_COLLECT: // 收藏

                    MyCircleRequest.shopArticle.setIsCollected(1);
                    ((ImageView) findViewById(R.id.mycircle_third_top_title_collect_btn))
                            .setImageResource(R.drawable.shoucang_press);

                    break;

                case Constants.ACTION_TYPE_UNCOLLECT: // 取消收藏
                    MyCircleRequest.shopArticle.setIsCollected(0);
                    ((ImageView) findViewById(R.id.mycircle_third_top_title_collect_btn))
                            .setImageResource(R.drawable.shouchang);

                    break;

                case Constants.ACTION_TYPE_DOWN: // 点踩
                    if (pageType == Constants.ARTICLE_DETAIL) {
                        final TextView textDown =
                                (TextView) HeaderView.findViewById(R.id.down_operate_text_anim);
                        textDown.setVisibility(View.VISIBLE);
                        textDown.startAnimation(animation);
                        mMainHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                textDown.setVisibility(View.INVISIBLE);
                            }
                        }, 1000);

                        MyCircleRequest.shopArticle.setIsOppose(1);
                        count = MyCircleRequest.shopArticle.getOpposeAmount() + 1;
                        MyCircleRequest.shopArticle.setOpposeAmount(count);
                        ((TextView) HeaderView.findViewById(R.id.article_detail_downicon))
                                .setText(count + "");
                        ((TextView) HeaderView.findViewById(R.id.article_detail_downicon))
                                .setCompoundDrawablesWithIntrinsicBounds(R.drawable.cai_big_press, 0, 0, 0);
                    }
                    break;

                case Constants.ACTION_TYPE_UP: // 点赞

                    if (pageType == Constants.ARTICLE_DETAIL) {
                        final TextView textZan = (TextView) HeaderView.findViewById(R.id.operate_text_anim);
                        textZan.setVisibility(View.VISIBLE);
                        textZan.startAnimation(animation);
                        mMainHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                textZan.setVisibility(View.INVISIBLE);
                            }
                        }, 1000);

                        MyCircleRequest.shopArticle.setIsPraise(1);
                        count = MyCircleRequest.shopArticle.getPraiseAmount() + 1;
                        MyCircleRequest.shopArticle.setPraiseAmount(count);

                        ((TextView) HeaderView.findViewById(R.id.detail_upicon)).setText(count + "");
                        ((TextView) HeaderView.findViewById(R.id.detail_upicon))
                                .setCompoundDrawablesWithIntrinsicBounds(R.drawable.zan_big_press, 0, 0, 0);
                    }
                    break;

                case Constants.ACTION_TYPE_CANCEL_DOWN: // 取消点踩
                    if (pageType == Constants.ARTICLE_DETAIL) {
                        MyCircleRequest.shopArticle.setIsOppose(0);
                        count = MyCircleRequest.shopArticle.getOpposeAmount() - 1;
                        count = (count == -1) ? 0 : count;
                        MyCircleRequest.shopArticle.setOpposeAmount(count);
                        ((TextView) HeaderView.findViewById(R.id.article_detail_downicon))
                                .setText(count + "");
                        ((TextView) HeaderView.findViewById(R.id.article_detail_downicon))
                                .setCompoundDrawablesWithIntrinsicBounds(R.drawable.cai_big_normal, 0, 0, 0);
                    }
                    break;

                case Constants.ACTION_TYPE_CANCEL_UP: // 取消点赞
                    if (pageType == Constants.ARTICLE_DETAIL) {
                        MyCircleRequest.shopArticle.setIsPraise(0);
                        count = MyCircleRequest.shopArticle.getPraiseAmount() - 1;
                        MyCircleRequest.shopArticle.setPraiseAmount(count);

                        count = (count == -1) ? 0 : count;
                        ((TextView) HeaderView.findViewById(R.id.detail_upicon)).setText(count + "");
                        ((TextView) HeaderView.findViewById(R.id.detail_upicon))
                                .setCompoundDrawablesWithIntrinsicBounds(R.drawable.zan_big_normal, 0, 0, 0);
                    }
                    break;

            }
            actionType = -1;
        }

        @Override
        public void onFail(int code) {
            actionType = -1;
            if (code == 20101) {
                ViewUtils.showToast(getResources().getString(R.string.actions_must_sign));
            } else {
                ViewUtils.showToast("操作失败");
            }
        }
    };

    protected void cleanAtWho() {
        parentId = -1;
        publishForumContent.setHint(getResources().getString(R.string.upload_forum_edittext_hint));
    }

    @Override
    public void onBackPressed() {

        if (publishForumContent.getHint().toString().contains("@")) {
            cleanAtWho();
            return;
        }

        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (actionType != -2) {
            photoBitmap = null;
        }
        MyCircleRequest.shopArticle = null;
        MyCircleRequest.shopTopic = null;
        if(countreply>0&&MyCircleThirdActAssi.callBack!=null) {
            MyCircleThirdActAssi.callBack.onCall(countreply);
        }
        MyCircleThirdActAssi.callBack=null;
    }
}

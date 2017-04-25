package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.util.Constants;

/**
 * TODO 添加类的一句话简单描述。 <p/> TODO 详细描述 <p/> TODO 示例代码 <p/> <pre> </pre> @author Administrator @version YIBA-O2O 2014-12-30 @since YIBA-O2O
 */
public abstract class MyCircleSecondActAssi extends BasicMyCircleAct implements OnClickListener {
    protected ShopTopic shopTopTopic1, shopTopTopic2; /* tag参数根据自己需要自己添加在后面，是用来设置title名的*/
    protected long circleId;
    protected byte isShop;  //0:本门店  1：非本门店
    protected String circleType;

    public static void MyCircleLunch(Activity a, int tag, String title) {
        Intent intent = new Intent();
        if (tag > 9) {
            intent.putExtra("mycircle_third_page", tag);
            switch (tag) {
                case Constants.FORUM_COMMUNITY_DETAIL:
                    intent.putExtra("mycircle_third_title", title);
                    intent.putExtras(intent);
                    break;
                case Constants.ARTICLE_DETAIL:
                    intent.putExtra("mycircle_third_title", title);
                    break;
                case Constants.FORUM_PUBLISH:
                    intent.putExtra("mycircle_third_title", title);
                    break;
                default:
                    break;
            }
            intent.setClass(a, MyCircleThirdAct.class);
        } else {
            intent.putExtra("mycircle_second_page", tag);
            switch (tag) {
                case Constants.FORUM_COMMUNITY:
                    intent.putExtra("mycircle_second_title", title);
                    break;
                default:
                    break;
            }
            intent.setClass(a, MyCircleSecondAct.class);
        }
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    public static void MyCircleLunch(Activity a, int tag, String title, byte isShopTopic) {
        Intent intent = new Intent();
        if (tag > 9) {
            intent.putExtra("isShop", isShopTopic);
            intent.putExtra("mycircle_third_page", tag);
            switch (tag) {
                case Constants.FORUM_COMMUNITY_DETAIL:
                    intent.putExtra("mycircle_third_title", title);
                    intent.putExtras(intent);
                    break;
                case Constants.ARTICLE_DETAIL:
                    intent.putExtra("mycircle_third_title", title);
                    break;
                case Constants.FORUM_PUBLISH:
                    intent.putExtra("mycircle_third_title", title);
                    break;
                default:
                    break;
            }
            intent.setClass(a, MyCircleThirdAct.class);
        } else {
            intent.putExtra("isShop", isShopTopic);
            intent.putExtra("mycircle_second_page", tag);
            switch (tag) {
                case Constants.FORUM_COMMUNITY:
                    intent.putExtra("mycircle_second_title", title);
                    break;
                default:
                    break;
            }
            intent.setClass(a, MyCircleSecondAct.class);
        }
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_mycircle_basic_second_layout);

        fm = getSupportFragmentManager();

        mInflater = getLayoutInflater();
        rootLayout = (RelativeLayout) findViewById(R.id.mycircle_root_layout);
        findViewById(R.id.mycircle_title_back_icon).setOnClickListener(this);
        findViewById(R.id.mycircle_second_page_top_tip1).setOnClickListener(this);
        findViewById(R.id.mycircle_second_page_top_tip2).setOnClickListener(this);
        findViewById(R.id.mycircle_second_page_top_tip3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mycircle_title_back_icon:
                finish();
                break;

            case R.id.mycircle_second_page_tab1:
                if (shopTopTopic1 != null) {
                    MyCircleRequest.shopTopic = shopTopTopic1;
                    if ("brandCircle".equals(circleType)) {
                        MyCircleSecondAct.MyCircleLunch(this, Constants.FORUM_COMMUNITY_DETAIL, ((TextView)
                                findViewById(R.id.mycircle_top_title)).getText().toString(), (byte)1);
                    }else{
                        MyCircleSecondAct.MyCircleLunch(this, Constants.FORUM_COMMUNITY_DETAIL, ((TextView)
                                findViewById(R.id.mycircle_top_title)).getText().toString(), shopTopTopic1.getIsShopTopic());
                    }
                }
                break;

            case R.id.mycircle_second_page_tab2:
                if (shopTopTopic2 != null) {
                    MyCircleRequest.shopTopic = shopTopTopic2;
                    if ("brandCircle".equals(circleType)) {
                        MyCircleSecondAct.MyCircleLunch(this, Constants.FORUM_COMMUNITY_DETAIL, ((TextView)
                                findViewById(R.id.mycircle_top_title)).getText().toString(), (byte)1);
                    } else {
                        MyCircleSecondAct.MyCircleLunch(this, Constants.FORUM_COMMUNITY_DETAIL, ((TextView)
                                findViewById(R.id.mycircle_top_title)).getText().toString(), shopTopTopic2.getIsShopTopic());
                    }
                }
                break;

            case R.id.mycircle_publish_forum_btn:
                Intent intent = new Intent();
                intent.putExtra("isShop", isShop);  //默认否
                intent.putExtra("mycircle_third_title",
                        "发帖");
                intent.putExtra("circleId", circleId);
                intent.putExtra("mycircle_third_page", Constants.FORUM_PUBLISH);
                intent.setClass(this, MyCircleThirdAct.class);
                EaseConstant.startActivity(intent, this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        MyCircleRequest.publishTopic = null;
        photoBitmap = null;
    }

}

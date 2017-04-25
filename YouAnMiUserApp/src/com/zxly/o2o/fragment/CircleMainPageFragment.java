package com.zxly.o2o.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.ui.EaseBaiduMapActivity;
import com.zxly.o2o.activity.ForumAllListAct;
import com.zxly.o2o.activity.ForumGuideAct;
import com.zxly.o2o.activity.MyCircleSecondAct;
import com.zxly.o2o.adapter.CircleForumListAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.CircleForumVO;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ForumMainRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.view.MyFlipperView;

import java.util.List;

/**
 * Created by Administrator on 2015/12/15.
 */
public class CircleMainPageFragment extends BaseListViewFragment {
    private ForumMainRequest forumMainRequest;
    public static boolean isReLoad;

    View view;

    @Override
    public void onDestroy() {
        super.onDestroy();

        saveTopTopicId();


    }

    private void saveTopTopicId() {
        String ids = "";
        if (objectAdapter.getCount() > 1) {
            CircleForumVO circleForumVO;
            circleForumVO = (CircleForumVO) objectAdapter.getItem(1);
            if (circleForumVO.getIsTop() == 1) {
                ids = String.valueOf(circleForumVO.getId());

                for (int i = 2; i < objectAdapter.getCount(); i++) {
                    circleForumVO = (CircleForumVO) objectAdapter.getItem(i);
                    if (circleForumVO.getIsTop() == 1) {
                        ids = ids + "," + circleForumVO.getId();
                    } else {
                        break;
                    }
                }
            }
        }
        PreferUtil.getInstance().setForumTopList(ids);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isReLoad || (objectAdapter != null && objectAdapter.getCount() == 0 && !isFirstLoad)) {
            page = 1;
            loadData();
            isReLoad = false;
        }
    }

    @Override
    protected void loadData() {
        if (forumMainRequest == null) {
            forumMainRequest = new ForumMainRequest();
            forumMainRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    if (forumMainRequest.copyList.size() != 0 ||
                            forumMainRequest.recommendCircles.size() != 0) {
                        if (page == 1) {
                            objectAdapter.clear();
                            if (forumMainRequest.recommendSize > 5) {
                                if(view==null) {
                                    addBtnMore();
                                }
                            } else  {
                                if (view != null) {
                                    mListView.removedF(view);
                                    view = null;
                                }
                            }
                        }
                        ((CircleForumListAdapter) objectAdapter).setrecommendPosition(
                                forumMainRequest.recommendSize == 0 ? -1 : forumMainRequest
                                        .copyList.size() - forumMainRequest.recommendSize);
                        objectAdapter.addItem(forumMainRequest.copyList, true);
                        if (isFirstLoad) {
                            viewContainer.setDisplayedChild(MyFlipperView.LOADSUCCESSFUL, true);
                            isFirstLoad = false;
                        }
                    } else {
                        if (view != null) {
                            mListView.removedF(view);
                        }
                        viewContainer.setDisplayedChild(MyFlipperView.NODATA, true);
                    }
                    mListView.onRefreshComplete();
                }

                @Override
                public void onFail(int code) {
                    viewContainer.setDisplayedChild(MyFlipperView.LOADFAIL, true);
                    mListView.onRefreshComplete();
                }
            });
        } else {
            forumMainRequest.concernCircles.clear();
            forumMainRequest.recommendCircles.clear();
            forumMainRequest.shopCircle = new CircleForumVO();
        }
        forumMainRequest.start();

    }

    @Override
    protected void initListView(Bundle bundle) {

        /*第一次打开圈子显示圈子引导界面*/
        if (!PreferUtil.getInstance().getForumGuideIsShow()) {
            EaseConstant.startActivityNormal(ForumGuideAct.class, getActivity());
        }

        objectAdapter = new CircleForumListAdapter(content.getContext(), -1, new CallBack() {
            @Override
            public void onCall() {
                saveTopTopicId();
                loadData();
            }
        });

        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mListView.setDivideHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 0.5,
                AppController
                        .displayMetrics));
    }

    private void addBtnMore() {
        view = getActivity().getWindow().getLayoutInflater().inflate(R.layout.circle_main_page_footview,
                null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EaseConstant.startActivityNormalWithString(ForumAllListAct.class,
                        getActivity(), "圈子列表");
            }
        });
        mListView.addF(view);
    }

    @Override
    protected int layoutId() {
        return R.layout.mycircle_page_layout;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - 1;
        CircleForumVO item = (CircleForumVO) objectAdapter.getItem(position);
        if (position <= objectAdapter.getCount() + 1) {
            Intent intent = new Intent();

            //本店交流的圈子为平台圈子
            if (position == 0) {
                intent.putExtra("isShop", (byte) 0);  //是
            } else {
                intent.putExtra("isShop", (byte) 1);  //否
            }
            intent.putExtra("mycircle_second_title",
                    item.getName());
            intent.putExtra("circleId", item.getId());
            intent.setClass(getActivity(), MyCircleSecondAct.class);
            EaseConstant.startActivity(intent, getActivity());
        }
    }
}

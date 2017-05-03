package com.zxly.o2o.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.AppException;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsnx on 2016/9/12.
 */
public class CollegeCourseView extends LinearLayout {
    private int type;
    private int contentType;//课程内容类型 1.视频文章,0.图文文章
    private long createTime;
    private int id;//课程id
    private String image;//课程封面
    private String[] label;//课程标签
    private int learnCount;//学习人数
    private String title;//课程名称
    private String typeName;

    private TextView txtTypeName,txtTitle,txtTime,txtRenshu,txtLabel1,txtLabel2;
    private View btnClose;
    private NetworkImageView imgHead;
    private boolean isExistCollege;

    @Override
    public int getId() {
        return id;
    }

    /**
     *
     * @param context
     * @param type 1:客多多 2:粉丝 3:会员 4:活动 5:店铺
     */
    public CollegeCourseView(Context context,int type) {
        super(context);
        this.type=type;
        init();
    }
    private void  init()
    {
        inflate(getContext(), R.layout.view_college_course, this);
        setVisibility(View.GONE);
        txtTypeName= (TextView) findViewById(R.id.txt_typename);
        txtTitle=(TextView)findViewById(R.id.txt_title);
        txtTime= (TextView) findViewById(R.id.txt_time);
        txtRenshu= (TextView) findViewById(R.id.txt_renshu);
        txtLabel1= (TextView) findViewById(R.id.txt_label1);
        txtLabel2= (TextView) findViewById(R.id.txt_label2);
        btnClose=findViewById(R.id.btn_close);
        imgHead= (NetworkImageView) findViewById(R.id.img_head_icon);
        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.GONE);
            }
        });
        show();
    }
    public void show(){
        new SpecificLessonRequest().start();
    }
    class SpecificLessonRequest extends BaseRequest{


       SpecificLessonRequest()
      {
         addParams("type",type);
          setOnResponseStateListener(new ResponseStateListener() {
              @Override
              public void onOK() {
                  if(isExistCollege)
                  {
                      isExistCollege=false;
                      ViewUtils.setText(txtTypeName,"："+typeName);
                      ViewUtils.setText(txtTitle,title);


                      if(label!=null)
                      {
                          int length=label.length;
                          for(int i=0;i<length;i++)
                          {
                              switch (i)
                              {
                                  case 0:
                                      ViewUtils.setVisible(txtLabel1);
                                      String labelName=label[i];
                                      if(!labelName.equals("推荐"))
                                      {
                                          txtLabel1.setBackgroundColor(Color.parseColor("#1EA1F7"));
                                      }
                                      txtLabel1.setText(label[i]);
                                      break;
                                  case 1:
                                      ViewUtils.setVisible(txtLabel2);
                                      txtLabel2.setText(label[i]);
                                      break;
                              }
                          }
                      }
                      txtTime.setText(TimeUtil.formatTimeHHMMDD(createTime));
                      imgHead.setImageUrl(image, AppController.imageLoader);
                      Spanned txtlearnCount= Html.fromHtml("<font color=\"#f49126\">" + learnCount + "&nbsp;</font>"+"人正在学习");
                      txtRenshu.setText(txtlearnCount);
                      setVisibility(View.VISIBLE);
                  }else
                  {
                      setVisibility(View.GONE);
                  }

              }

              @Override
              public void onFail(int code) {
                  setVisibility(View.GONE);
              }
          });
      }
        @Override  
        protected String method() {
            return "/my/specificLesson";
        }

        @Override
        protected void fire(String data) throws AppException {
            isExistCollege=true;
            try {
                JSONObject jsonObject=new JSONObject(data);
                contentType=jsonObject.optInt("contentType");
                createTime=jsonObject.optLong("createTime");
                id=jsonObject.optInt("id");
                image=jsonObject.optString("image");
                learnCount=jsonObject.optInt("num");
                title=jsonObject.optString("title");
                typeName=jsonObject.optString("type");
                JSONArray labelArray=jsonObject.getJSONArray("label");
                int length=labelArray.length();
                label=new String[length];
                for(int i=0;i<length;i++)
                {
                    label[i]=labelArray.optString(i);
                }
            } catch (JSONException e) {
                throw JSONException(e);
            }
        }

    }
}

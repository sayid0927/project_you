package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.model.YamLessonInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hejun on 2016/10/18.
 */
public class YamLessonRequest extends BaseRequest{

    private int contentType;
    private long createTime;
    private int id;
    private String image;
    private int learnCount;
    private String title;
    private String typeName;
    private String[] label;
    private YamLessonInfo yamLessonInfo;

    public  YamLessonRequest(int type){
        addParams("type",type);
    }
    @Override
    protected String method() {
        return "/my/specificLesson";
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject=new JSONObject(data);
            yamLessonInfo = new YamLessonInfo();
            contentType =jsonObject.optInt("contentType");
            createTime =jsonObject.optLong("createTime");
            id =jsonObject.optInt("id");
            image =jsonObject.optString("image");
            learnCount =jsonObject.optInt("num");
            title =jsonObject.optString("title");
            typeName =jsonObject.optString("type");
            yamLessonInfo.setContentType(contentType);
            yamLessonInfo.setCreateTime(createTime);
            yamLessonInfo.setId(id);
            yamLessonInfo.setImage(image);
            yamLessonInfo.setLearnCount(learnCount);
            yamLessonInfo.setTitle(title);
            yamLessonInfo.setTypeName(typeName);
            JSONArray labelArray=jsonObject.getJSONArray("label");
            int length=labelArray.length();
            label=new String[length];
            for(int i=0;i<length;i++)
            {
                label[i]=labelArray.optString(i);
            }
            yamLessonInfo.setLabel(label);
        } catch (JSONException e) {
            throw JSONException(e);
        }
    }

    public YamLessonInfo getYamLessonInfo(){
        return yamLessonInfo;
    }

}

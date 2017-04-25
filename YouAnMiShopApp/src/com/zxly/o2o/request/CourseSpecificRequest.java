package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

import org.json.JSONObject;

/**
 * @author fengrongjian 2016-8-31
 * @description 获取商学院指定课程
 */
public class CourseSpecificRequest extends BaseRequest {
    private long id;
    private String image;
    private long num;
    private String title;

    /**
     * @param type 1：粉丝 2：用户 3：店铺 4:客多多
     */
    public CourseSpecificRequest(long type) {
        addParams("type", type);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            id = jsonObject.optLong("id");
            image = jsonObject.getString("image");
            num = jsonObject.optLong("num");
            title = jsonObject.getString("title");
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "my/specificLesson";
    }

    public long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public long getNum() {
        return num;
    }

    public String getTitle() {
        return title;
    }
}

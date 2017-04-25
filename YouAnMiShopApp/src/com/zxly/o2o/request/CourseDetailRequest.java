package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.CollegeCourse;

/**
 * @author fengrongjian 2016-8-31
 * @description 获取文章详细信息
 */
public class CourseDetailRequest extends BaseRequest {
    private CollegeCourse collegeCourse;

    public CourseDetailRequest(long articleId) {
        addParams("articleId", articleId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            TypeToken<CollegeCourse> type = new TypeToken<CollegeCourse>() {
            };
            collegeCourse = GsonParser.getInstance().fromJson(data, type);
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "my/getArticleInfo";
    }

    public CollegeCourse getCollegeCourse() {
        return collegeCourse;
    }
}

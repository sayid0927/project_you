package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.CollegeCourse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2016-8-31
 * @description 获取最新的一篇文章
 */
public class CourseNewestRequest extends BaseRequest {
    private List<CollegeCourse> collegeCourseList = new ArrayList<CollegeCourse>();

    public CourseNewestRequest(long articleId) {
        addParams("articleId", articleId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            TypeToken<List<CollegeCourse>> type = new TypeToken<List<CollegeCourse>>() {
            };
            collegeCourseList = GsonParser.getInstance().fromJson(data, type);
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "my/getNewArticleOne";
    }

    public List<CollegeCourse> getCollegeCourseList() {
        return collegeCourseList;
    }
}

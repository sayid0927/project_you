package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.CollegeCourse;
import com.zxly.o2o.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2016-8-31
 * @description 获取学院文章列表
 */
public class CourseListRequest extends BaseRequest {
    private List<CollegeCourse> collegeCourseList = new ArrayList<CollegeCourse>();

    public CourseListRequest(long pageIndex, long pageSize) {
        addParams("pageIndex", pageIndex);
        addParams("pageSize", pageSize);
    }

    public CourseListRequest(String articleName, long articleTypeId, long pageIndex, long pageSize) {
        if(!StringUtil.isNull(articleName)){
            addParams("articleName", articleName);
        }
        if(articleTypeId != 0){
            addParams("articleTypeId", articleTypeId);
        }
        addParams("pageIndex", pageIndex);
        addParams("pageSize", pageSize);
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
        return false;
    }

    @Override
    protected String method() {
        return "my/getArticleList";
    }

    public List<CollegeCourse> getCollegeCourseList() {
        return collegeCourseList;
    }
}

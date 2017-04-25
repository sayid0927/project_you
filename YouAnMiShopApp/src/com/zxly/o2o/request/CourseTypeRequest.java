package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.CourseType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2016-8-31
 * @description 获取文章类型条件
 */
public class CourseTypeRequest extends BaseRequest {
    private List<CourseType> courseTypeList = new ArrayList<CourseType>();

    public CourseTypeRequest(long shopId) {
        addParams("shopId", shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            TypeToken<List<CourseType>> type = new TypeToken<List<CourseType>>() {
            };
            courseTypeList = GsonParser.getInstance().fromJson(data, type);
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
        return "my/getArticleType";
    }

    public List<CourseType> getCourseTypeList() {
        return courseTypeList;
    }
}

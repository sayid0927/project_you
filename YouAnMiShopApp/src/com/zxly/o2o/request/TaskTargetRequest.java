package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.TaskInfo;
import com.zxly.o2o.util.DataUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by kenwu on 2015/12/15.
 */
public class TaskTargetRequest extends BaseRequest {

    private List<TaskInfo> taskList;
    private boolean hasPromotion = false;//三种推广方式是否存在任意一项不为0（存在：true, 不存在：false）
    private boolean hasTarget = false;//是否存在任务指标（存在：true, 不存在：false）
    private boolean showTips = false;//是否应该显示未完成推广任务的底部提示

    public TaskTargetRequest(int year,int month) {
        addParams("userId", Account.user.getId());
        addParams("shopId",Account.user.getShopId());
        addParams("month",month);
        addParams("year",year);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if(json.has("taskList")){
                TypeToken<List<TaskInfo>> types = new TypeToken<List<TaskInfo>>() {};
                String str = json.getString("taskList");
                taskList = GsonParser. getInstance().fromJson(str, types);
            }
            if (!DataUtil.listIsNull(taskList)) {
                for (TaskInfo taskInfo : taskList) {
                    if (taskInfo.getTargetValue() != 0) {
                        hasTarget = true;
                        break;
                    }
                }
            }
            if (!DataUtil.listIsNull(taskList)) {
                for (TaskInfo taskInfo : taskList) {
                    if (taskInfo.getFinishValue() != 0) {
                        hasPromotion = true;
                        break;
                    }
                }
                if(hasPromotion){
                    for (TaskInfo taskInfo : taskList) {
                        if (taskInfo.getTargetValue() != 0 && taskInfo.getFinishValue() < taskInfo.getTargetValue()) {
                            showTips = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

    }

    public boolean isHasPromotion() {
        return hasPromotion;
    }

    public boolean isHasTarget() {
        return hasTarget;
    }

    public boolean isShowTips() {return showTips; }

    public List<TaskInfo> getTaskList() {
        return taskList;
    }

    @Override
    protected String method() {
        return "/makeFans/taskKpi";
    }
}

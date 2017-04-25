package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.TaskInfo;
import com.zxly.o2o.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/7/13.
 */
public class HistoryStatisticsRequest extends BaseRequest {

    private List<TaskInfo> taskInfos=new ArrayList<TaskInfo>();
    public HistoryStatisticsRequest(String date) {
        addParams("shopId", Account.user.getShopId());
        if(Account.user.getRoleType()!= Constants.USER_TYPE_ADMIN)
        {
            addParams("userId",Account.user.getId());
        }

        addParams("date",date);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray=new JSONArray(data);
            int len=jsonArray.length();
            for(int i=0;i<len;i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                TaskInfo taskInfo=new TaskInfo();
                taskInfo.setCurProgress(jsonObject.getInt("curProgress"));
                taskInfo.setName(jsonObject.getString("name"));
                taskInfo.setTarget(jsonObject.getInt("target"));
                taskInfo.setUnitName(jsonObject.getString("unitName"));
                taskInfos.add(taskInfo);
            }
        } catch (JSONException e) {
            throw JSONException(e);
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    public List<TaskInfo> getTaskInfos() {
        return taskInfos;
    }

    @Override
    protected String method() {
        return "/promote/quota/statistics";
    }
}

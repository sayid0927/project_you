package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.PostInfo;
import com.zxly.o2o.model.PostTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/7/6.
 */
public class GeneralizeTargetSetInitRequest extends BaseRequest {
    private List<PostInfo> postInfos = new ArrayList<PostInfo>();
    private String[] names;

    public GeneralizeTargetSetInitRequest() {
        addParams("shopId", Account.user.getShopId());

    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray = new JSONArray(data);
            int len = jsonArray.length();
            names = new String[len];
            for (int i = 0; i < len; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PostInfo postInfo = new PostInfo();
                postInfo.setPostId(jsonObject.getInt("postId"));
                postInfo.setPostName(jsonObject.getString("postName"));
                if (!jsonObject.has("tasks")) {
                    postInfo.setSave(false);
                } else {
                    postInfo.setSave(true);
                    JSONArray tasks = new JSONArray(jsonObject.getString("tasks"));
                    int taskLen = tasks.length();
                    for (int k = 0; k < taskLen; k++) {
                        JSONObject jsonTask = tasks.getJSONObject(k);
                        int type = jsonTask.getInt("type");
                        String target=jsonTask.getString("target");
                        Integer id=null;
                        if(jsonTask.has("id"))
                        {
                            id=jsonTask.getInt("id");
                        }
                         PostTask postTask=new  PostTask(type,target,id);
                        postInfo.tasks.add(postTask);
                    }
                }

                postInfos.add(postInfo);
                names[i] = postInfo.getPostName();
            }
        } catch (JSONException e) {
            throw JSONException(e);
        }
    }

    public String[] getNames() {
        return names;
    }

    public List<PostInfo> getPostInfos() {
        return postInfos;
    }

    @Override
    protected String method() {
        return "/promote/setQuota/init";
    }
}

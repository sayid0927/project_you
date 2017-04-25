package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.MyPromotionMember;
import com.zxly.o2o.model.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author fengrongjian 2015-12-7
 * @description 推广人详细网络请求
 */
public class PromotionDetailRequest extends BaseRequest {
    private User user;
    private ArrayList<MyPromotionMember> membersList;

    public PromotionDetailRequest(long userId) {
        addParams("userId", userId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);

            if (json.has("myMembers")) {
                String myMembers = json.getString("myMembers");
                if (myMembers != null) {
                    TypeToken<List<MyPromotionMember>> type = new TypeToken<List<MyPromotionMember>>() {
                    };
                    membersList = (ArrayList<MyPromotionMember>) GsonParser.getInstance()
                            .fromJson(myMembers, type);
                    for (MyPromotionMember myPromotionMember : membersList) {
                        if ("".equals(myPromotionMember.getLetter())) {
                            myPromotionMember.setLetter("#");
                        }
                    }
                    Collections.sort(membersList, comparator);
                }
            }

            if (json.has("userInfo")) {
                String userInfo = json.getString("userInfo");
                if (userInfo != null) {
                    user = GsonParser.getInstance().getBean(userInfo, User.class);
                }
            }

        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "bind/bindingPromoterPage";
    }

    public ArrayList<MyPromotionMember> getMembersList() {
        return membersList;
    }

    public User getUser() {
        return user;
    }

    Comparator<MyPromotionMember> comparator = new Comparator<MyPromotionMember>() {
        public int compare(MyPromotionMember member1, MyPromotionMember member2) {
            if (member1.getLetter().equals("@")
                    || member2.getLetter().equals("#")) {
                return -1;
            } else if (member1.getLetter().equals("#")
                    || member2.getLetter().equals("@")) {
                return 1;
            } else {
                return member1.getLetter().compareTo(member2.getLetter());
            }
        }
    };

}

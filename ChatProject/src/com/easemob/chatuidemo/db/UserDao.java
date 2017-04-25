/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.db;

import java.util.List;
import java.util.Map;

import android.content.Context;

import com.easemob.chatuidemo.domain.RobotUser;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;

public class UserDao {
	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	public static final String COLUMN_NAME_REMARKNAME = "remarkName";
	public static final String COLUMN_NAME_USERID = "userId";
	public static final String COLUMN_NAME_ISBLACK = "isBlack";
	public static final String COLUMN_NAME_SHOPID = "shopId";
	public static final String COLUMN_NAME_GROUPID = "groupId";
	public static final String COLUMN_NAME_GROUPNAME = "groupName";
	public static final String COLUMN_HX_ID = "toChatUserID";
	public static final String COLUMN_NAME_ADDEDTIME = "addedTime";
	public static final String COLUMN_NAME_CREATETIME = "createTime";
	public static final String COLUMN_NAME_GENDER = "gender";
	public static final String COLUMN_NAME_ORIGINHEADURL = "originHeadUrl";
	public static final String COLUMN_NAME_SIGNATURE = "signature";
	public static final String COLUMN_NAME_ISTOP = "isTop";
	public static final String COLUMN_NAME_INITIAL= "initial";

	public static final String PREF_TABLE_NAME = "pref";
	public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
	public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";

	public static final String ROBOT_TABLE_NAME = "robots";
	public static final String ROBOT_COLUMN_NAME_ID = "username";
	public static final String ROBOT_COLUMN_NAME_NICK = "nick";
	public static final String ROBOT_COLUMN_NAME_AVATAR = "avatar";

	public static final String UNREGIST_TABLE_NAME = "unregist";
	public static final String UNREGIST_IMEI = "imei";
	
	
	public UserDao(Context context) {
	}

	/**
	 * 保存好友list
	 * 
	 * @param contactList
	 */
	public void saveYAMContactList(List<EaseYAMUser> contactList) {
		DemoDBManager.getInstance().saveYAMContactList(contactList);
	}
	public void saveContactList(List<EaseUser> contactList) {
		DemoDBManager.getInstance().saveContactList(contactList);
	}
	public void saveUnRegistList(List<EaseUser> contactList) {
		DemoDBManager.getInstance().saveUnRegistList(contactList);
	}

	public void deleteContactList() {
		DemoDBManager.getInstance().deleteContactList();
	}

	/**
	 * 获取好友list
	 * 
	 * @return
	 */
	public Map<String, EaseYAMUser> getYAMContactList() {
	    return DemoDBManager.getInstance().getYAMContactList();
	}
	public List<EaseYAMUser> getUnRegistList() {
		return DemoDBManager.getInstance().getUnRegistContactList();
	}

	/**
	 * 删除一个联系人
	 * @param username
	 */
	public void deleteYAMContact(String username){
	    DemoDBManager.getInstance().deleteYAMContact(username);
	}

	/**
	 * 删除一个联系人
	 * @param userId
	 */
	public void deleteYAMContactWithId(String userId){
		DemoDBManager.getInstance().deleteYAMContact(userId);
	}
	
	/**
	 * 保存一个联系人
	 * @param user
	 */
	public void saveYAMContact(EaseYAMUser user){
	    DemoDBManager.getInstance().saveYAMContact(user);
	}
	
	public void setDisabledGroups(List<String> groups){
	    DemoDBManager.getInstance().setDisabledGroups(groups);
    }
    
    public List<String>  getDisabledGroups(){       
        return DemoDBManager.getInstance().getDisabledGroups();
    }
    
    public void setDisabledIds(List<String> ids){
        DemoDBManager.getInstance().setDisabledIds(ids);
    }
    
    public List<String> getDisabledIds(){
        return DemoDBManager.getInstance().getDisabledIds();
    }
    
    public Map<String, RobotUser> getRobotUser(){
    	return DemoDBManager.getInstance().getRobotList();
    }
    
    public void saveRobotUser(List<RobotUser> robotList){
    	DemoDBManager.getInstance().saveRobotList(robotList);
    }
}

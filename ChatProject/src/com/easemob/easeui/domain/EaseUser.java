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
package com.easemob.easeui.domain;

import com.easemob.chat.EMContact;

public class EaseUser extends EMContact {
	private long createTime;

	private int gender;  //1:man 2:women

	private String signature="";

	private String originHeadUrl="";
	private String thumHeadUrl="";
	private String nickname="";
	private String userName="";
	private String groupName="";
	private String name="";
	private long groupId;
	private int isTop;   //0：false 1：true
	private boolean isOnline;

    /**
     * 昵称首字母
     */
	protected String initialLetter="";
	/**
	 * 用户头像
	 */
	protected String avatar;

	protected long id;
	protected long userId;

	private String imei="";

	private UserRemark userRemark = new UserRemark();
	public class UserRemark {
		String remarkName="";
		String description="";

		public String getRemarkName() {
			return remarkName;
		}

		public void setRemarkName(String remarkName) {
			this.remarkName = remarkName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public UserRemark getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(UserRemark userRemark) {
		this.userRemark = userRemark;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public EaseUser(String username){
	    this.username = username;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getInitialLetter() {
		return initialLetter;
	}

	public void setInitialLetter(String initialLetter) {
		this.initialLetter = initialLetter;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getOriginHeadUrl() {
		return originHeadUrl;
	}

	public void setOriginHeadUrl(String originHeadUrl) {
		this.originHeadUrl = originHeadUrl;
	}

	public String getThumHeadUrl() {
		return thumHeadUrl;
	}

	public void setThumHeadUrl(String thumHeadUrl) {
		this.thumHeadUrl = thumHeadUrl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof EaseUser)) {
			return false;
		}
		return getUsername().equals(((EaseUser) o).getUsername());
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}
}

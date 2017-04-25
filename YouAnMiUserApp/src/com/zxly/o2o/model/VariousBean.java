package com.zxly.o2o.model;

/**
 * 提供公共属性集中的对象
 * <p>
 * 
 * <pre>
 * </pre>
 * 
 * @author 龙汀<Administrator>
 */
public abstract class VariousBean extends MyCirCleObject {
	private long id;

	private long createTime;

	private long updateTime;

	private int createrId;

	private int updateId;

	private String nickname = "";

	private boolean isNeedPraiseAnim;

	public boolean isNeedPraiseAnim() {
		return isNeedPraiseAnim;
	}

	public void setNeedPraiseAnim(boolean needPraiseAnim) {
		isNeedPraiseAnim = needPraiseAnim;
	}

	public Integer getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getCreaterId() {
		return createrId;
	}

	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}

	public String getNickName() {
		return nickname;
	}

	public void setNikeName(String nickName) {
		this.nickname = nickname;
	}

}

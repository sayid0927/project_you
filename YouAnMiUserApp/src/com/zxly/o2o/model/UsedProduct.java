package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

public class UsedProduct extends BaseProduct {
	private int assureParty;// 担保方（1：个人发布，2门店担保）
	private String realName;// 联系人姓名
	private String deprName;// 折旧类型中文名称
	private long putawayTime;// 发布时间
    private String desc="";//物品描述
	private List<UsedDesc> usedDescList=new ArrayList<UsedDesc>();
	private Assure assure;//保修信息



    public void addAllUsedDescList(List<UsedDesc> list)
	{
		if(list!=null&&!list.isEmpty())
		{
			usedDescList.addAll(list);
		}
	}
	
	public int getAssureParty() {
		return assureParty;
	}

	public void setAssureParty(int assureParty) {
		this.assureParty = assureParty;
	}
	

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}


	public String getDeprName() {
		return deprName;
	}

	public void setDeprName(String deprName) {
		this.deprName = deprName;
	}

	public long getPutawayTime() {
		return putawayTime;
	}

	public void setPutawayTime(long putawayTime) {
		this.putawayTime = putawayTime;
	}

	public List<UsedDesc> getUsedDescList() {
		return usedDescList;
	}

	public Assure getAssure() {
		return assure;
	}

	public void setAssure(Assure assure) {
		this.assure = assure;
	}
    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

}

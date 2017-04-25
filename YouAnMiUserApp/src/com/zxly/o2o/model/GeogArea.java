package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

public class GeogArea {
	private long id;
	private String name;
	private List<GeogVillage> villageList = new ArrayList<GeogVillage>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GeogVillage> getVillageList() {
		return villageList;
	}

	public void setVillageList(List<GeogVillage> villageList) {
		this.villageList = villageList;
	}
	@Override
	public String toString() {
		return name;
	}

	@Override
    public boolean equals(Object o)
    {
        if (o instanceof GeogArea)
        {
            if (((GeogArea)o).id == this.id)
            {
                return true;
            }
        }
        return false;
    }
	


}

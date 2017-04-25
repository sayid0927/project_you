package com.zxly.o2o.model;

public class GeogVillage {
	private long id;
	private String name;
	private String spell;
	private int type;

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

	public String getSpell() {
		return spell;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
    public boolean equals(Object o)
    {
        if (o instanceof GeogVillage)
        {
            if (((GeogVillage)o).id == this.id)
            {
                return true;
            }
        }
        return false;
    }

}

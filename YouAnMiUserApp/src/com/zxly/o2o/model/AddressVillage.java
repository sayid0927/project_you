package com.zxly.o2o.model;

import java.io.Serializable;

public class AddressVillage implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String spell;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public void setSpell(String spell) {
		this.spell = spell;
	}

}

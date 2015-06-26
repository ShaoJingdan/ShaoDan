package com.sugon.app.dto;

import java.util.List;

public class Brand {
	public int itemcount;
	public String name;
	public List<String> types;
	public Brand(int itemcount, String name, List<String> types) {
		super();
		this.itemcount = itemcount;
		this.name = name;
		this.types = types;
	}
	
	

}

package org.neuroph.contrib.util;

public enum GridType {
	
	STATIC(1),
	INCREMENTAL(2);
	
	private int type;
	
	GridType(int type){
		this.setType(type);
		
	}

	public Integer getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String toString(){
		return this.getType().toString();
	}

}

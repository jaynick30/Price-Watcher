package model;

public enum Shipping {
	FREE, PAID;
	
	public String isFree() {
		if (this.equals(FREE)){return "1";}
		else{return "0";}
	}
}

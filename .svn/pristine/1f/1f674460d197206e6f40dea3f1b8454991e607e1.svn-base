package org.pgcl.enums;

public enum DepositPurpose {
	
	CONNECTION(0,"Connection"),
	DISCONNECTION(1,"Disconnection"),
	RECONNECTION(2,"Reconnection"),
	LOAD_INCREASE(3,"Load Increase"),
	LOAD_DECREASE(4,"Load Decrease"),
	NAME_CHANGE(5,"Name Change"),
	BURNER_SHIFTING(6,"Burner Shifting"),
	ILLEGAL_OPERATION(7,"Illegal Operation"),
	SECURITY_ADJUSTMENT(8,"Security Deposit"),
	OTHERS(9,"Others");
	
    private String label;
    private int id;

    private DepositPurpose(int id,String label) {
        this.id=id;
    	this.label = label;
    }

    public String getLabel() {
        return label;
    }
    public int getId() {
        return id;
    }

}

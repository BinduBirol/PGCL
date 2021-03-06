package org.pgcl.enums;

public enum  DisconnCauseNonMeter {

	ILLEGAL_CONNECTION(0,"Illegal Connection"),
    ARREAR_BILL(1,"Arrear Bill"),
    APPLIED_BY_CUSTOMER(2,"Applied by Customer "),
	GOVT_TRANSFER(3,"Due to Govt Transfer"),
	NATURAL_CALAMITY(4,"Due to Natural Calamity"),
	REPAIR_MAINTAIN(5,"Repair and Maintenance");

    private String label;
    private int id;

    private DisconnCauseNonMeter(int id,String label) {
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

package com.example.primavista.production.entity;

public enum Size {
	
	XS("XS"),
	S("S"),
	M("M"),
	L("L"),
	XL("XL"),
	XXL("XXL"),
	XXXL("XXXL");
	
private final String displayValue;
    
    private Size(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }

}

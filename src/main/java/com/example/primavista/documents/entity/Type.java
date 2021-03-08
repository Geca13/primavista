package com.example.primavista.documents.entity;

public enum Type {
	
	INCOMMING("Incomming"),
	OUTGOING("Outgoing");
	
private final String displayValue;
    
    private Type(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }

}

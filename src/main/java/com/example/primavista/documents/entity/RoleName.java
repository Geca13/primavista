package com.example.primavista.documents.entity;

public enum RoleName {
	
	ROLE_ADMIN("Admin"),
	ROLE_USER("User");

	 private final String displayValue;
	    
	    private RoleName(String displayValue) {
	        this.displayValue = displayValue;
	    }
	    
	    public String getDisplayValue() {
	        return displayValue;
	    }
	

}

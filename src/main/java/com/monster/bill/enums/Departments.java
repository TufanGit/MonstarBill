package com.monster.bill.enums;

/**
 * Departments of the Application
 * @author prashant
 * 14-07-2022
 */
public enum Departments {

	// enum fields
	FINANCE("Finance"),
	HR("HR"),
	ADMIN("Admin"),
	PRODUCTION("Production"),
	DESIGN("Design");
	
	// constructor
    private Departments(final String department) {
        this.department = department;
    }
 
    // internal state
    private String department;
 
    public String getDepartment() {
        return department;
    }
    
}

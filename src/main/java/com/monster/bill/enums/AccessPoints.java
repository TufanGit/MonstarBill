package com.monster.bill.enums;

/**
 * Access Points of the application
 * @author prashant
 * 14-07-2022
 */
public enum AccessPoints {

	// enum fields
	AP_INVOICE("AP Invoice"),
	PAYMENT("Payment"),
	VENDOR_REPORT("Vendor Report"),
	MAIL_DASHBOARD("Mail Dashboard");
	
	// constructor
    private AccessPoints(final String accessPoint) {
        this.accessPoint = accessPoint;
    }
 
    // internal state
    private String accessPoint;
 
    public String getAccessPoint() {
        return accessPoint;
    }
    
}

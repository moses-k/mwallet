package com.mwallet.tps.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "atm_details")
public class ATM {

	@Id
	@Column(name = "atmcode", columnDefinition="CHAR(10) NOT NULL")
	private String atmCode;
	
	@Column(name = "location", columnDefinition="CHAR(50) NOT NULL")
	private String location;
	
	@Column(name = "vendor", columnDefinition="CHAR(50) NOT NULL")
	private String vendor;
	
	@Column(name = "serialnumber",columnDefinition="CHAR(30) NOT NULL")
	private String serialNumber;

	public String getAtmCode() {
		return atmCode;
	}

	public String getLocation() {
		return location;
	}

	public String getVendor() {
		return vendor;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setAtmCode(String atmCode) {
		this.atmCode = atmCode;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	
	
}

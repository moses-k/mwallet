package com.mwallet.tps.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transaction_rules")
public class TransactionRules {
	
	@Id
	@Column(name = "txntype" ,columnDefinition="CHAR(3) NOT NULL")
	private String txnType;
	
	@Column(name = "description", columnDefinition="VARCHAR(50) NOT NULL")
	private String description;
	
	@Column(name = "usertype", columnDefinition="VARCHAR(12) NOT NULL")
	private String usertype;
	
	@Column(name = "status", columnDefinition="CHAR(12) NOT NULL")
	private String status;
	
	@Column(name = "createdon")
	private String createdon;

}

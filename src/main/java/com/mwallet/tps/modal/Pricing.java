package com.mwallet.tps.modal;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "txn_pricing")
public class Pricing {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "planid", columnDefinition = "INT NOT NULL")
	private String planId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "txntype", referencedColumnName = "txnType", unique = true)
	@JsonIgnore
	private TransactionRules rules;
	
	@Column(name = "varfee", precision = 10, scale = 2)
	private BigDecimal varFee;
	
	@Column(name = "fixedfee", precision = 10, scale = 2)
	private BigDecimal fixedFee;
	
	@Column(name = "status", columnDefinition="VARCHAR(10) NOT NULL")
	private String status;
	
	@Column(name = "minimum_amount", precision = 10, scale = 2)
	private BigDecimal minAmount;
	
	@Column(name = "max_amount", precision = 10, scale = 2)
	private BigDecimal maxAmount;
	
	@Column(name = "paymode", columnDefinition="VARCHAR(12) NOT NULL")
	private String payMode;
	

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public TransactionRules getRules() {
		return rules;
	}

	public void setRules(TransactionRules rules) {
		this.rules = rules;
	}

	public BigDecimal getVarFee() {
		return varFee;
	}

	public void setVarFee(BigDecimal varFee) {
		this.varFee = varFee;
	}

	public BigDecimal getFixedFee() {
		return fixedFee;
	}

	public void setFixedFee(BigDecimal fixedFee) {
		this.fixedFee = fixedFee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	
	
	
}

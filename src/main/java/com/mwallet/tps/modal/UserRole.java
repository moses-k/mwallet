package com.mwallet.tps.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_role")
public class UserRole {
	public UserRole() {
		
	}
	public UserRole(Integer code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_code")
	private Integer code;
	
	@Column(name = "description")
	private String description;

	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

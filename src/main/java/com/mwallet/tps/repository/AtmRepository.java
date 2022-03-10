package com.mwallet.tps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mwallet.tps.modal.ATM;

public interface AtmRepository extends JpaRepository<ATM, String> {

}

package com.mwallet.tps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mwallet.tps.modal.ATMTransactions;

public interface AtmTransactionRepository extends JpaRepository<ATMTransactions, String> {

}

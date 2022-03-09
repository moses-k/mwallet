package com.mwallet.tps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mwallet.tps.modal.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions, String> {

}

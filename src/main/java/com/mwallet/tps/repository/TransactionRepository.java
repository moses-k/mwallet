package com.mwallet.tps.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mwallet.tps.modal.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions, String> {
	
	@Query(value = "select e from Transactions e where e.walletId =:walletid ") // using @query with native
	List<Transactions> findLastTenTxn(@Param("walletid") String walletId);
	

}

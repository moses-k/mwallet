package com.mwallet.tps.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mwallet.tps.modal.Pricing;

public interface PricingRepository extends JpaRepository<Pricing, String> {
	
	@Query(value = "select e from Pricing e where e.rules.txnType =:txntype ") // using @query with native
	Optional<Pricing> findByTxnType(@Param("txntype") String txnType);
	

}

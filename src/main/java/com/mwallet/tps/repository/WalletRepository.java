package com.mwallet.tps.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mwallet.tps.modal.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, String>{
	
	@Query(value = "select e from Wallet e where e.user.relationshipNo =:id ") // using @query with native
	Optional<Wallet> findByRelationshipNo(@Param("id") String id);
	
	@Modifying
	@Query(value = "update Wallet d set d.currentBalance = :currentbalance where  d.walletId= :walletid") //using query
	void setUpdateWallet (@Param("currentbalance") BigDecimal currentbalance, @Param("walletid") String walletid);

}

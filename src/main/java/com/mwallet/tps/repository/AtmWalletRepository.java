package com.mwallet.tps.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mwallet.tps.modal.ATMWallet;

public interface AtmWalletRepository extends JpaRepository<ATMWallet, String> {

	@Query(value = "select e from ATMWallet e where e.atm.atmCode =:atmcode ")
	Optional<ATMWallet> findByAtmCode(@Param("atmcode") String atmcode);

	@Modifying
	@Query(value = "update ATMWallet d set d.cashBalance = :cashbalance,  d.floatBalance = "
			+ ":floatbalance where  d.walletId= :walletid") 		
	void setUpdateWallet(@Param("cashbalance") BigDecimal cashBalance, @Param("floatbalance") BigDecimal floatBalance,
			@Param("walletid") String walletid);

}

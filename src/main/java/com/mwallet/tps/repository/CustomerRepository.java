package com.mwallet.tps.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mwallet.tps.modal.User;

public interface CustomerRepository extends JpaRepository<User, String> {
	
	@Query(value = "select e from User e where e.email =:id ") // using @query with native
	Optional<User> findByEmail(@Param("id") String id);
	
	

}

package com.costly.costly.repository;

import com.costly.costly.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends org.springframework.data.jpa.repository.JpaRepository<com.costly.costly.model.Transaction, Long> {
    Transaction findTransactionById(Long id);
    @Query("SELECT t FROM Transaction t WHERE t.list.id = :id")
    java.util.List<com.costly.costly.model.Transaction> findTransactionsByListId(Long id);
}

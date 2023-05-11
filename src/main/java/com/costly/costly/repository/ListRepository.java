package com.costly.costly.repository;

import com.costly.costly.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListRepository extends org.springframework.data.jpa.repository.JpaRepository<com.costly.costly.model.List, Long> {
    com.costly.costly.model.List findListById(Long id);

    @Query("SELECT t FROM Transaction t WHERE t.list.id = :id")
    List<Transaction> findTransactionsByListId(Long id);
}

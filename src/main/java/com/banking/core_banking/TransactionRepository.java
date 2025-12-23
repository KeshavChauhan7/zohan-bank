package com.banking.core_banking;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // This allows us to fetch all transactions sorted by the newest first
    List<Transaction> findAllByOrderByTimestampDesc();
}
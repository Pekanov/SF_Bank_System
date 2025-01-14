package org.skillfactory.sf_bank_system.repository;

import org.skillfactory.sf_bank_system.model.entity.Budget;
import org.skillfactory.sf_bank_system.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


}

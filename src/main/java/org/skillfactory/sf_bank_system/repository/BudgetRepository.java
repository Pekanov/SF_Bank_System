package org.skillfactory.sf_bank_system.repository;

import org.skillfactory.sf_bank_system.model.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
}

package com.gl.fms.repository;

import com.gl.fms.dto.ExpenseCategoryDTO;
import com.gl.fms.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory,Integer> {
    Optional<ExpenseCategory> findByName(String categoryName);
}

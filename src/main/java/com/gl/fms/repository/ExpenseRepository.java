package com.gl.fms.repository;

import com.gl.fms.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
//    List<Expense> findByUserIdAndDateBetween(Integer userId, LocalDateTime localDateTime, LocalDateTime localDateTime1);
//    List<Expense> findByUser_IdAndCategory_Id(Integer userId, Integer categoryId);
//    @Query("SELECT e FROM Expense e WHERE e.id IN (SELECT exp.id FROM Expense exp WHERE exp.user.id = :userId)")
//    List<Expense> findExpensesByUserId(@Param("userId") Integer userId);
////
//    @Query("SELECT new com.gl.fms.dto.ExpenseByCategoryDTO(e.category.name, SUM(e.amount)) " +
//            "FROM Expense e WHERE e.user.id = :userId GROUP BY e.category.name")
//    List<ExpenseByCategoryDTO> getGroupedExpensesByCategory(@Param("userId") Integer userId);
}


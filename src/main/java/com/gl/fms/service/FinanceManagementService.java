package com.gl.fms.service;

import com.gl.fms.dto.*;
import com.gl.fms.entity.Expense;
import com.gl.fms.exception.FinanceManagementSystemException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FinanceManagementService {

    // 1. User Registration & Budget Creation
    ResponseDTO addUserAndCreateAssociatedRecords(UserDTO userDTO) throws FinanceManagementSystemException;
    ResponseDTO deleteUserAndAllAssociatedData(String email) throws FinanceManagementSystemException;

    // 2. Goal Tracking
    ResponseDTO createGoal(String userEmail,GoalDTO goalDTO) throws FinanceManagementSystemException;
    ResponseDTO updateGoalStatus(String userEmail,String goalName, String status) throws FinanceManagementSystemException;
    List<GoalDTO> getGoalsProgressOfaUser(String userEmail) throws FinanceManagementSystemException;
    ResponseDTO deleteGoalOfaUser(String userEmail,String goalName) throws FinanceManagementSystemException;

    // 3. Income Management
    ResponseDTO addIncomeSourceToUser(String userEmail,IncomeDTO incomeDTO) throws FinanceManagementSystemException;
    ResponseDTO updateIncomeSourceByEmail(String userEmail,IncomeDTO incomeDTO) throws FinanceManagementSystemException;
    ResponseDTO deleteIncomeSourceByEmail(String userEmail,String source) throws FinanceManagementSystemException;

    // 4. Expense Management & Categorization
    ResponseDTO addExpenseToUser(String userEmail,ExpenseDTO expenseDTO) throws FinanceManagementSystemException;
    ResponseDTO updateExpenseByUserEmail(String userEmail,ExpenseDTO expenseDTO) throws FinanceManagementSystemException;
    ResponseDTO deleteExpenseByNameAndDate(String userEmail, String expenseName, LocalDate targetDate) throws FinanceManagementSystemException;

    // 5. Search & Analytics
    IncomeAndExpenseDTO getIncomeAndExpensesByDateRange(String userEmail, LocalDate startDate, LocalDate endDate) throws FinanceManagementSystemException;
    Map<String,Float> getExpensesPercentageByCategory(String userEmail) throws FinanceManagementSystemException;

    // 6. Dashboard
    DashboardDTO getDashboardStats(String userEmail) throws FinanceManagementSystemException;

}

package com.gl.fms.controller;

import com.gl.fms.dto.*;
import com.gl.fms.entity.Expense;
import com.gl.fms.exception.FinanceManagementSystemException;
import com.gl.fms.service.FinanceManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fms")
public class FinanceManagementController {

    @Autowired
    private FinanceManagementService financeManagementService;

    // 1. User
    @PostMapping("/add-user-and-associated-records")
    public ResponseEntity<ResponseDTO> addUserAndCreateAssociatedRecords(@RequestBody @Valid UserDTO userDTO) throws FinanceManagementSystemException {
        ResponseDTO responseDTO = financeManagementService.addUserAndCreateAssociatedRecords(userDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-user-and-all-associated-data/{email}")
    public ResponseEntity<ResponseDTO> deleteUserAndAllAssociatedData(@PathVariable @Email String email) throws FinanceManagementSystemException {
        ResponseDTO responseDTO = financeManagementService.deleteUserAndAllAssociatedData(email);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 2. Goals
    @PostMapping("/create-goal/{userEmail}")
    public ResponseEntity<ResponseDTO> createGoal(@PathVariable @Email String userEmail,@RequestBody @Valid GoalDTO goalDTO) throws FinanceManagementSystemException {
        ResponseDTO responseDTO = financeManagementService.createGoal(userEmail,goalDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update-goal-status/{userEmail}/{goalName}/{status}")
    public ResponseEntity<ResponseDTO> updateGoalStatus(@PathVariable @Email String userEmail,@PathVariable String goalName,@PathVariable String status)throws FinanceManagementSystemException {
        ResponseDTO responseDTO = financeManagementService.updateGoalStatus(userEmail,goalName, status);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/get-goals-progress-of-a-user/{userEmail}")
    public ResponseEntity<List<GoalDTO>> getGoalsProgressOfaUser(@PathVariable @Email String userEmail) throws FinanceManagementSystemException {
        List<GoalDTO> goalDTOS = financeManagementService.getGoalsProgressOfaUser(userEmail);
        return new ResponseEntity<>(goalDTOS, HttpStatus.OK);
    }

    @DeleteMapping("/delete-goal-of-a-user/{userEmail}/{goalName}")
    public ResponseEntity<ResponseDTO> deleteGoalOfaUser(@PathVariable @Email String userEmail,@PathVariable String goalName) throws FinanceManagementSystemException {
        ResponseDTO responseDTO = financeManagementService.deleteGoalOfaUser(userEmail,goalName);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 3. Income
    @PostMapping("/add-income-source-to-user/{userEmail}")
    public ResponseEntity<ResponseDTO> addIncomeSourceToUser(@PathVariable @Email String userEmail,@RequestBody @Valid IncomeDTO incomeDTO) throws FinanceManagementSystemException {
        ResponseDTO responseDTO = financeManagementService.addIncomeSourceToUser(userEmail,incomeDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update-income-by-email/{userEmail}")
    public ResponseEntity<ResponseDTO> updateIncomeSourceByEmail(@PathVariable @Email String userEmail,@RequestBody @Valid IncomeDTO incomeDTO) throws FinanceManagementSystemException {
        ResponseDTO responseDTO = financeManagementService.updateIncomeSourceByEmail(userEmail,incomeDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete-income-by-email/{userEmail}/{source}")
    public ResponseEntity<ResponseDTO> deleteIncomeSourceByEmail(@PathVariable @Email String userEmail ,@PathVariable String source) throws FinanceManagementSystemException {
        ResponseDTO responseDTO = financeManagementService.deleteIncomeSourceByEmail(userEmail,source);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 4. Expense
    @PostMapping("/add-expense-to-user/{userEmail}")
    public ResponseEntity<ResponseDTO> addExpenseToUser(@PathVariable @Email String userEmail,@RequestBody @Valid ExpenseDTO expenseDTO) throws FinanceManagementSystemException{
        ResponseDTO responseDTO = financeManagementService.addExpenseToUser(userEmail,expenseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update-expense-by-user-email/{userEmail}")
    public ResponseEntity<ResponseDTO> updateExpenseByUserEmail(@PathVariable @Email String userEmail,@RequestBody @Valid ExpenseDTO expenseDTO) throws FinanceManagementSystemException {
        ResponseDTO responseDTO = financeManagementService.updateExpenseByUserEmail(userEmail,expenseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete-expense-by-name-and-date/{userEmail}/{expenseName}/{targetDate}")
    public ResponseEntity<ResponseDTO> deleteExpenseByNameAndDate(@PathVariable @Email String userEmail,@PathVariable String expenseName,@PathVariable LocalDate targetDate){
        ResponseDTO responseDTO = financeManagementService.deleteExpenseByNameAndDate(userEmail, expenseName, targetDate);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 5. Income & Expense by Date Range
    @GetMapping("/get-income-and-expenses-by-date-range/{userEmail}/{startDate}/{endDate}")
    public ResponseEntity<IncomeAndExpenseDTO> getIncomeAndExpensesByDateRange(@PathVariable @Email String userEmail,@PathVariable LocalDate startDate,@PathVariable LocalDate endDate) throws FinanceManagementSystemException {
        IncomeAndExpenseDTO incomeAndExpenseDTO = financeManagementService.getIncomeAndExpensesByDateRange(userEmail,startDate,endDate);
        return new ResponseEntity<>(incomeAndExpenseDTO, HttpStatus.OK);
    }

    // 6. Expense by Category
    @GetMapping("/get-Expenses-percentage-By-Category/{userEmail}")
    public ResponseEntity<Map<String,Float>> getExpensesPercentageByCategory(@PathVariable @Email String userEmail) throws FinanceManagementSystemException{
        Map<String,Float> responseDTO = financeManagementService.getExpensesPercentageByCategory(userEmail);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // 7. Dashboard Stats
    @GetMapping("/dashboard/{userEmail}")
    public ResponseEntity<?> getDashboardStats(@PathVariable @Email String userEmail) {
        try {
            DashboardDTO dashboardDTO = financeManagementService.getDashboardStats(userEmail);
            return ResponseEntity.ok(dashboardDTO);
        } catch (FinanceManagementSystemException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace(); // for debugging, consider logging in real apps
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Internal error"));
        }
    }

}

package com.gl.fms.service;

import com.gl.fms.dto.*;
import com.gl.fms.entity.*;
import com.gl.fms.exception.FinanceManagementSystemException;
import com.gl.fms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinanceManagementServiceImpl implements FinanceManagementService{

    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseDTO addUserAndCreateAssociatedRecords(UserDTO userDTO) throws FinanceManagementSystemException{
        User user = userRepository.findByEmail(userDTO.getEmail());

        if(user!=null)
            throw new FinanceManagementSystemException("Already user exists!");

        User newuser=new User();
        newuser.setEmail(userDTO.getEmail());
        newuser.setName(userDTO.getName());

        List<Goal> goalList = new ArrayList<>();
        List<Income> incomeList =new ArrayList<>();
        List<Expense> expenseList =new ArrayList<>();

        //Goal
        for(GoalDTO goalDTO : userDTO.getGoalDTOS()){
            Goal goal =new Goal();
            goal.setName(goalDTO.getName());
            goal.setStatus(goalDTO.getStatus());
            goal.setTargetDate(goalDTO.getTargetDate());
            goal.setTargetAmount(goalDTO.getTargetAmount());

            goalRepository.save(goal);
            goalList.add(goal);
        }
        newuser.setGoals(goalList);

        //Income
        for(IncomeDTO incomeDTO : userDTO.getIncomeDTOS()){
            Income income = new Income();
            income.setAmount(incomeDTO.getAmount());
            income.setDate(incomeDTO.getDate());
            income.setSource(incomeDTO.getSource());

            incomeRepository.save(income);
            incomeList.add(income);
        }
        newuser.setIncomes(incomeList);

        // EXPENSES
        for (ExpenseDTO expenseDTO : userDTO.getExpenseDTOS()) {
            Expense expense = new Expense();
            expense.setDate(expenseDTO.getDate());
            expense.setAmount(expenseDTO.getAmount());
            expense.setName(expenseDTO.getName());

            // Handle ExpenseCategory (Many expenses can share one category)
            String categoryName = expenseDTO.getExpenseCategoryDTO().getName();

            Optional<ExpenseCategory> optionalCategory = expenseCategoryRepository.findByName(categoryName);
            ExpenseCategory expenseCategory;
            if (optionalCategory.isPresent()) {
                expenseCategory = optionalCategory.get();
            } else {
                expenseCategory = new ExpenseCategory();
                expenseCategory.setName(categoryName);
                expenseCategory = expenseCategoryRepository.save(expenseCategory);
            }
            // Set the category in the expense
            expense.setCategory(expenseCategory);

            // Save the expense
            expense = expenseRepository.save(expense);
            expenseList.add(expense);
        }
        newuser.setExpenses(expenseList);

        userRepository.save(newuser);

//        Generating Dashboard
        DashboardDTO dashboardDTO = getDashboardStats(newuser.getEmail());
        Dashboard dashboard = new Dashboard();

        dashboard.setTotalIncome(dashboardDTO.getTotalIncome());
        dashboard.setTotalExpense(dashboardDTO.getTotalExpenses());
        dashboard.setBalance(
                Optional.ofNullable(dashboard.getTotalIncome()).orElse(0.0)
                        - Optional.ofNullable(dashboard.getTotalExpense()).orElse(0.0)
        );

        dashboardRepository.save(dashboard);  // Save first to generate ID

        newuser.setDashboard(dashboard);
        userRepository.save(newuser);

        return  new ResponseDTO("Sucessfully added user and created Associated Records");
    }

    @Override
    public ResponseDTO deleteUserAndAllAssociatedData(String email) throws FinanceManagementSystemException {
        User user = userRepository.findByEmail(email);

        if (user==null) {
            throw new FinanceManagementSystemException("User not found with Email: " + email);
        }

        // Delete related records if not handled by cascade
        incomeRepository.deleteAll(user.getIncomes());
        expenseRepository.deleteAll(user.getExpenses());
        goalRepository.deleteAll(user.getGoals());
        dashboardRepository.delete(user.getDashboard());

        // Finally, delete user
        userRepository.delete(user);
        return  new ResponseDTO("Sucessfully deleted user and Associated Records");
    }

    //GOAL
    public ResponseDTO createGoal(String userEmail, GoalDTO goalDTO) throws FinanceManagementSystemException {
        // Fetch user
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new FinanceManagementSystemException("User does not exist to add Goal!");
        }

        boolean goalExists = user.getGoals().stream()
                .anyMatch(goal -> goal.getName().equalsIgnoreCase(goalDTO.getName()));

        if (goalExists) {
            throw new FinanceManagementSystemException("The Goal is already present!");
        }

        // Create new goal
        Goal newGoal = new Goal();
        newGoal.setName(goalDTO.getName());
        newGoal.setStatus(goalDTO.getStatus());
        newGoal.setTargetDate(goalDTO.getTargetDate());
        newGoal.setTargetAmount(goalDTO.getTargetAmount());

        // Add to user's goals and save
        user.getGoals().add(newGoal);
        userRepository.save(user);

        return new ResponseDTO("Successfully created new goal for user");
    }

    public ResponseDTO updateGoalStatus(String userEmail,String goalName, String status) throws FinanceManagementSystemException{
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new FinanceManagementSystemException("User does not exist to Update Goal!");
        }

        Optional<Goal> userGoal = user.getGoals().stream()
                .filter(goal -> goal.getName().equalsIgnoreCase(goalName))
                .findFirst();

        if (userGoal.isEmpty()) {
            throw new FinanceManagementSystemException("The Goal does not exist!");
        }

        userGoal.get().setStatus(status);
        userRepository.save(user);
//        goalRepository.save(userGoal.get()); //not need because user cascade all operations
        return new ResponseDTO("Sucessfully Updated goal Status");
    }
    public List<GoalDTO> getGoalsProgressOfaUser(String userEmail) throws FinanceManagementSystemException {
        User user = userRepository.findByEmail(userEmail);
        if (user==null) {
            throw new FinanceManagementSystemException("User does not exist!");
        }

        List<Goal> goals = user.getGoals();
        if (goals == null || goals.isEmpty()) {
            throw new FinanceManagementSystemException("User does not have goals");
        }

        // Convert Goal entities to GoalDTOs
        List<GoalDTO> goalDTOs = new ArrayList<>();
        for (Goal goal : goals) {
            GoalDTO dto = new GoalDTO();
            dto.setName(goal.getName());
            dto.setStatus(goal.getStatus());
            dto.setTargetDate(goal.getTargetDate());
            dto.setTargetAmount(goal.getTargetAmount());
            goalDTOs.add(dto);
        }

        return goalDTOs;
    }
    public ResponseDTO deleteGoalOfaUser(String userEmail, String goalName) throws FinanceManagementSystemException {

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new FinanceManagementSystemException("User does not exist to delete goal!");
        }

        Optional<Goal> userGoalOptional = user.getGoals().stream()
                .filter(goal -> goal.getName().equalsIgnoreCase(goalName))
                .findFirst();

        if (userGoalOptional.isEmpty()) {
            throw new FinanceManagementSystemException("The goal does not exist!");
        }

        Goal userGoal = userGoalOptional.get();
        // Remove the goal from the user's list
        user.getGoals().remove(userGoal);
        // goalRepository.delete(userGoal); // user entity will cascade no need
        userRepository.save(user);

        return new ResponseDTO("The goal was successfully deleted");
    }


    // 3. Income Management
    public ResponseDTO addIncomeSourceToUser(String userEmail, IncomeDTO incomeDTO) throws FinanceManagementSystemException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new FinanceManagementSystemException("User does not exist!");
        }

        Optional<Income> optionalIncome = user.getIncomes().stream()
                .filter(i -> i.getSource().equalsIgnoreCase(incomeDTO.getSource()))
                .findFirst();

        if (optionalIncome.isPresent()) {
            throw new FinanceManagementSystemException("The income source is already present!");
        }

        Income newIncome = new Income();
        newIncome.setSource(incomeDTO.getSource());
        newIncome.setDate(incomeDTO.getDate());
        newIncome.setAmount(incomeDTO.getAmount());

        user.getIncomes().add(newIncome);
        userRepository.save(user);

        return new ResponseDTO("The income source was added successfully");
    }

    public ResponseDTO updateIncomeSourceByEmail(String userEmail, IncomeDTO incomeDTO) throws FinanceManagementSystemException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new FinanceManagementSystemException("User does not exist!");
        }

        Optional<Income> optionalIncome = user.getIncomes().stream()
                .filter(income -> income.getSource().equals(incomeDTO.getSource()))
                .findFirst();

        if (optionalIncome.isEmpty()) {
            throw new FinanceManagementSystemException("The income source does not exist for this user!");
        }

        Income income = optionalIncome.get();
        income.setAmount(incomeDTO.getAmount());
        income.setDate(incomeDTO.getDate());

        userRepository.save(user);

        return new ResponseDTO("The income source was successfully updated");
    }

    public ResponseDTO deleteIncomeSourceByEmail(String userEmail,String source) throws FinanceManagementSystemException{
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new FinanceManagementSystemException("User does not exist!");
        }

        Optional<Income> optionalIncome = user.getIncomes().stream()
                .filter(income -> income.getSource().equals(source))
                .findFirst();

        if (optionalIncome.isEmpty()) {
            throw new FinanceManagementSystemException("The income source does not exist for this user!");
        }

        Income income = optionalIncome.get();
        user.getIncomes().remove(income);
        incomeRepository.delete(income);
        userRepository.save(user);

        return new ResponseDTO("The income source was successfully deleted");
    }

    // 4. Expense Management & Categorization
    public ResponseDTO addExpenseToUser(String userEmail,ExpenseDTO expenseDTO) throws FinanceManagementSystemException{
         User user = userRepository.findByEmail(userEmail);
         if(user == null)
             throw new FinanceManagementSystemException("User does not Exits!");
         Optional<Expense> optionalExpense = user.getExpenses().stream().filter(i->i.getName().equalsIgnoreCase(expenseDTO.getName())).findFirst();
         if(optionalExpense.isPresent()){
             throw new FinanceManagementSystemException("Expense already exits");}

         Expense expense = new Expense();
         expense.setName(expenseDTO.getName());
         expense.setDate(expenseDTO.getDate());
         expense.setAmount(expenseDTO.getAmount());

        // Check existing ExpenseCategory or create new
        String categoryName = expenseDTO.getExpenseCategoryDTO().getName();
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByName(categoryName)
                .orElseGet(() -> {
                    ExpenseCategory newCategory = new ExpenseCategory();
                    newCategory.setName(categoryName);
                    return expenseCategoryRepository.save(newCategory);
                });
        expense.setCategory(expenseCategory);

        // Save expense explicitly before adding to user
        Expense savedExpense = expenseRepository.save(expense);
        user.getExpenses().add(savedExpense);
        userRepository.save(user);

        return new ResponseDTO("The Expense source Sucessfully added!");
    }

    public ResponseDTO updateExpenseByUserEmail(String userEmail, ExpenseDTO expenseDTO) throws FinanceManagementSystemException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new FinanceManagementSystemException("User does not exist!");
        }

        Optional<Expense> optionalExpense = user.getExpenses().stream()
                .filter(i -> i.getName().equalsIgnoreCase(expenseDTO.getName()) && i.getDate().equals(expenseDTO.getDate()))
                .findFirst();

        if (optionalExpense.isEmpty()) {
            throw new FinanceManagementSystemException("Expense does not exist to update!");
        }

        Expense expense = optionalExpense.get();
        expense.setAmount(expenseDTO.getAmount());
        expense.setName(expenseDTO.getName());
        expense.setDate(expenseDTO.getDate());

        String categoryName = expenseDTO.getExpenseCategoryDTO().getName();

        ExpenseCategory category = expenseCategoryRepository.findByName(categoryName)
                .orElseGet(() -> {
                    ExpenseCategory newCategory = new ExpenseCategory();
                    newCategory.setName(categoryName);
                    return expenseCategoryRepository.save(newCategory);
                });

        expense.setCategory(category);

        expenseRepository.save(expense); // Save updated expense explicitly
        userRepository.save(user);

        return new ResponseDTO("The expense was successfully updated!");
    }
    public ResponseDTO deleteExpenseByNameAndDate(String userEmail, String expenseName, LocalDate targetDate) throws FinanceManagementSystemException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new FinanceManagementSystemException("User does not exist!");
        }

        Optional<Expense> optionalExpense = user.getExpenses().stream()
                .filter(expense -> expense.getName().equalsIgnoreCase(expenseName)
                        && expense.getDate().equals(targetDate))
                .findFirst();

        if (optionalExpense.isEmpty()) {
            throw new FinanceManagementSystemException("Expense not found for the given name and date!");
        }

        Expense expense = optionalExpense.get();
        user.getExpenses().remove(expense);
        expenseRepository.delete(expense); // Optional: based on cascade config
        userRepository.save(user);

        return new ResponseDTO("Expense '" + expenseName + "' on " + targetDate + " was successfully deleted!");
    }


    // 5. Search & Analytics
    @Override
    public IncomeAndExpenseDTO getIncomeAndExpensesByDateRange(String userEmail, LocalDate startDate, LocalDate endDate) throws FinanceManagementSystemException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new FinanceManagementSystemException("User does not exist with email: " + userEmail);

        List<Expense> expenseList = user.getExpenses().stream()
                .filter(i -> !i.getDate().isBefore(startDate) && !i.getDate().isAfter(endDate))
                .toList();

        List<Income> incomeList = user.getIncomes().stream()
                .filter(i -> !i.getDate().isBefore(startDate) && !i.getDate().isAfter(endDate))
                .toList();
        List<ExpenseDTO> expenseDTOList = new ArrayList<>();
        List<IncomeDTO> incomeDTOList = new ArrayList<>();

        for (Expense expense : expenseList) {
            ExpenseDTO expenseDTO = new ExpenseDTO(expense.getName(), expense.getAmount(), expense.getDate());

            ExpenseCategoryDTO expenseCategoryDTO = new ExpenseCategoryDTO();
            if (expense.getCategory() != null) {
                expenseCategoryDTO.setName(expense.getCategory().getName());
            }
            expenseDTO.setExpenseCategoryDTO(expenseCategoryDTO);

            expenseDTOList.add(expenseDTO);
        }

        for (Income income : incomeList) {
            IncomeDTO incomeDTO = new IncomeDTO(income.getSource(), income.getAmount(), income.getDate());
            incomeDTOList.add(incomeDTO);
        }

        double totalExpense = expenseList.stream().mapToDouble(Expense::getAmount).sum();
        double totalIncome = incomeList.stream().mapToDouble(Income::getAmount).sum();

        IncomeAndExpenseDTO incomeAndExpenseDTO = new IncomeAndExpenseDTO();
        incomeAndExpenseDTO.setTotalIncome(totalIncome);
        incomeAndExpenseDTO.setTotalExpense(totalExpense);
        incomeAndExpenseDTO.setIncomeDTOList(incomeDTOList);
        incomeAndExpenseDTO.setExpenseDTOList(expenseDTOList);

        return incomeAndExpenseDTO;
    }

    // 6. Expenses Percentage by Category
    public Map<String,Float> getExpensesPercentageByCategory(String userEmail) throws FinanceManagementSystemException{
        User user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new FinanceManagementSystemException("User does not exist with email: " + userEmail);
        List<Expense> expenses = user.getExpenses();
        float totalExpense = (float) expenses.stream().mapToDouble(Expense::getAmount).sum();

        Map<String, Float> categoryPercentage = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getName(),
                        Collectors.collectingAndThen(
                                Collectors.summingDouble(Expense::getAmount),
                                sum -> (float) ((sum / totalExpense) * 100)
                        )
                ));
        return categoryPercentage;
    }

    // 7. Dashboard
    @Override
    public DashboardDTO getDashboardStats(String userEmail) throws FinanceManagementSystemException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) throw new FinanceManagementSystemException("User not found");

        List<Income> incomes = Optional.ofNullable(user.getIncomes()).orElse(Collections.emptyList());
        List<Expense> expenses = Optional.ofNullable(user.getExpenses()).orElse(Collections.emptyList());
        List<Goal> goals = Optional.ofNullable(user.getGoals()).orElse(Collections.emptyList());

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double balance = totalIncome - totalExpenses;

        Map<String, Float> expensesByCategory = totalExpenses == 0 ? new HashMap<>() :
                expenses.stream()
                        .filter(e -> e.getCategory() != null)
                        .collect(Collectors.groupingBy(
                                e -> e.getCategory().getName(),
                                Collectors.summingDouble(Expense::getAmount)))
                        .entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> (float) ((e.getValue() / totalExpenses) * 100)
                        ));

        int activeGoals = (int) goals.stream().filter(g -> "active".equalsIgnoreCase(g.getStatus())).count();
        int completedGoals = (int) goals.stream().filter(g -> "completed".equalsIgnoreCase(g.getStatus())).count();

        List<GoalDTO> recentGoals = goals.stream()
                .sorted(Comparator.comparing(Goal::getTargetDate).reversed())
                .limit(3)
                .map(g -> {
                    GoalDTO dto = new GoalDTO();
                    dto.setName(g.getName());
                    dto.setTargetAmount(g.getTargetAmount());
                    dto.setTargetDate(g.getTargetDate());
                    dto.setStatus(g.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());

        List<ExpenseDTO> recentExpenses = expenses.stream()
                .filter(e -> e.getDate() != null)
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .limit(5)
                .map(e -> {
                    ExpenseDTO dto = new ExpenseDTO();
                    dto.setName(e.getName());
                    dto.setAmount(e.getAmount());
                    dto.setDate(e.getDate());
                    ExpenseCategoryDTO expenseCategoryDTO = e.getCategory() != null ? new ExpenseCategoryDTO(e.getCategory().getName()) : new ExpenseCategoryDTO();
                    dto.setExpenseCategoryDTO(expenseCategoryDTO);
                    return dto;
                })
                .collect(Collectors.toList());

        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setTotalIncome(totalIncome);
        dashboard.setTotalExpenses(totalExpenses);
        dashboard.setBalance(balance);
        dashboard.setExpensesByCategory(expensesByCategory);
        dashboard.setActiveGoalsCount(activeGoals);
        dashboard.setCompletedGoalsCount(completedGoals);
        dashboard.setRecentGoals(recentGoals);
        dashboard.setRecentExpenses(recentExpenses);

        return dashboard;
    }

}

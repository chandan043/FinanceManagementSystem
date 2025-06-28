package com.gl.fms.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class DashboardDTO {

    @NotNull(message = "The TotalIncome should not be NULL!")
    private Double totalIncome;
    @NotNull(message = "The TotalExpense should not be NULL!")
    private Double totalExpenses;
    @NotNull(message = "The balance should not be NULL!")
    private Double balance;
    @NotNull(message = "The ExpenseByCategory can not be NULL")
    private Map<String, Float> expensesByCategory;
    @NotBlank(message = "The activeGoalsCount should not be NULL/BlANK!")
    private int activeGoalsCount;
    @PositiveOrZero(message = "The completedGoalsCount must be zero or positive!")
    private int completedGoalsCount;
    @NotNull(message = "The recentGoals can not be NULL")
    private List<GoalDTO> recentGoals;
    @NotNull(message = "The recentExpenses can not be NULL")
    private List<ExpenseDTO> recentExpenses;
}

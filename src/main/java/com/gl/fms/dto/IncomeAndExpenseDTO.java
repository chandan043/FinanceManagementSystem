package com.gl.fms.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IncomeAndExpenseDTO {
    @NotNull(message = "The totalIncome Should not be NULL/BLANK!")
    double totalIncome;
    @NotNull(message = "The TotalExpense Should not be NULL/BLANK!")
    double totalExpense;
    @Valid
    @NotNull(message="expenseDTOList should not be Null!")
    List<ExpenseDTO> expenseDTOList;
    @Valid
    @NotNull(message="incomeDTOList should not be Null!")
    List<IncomeDTO> incomeDTOList;
}

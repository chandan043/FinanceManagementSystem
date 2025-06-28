package com.gl.fms.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseDTO {
    private Long id;
    @NotBlank(message = "The name Should not be NULL/BLANK!")
    private String name;
    @NotNull(message = "The amount Should not be NULL!")
    private Double amount;

    @PastOrPresent(message = "The Expense date cannot be in the future!")
    @NotNull(message = "The date should not be null!")
    private LocalDate date;


    @Valid
    @NotNull(message="expenseCategoryDTO should not be Null!")
    private ExpenseCategoryDTO expenseCategoryDTO;

    public ExpenseDTO(String name,Double amount,LocalDate date){
        this.name=name;
        this.amount=amount;
        this.date=date;
    }
}


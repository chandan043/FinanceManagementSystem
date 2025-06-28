package com.gl.fms.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    @NotBlank(message = "The Name Should not be NULL/BLANK!")
    private String name;
    @Email(message = "The Email should be in valid Formate")
    private String email;

    @Valid
    @NotNull(message="goalDTOS should not be Null!")
    private List<GoalDTO> goalDTOS;
    @Valid
    @NotNull(message="incomeDTOS should not be Null!")
    private List<IncomeDTO> incomeDTOS;
    @Valid
    @NotNull(message="expenseDTOS should not be Null!")
    private List<ExpenseDTO> expenseDTOS;
    @Valid
    @NotNull(message="dashboardDTO should not be Null!")
    private DashboardDTO dashboardDTO;
}


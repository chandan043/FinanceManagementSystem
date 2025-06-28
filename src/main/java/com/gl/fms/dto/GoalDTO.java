package com.gl.fms.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GoalDTO {

    private Integer id;
    @NotBlank(message = "The name Should not be NULL/BLANK!")
    private String name;
    @NotBlank(message = "The status Should not be NULL/BLANK!")
    private String status;
    @NotNull(message = "The targetAmount Should not be NULL!")
    private Double targetAmount;

    @Future(message = "the Goal Target Date should be in Future")
    @NotNull(message = "The targetDate Should not be NULL!")
    private LocalDate targetDate;
}


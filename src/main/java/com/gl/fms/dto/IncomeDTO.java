package com.gl.fms.dto;

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
public class IncomeDTO {

    private Integer id;
    @NotBlank(message = "The source Should not be NULL/BLANK!")
    private String source;
    @NotNull(message = "The amount Should not be NULL/BLANK!")
    private Double amount;
    @PastOrPresent(message = "The income Date should be in present or past")
    @NotNull(message = "The date Should not be NULL/BLANK!")
    private LocalDate date;

    public IncomeDTO(String source, Double amount, LocalDate date) {
        this.source = source;
        this.amount = amount;
        this.date = date;
    }

}


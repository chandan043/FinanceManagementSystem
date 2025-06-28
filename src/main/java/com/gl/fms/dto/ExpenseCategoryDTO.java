package com.gl.fms.dto;

import com.gl.fms.entity.Expense;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseCategoryDTO {

    private Integer id;
    @NotBlank(message = "The Name Should not be NULL/BLANK!")
    private String name;
    public ExpenseCategoryDTO(String name){
        this.name=name;
    }
}


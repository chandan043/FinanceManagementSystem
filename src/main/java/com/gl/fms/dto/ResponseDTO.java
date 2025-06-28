package com.gl.fms.dto;

import com.gl.fms.entity.Expense;
import com.gl.fms.entity.Income;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDTO {
    @NotBlank(message = "The message Should not be NULL/BLANK!")
    private String message;
    public ResponseDTO(String message) {
        this.message = message;
    }

}

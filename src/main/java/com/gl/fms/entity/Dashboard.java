package com.gl.fms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dashboard")
@Getter
@Setter
@NoArgsConstructor
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "total_income", nullable = false)
    private Double totalIncome;

    @Column(name = "total_expense", nullable = false)
    private Double totalExpense;

    @Column(name = "balance", nullable = false)
    private Double balance;

}

package com.gl.fms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id") // unidirectional FK to expense table
    private List<Expense> expenses;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id") // unidirectional FK to income table
    private List<Income> incomes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id") // unidirectional FK to goal table
    private List<Goal> goals;

    @OneToOne
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;
}

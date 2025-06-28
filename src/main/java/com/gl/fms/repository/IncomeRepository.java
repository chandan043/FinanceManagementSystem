package com.gl.fms.repository;

import com.gl.fms.dto.IncomeDTO;
import com.gl.fms.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income,Integer> {
//    List<Income> findByIdAndDateBetween(Integer userId, LocalDateTime localDateTime, LocalDateTime localDateTime1);

//    List<Income> findByUser(Integer userId);
}

package com.gl.fms.repository;

import com.gl.fms.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalRepository  extends JpaRepository<Goal,Integer> {
    Optional<Goal> findByName(String name);

//    List<GoalProgressDTO> getProgressForUser(Integer userId);
}

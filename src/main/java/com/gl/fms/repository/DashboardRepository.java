package com.gl.fms.repository;

import com.gl.fms.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardRepository extends JpaRepository<Dashboard, Integer> {
}
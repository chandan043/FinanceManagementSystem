package com.gl.fms.repository;

import com.gl.fms.dto.UserDTO;
import com.gl.fms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByEmail(String email);
}

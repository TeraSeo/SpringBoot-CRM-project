package com.example.demo.Repository;

import com.example.demo.entity.AM_SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AMRepository extends JpaRepository<AM_SignUp, Long> {
}

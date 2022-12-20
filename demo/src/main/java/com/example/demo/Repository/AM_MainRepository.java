package com.example.demo.Repository;

import com.example.demo.entity.AM_Main;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AM_MainRepository extends JpaRepository<AM_Main, Long> {

    @Modifying
    @Query(value = "ALTER TABLE main AUTO_INCREMENT = 1;", nativeQuery = true)
    void restartSeq();

    List<AM_Main> findByProviderContains(String provider);

    List<AM_Main> findByCustomerContains(String company);

    List<AM_Main> findByProviderAndCustomerContains(String provider, String company);

}

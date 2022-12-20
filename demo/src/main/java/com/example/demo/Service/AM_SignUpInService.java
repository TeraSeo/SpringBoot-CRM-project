package com.example.demo.Service;

import com.example.demo.Dto.AM_SignUpDto;
import com.example.demo.entity.AM_SignUp;

import java.util.List;

public interface AM_SignUpInService {

    String get_Id(Long id);

    boolean login(String loginId, String loginPw);

    String get_Name(Long id);

    AM_SignUp signUp(AM_SignUpDto am_signUpDto);

    boolean getLogined();

    void setLogined(boolean logined);

    List<AM_SignUp> findAll();

    void setAllowance(Long id, boolean allowance);

}

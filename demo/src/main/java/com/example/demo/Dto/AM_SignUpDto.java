package com.example.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AM_SignUpDto {

    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;

}

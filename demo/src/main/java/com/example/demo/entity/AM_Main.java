package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Table(name = "Main")
public class AM_Main {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sector;
    private String provider;
    private String installDate;
    private String serviceNum;
    private String customer;
    private String notes;
    private String ip;
    private String modelType;
    private String serialNum;
    private String version;
    private String account;
    private String ddns;
    private String address;
    private String contractType;
    private String license;
    private String marker;
    private String respon;
    private String recent;
    private String error;
    private String etc;

}

package com.example.demo.entity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "SignUp")
public class AM_SignUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // id 를 auto increment 해준다
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ColumnDefault("false")
    private boolean allowance;

    public boolean getAllowance() {
        return this.allowance;
    }

    public void setAllowance() {
        this.allowance = allowance;
    }

}

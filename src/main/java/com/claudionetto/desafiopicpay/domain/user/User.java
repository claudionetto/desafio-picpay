package com.claudionetto.desafiopicpay.domain.user;

import com.claudionetto.desafiopicpay.dto.UserCreateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String document;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private BigDecimal balance;

    public User(UserCreateDTO userCreateDTO){

        this.firstName = userCreateDTO.firstName();
        this.lastName = userCreateDTO.lastName();
        this.email = userCreateDTO.email();
        this.document = userCreateDTO.document();
        this.password = userCreateDTO.password();
        this.userType = userCreateDTO.userType();
        this.balance = userCreateDTO.balance();

    }

}

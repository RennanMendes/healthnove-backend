package com.healthnove.schedulingHealthNove.domain.model;

import com.healthnove.schedulingHealthNove.domain.dto.UserRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.UserUpdateDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Gender;
import com.healthnove.schedulingHealthNove.domain.enumerated.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String cpf;
    private String phone;
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;
    private boolean active;

    public User(UserRequestDto userData) {
        this.firstName = userData.name();
        this.lastName = userData.lastName();
        this.cpf = userData.cpf();
        this.phone = userData.phone();
        this.birthDate = userData.brithDate();
        this.gender = userData.gender();
        this.email = userData.email();
        this.password = userData.password();

        this.userType = UserType.PATIENT;
        this.active = true;
    }

    public void update(UserUpdateDto userData) {
        this.firstName = userData.firstName();
        this.lastName = userData.lastName();
        this.phone = userData.phone();
        this.email = userData.email();
    }

    public void delete() {
        this.active = false;
    }
}

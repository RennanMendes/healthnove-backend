package com.healthnove.schedulingHealthNove.domain.model;

import com.healthnove.schedulingHealthNove.domain.dto.user.UserRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.user.UserUpdateDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Gender;
import com.healthnove.schedulingHealthNove.domain.enumerated.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

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
        this.birthDate = userData.birthDate();
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

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void delete() {
        this.active = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

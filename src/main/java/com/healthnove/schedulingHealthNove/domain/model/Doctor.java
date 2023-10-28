package com.healthnove.schedulingHealthNove.domain.model;

import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_doctor")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String crm;

    @Enumerated(EnumType.STRING)
    private Speciality speciality;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

}

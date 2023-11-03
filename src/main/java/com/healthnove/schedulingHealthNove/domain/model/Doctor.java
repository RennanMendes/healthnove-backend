package com.healthnove.schedulingHealthNove.domain.model;

import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorRequestDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import jakarta.persistence.*;
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
    private Long id;

    @Column(unique = true)
    private String crm;

    @Enumerated(EnumType.STRING)
    private Speciality speciality;
    private boolean active;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    public Doctor(User user, DoctorRequestDto requestDto) {
        this.id = user.getId();
        this.crm = requestDto.crm();
        this.speciality = requestDto.speciality();
        this.active = true;
        this.user = user;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

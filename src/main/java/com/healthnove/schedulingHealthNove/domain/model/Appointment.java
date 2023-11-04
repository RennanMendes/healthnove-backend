package com.healthnove.schedulingHealthNove.domain.model;

import com.healthnove.schedulingHealthNove.domain.enumerated.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_appointment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_date")
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    public Appointment(LocalDateTime date, User user, Doctor doctor) {
        this.date = date;
        this.doctor = doctor;
        this.user = user;
        this.status = Status.SCHEDULED;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
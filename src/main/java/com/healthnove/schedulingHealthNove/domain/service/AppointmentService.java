package com.healthnove.schedulingHealthNove.domain.service;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentResponseDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Status;
import com.healthnove.schedulingHealthNove.domain.exception.UnscheduledAppointmentException;
import com.healthnove.schedulingHealthNove.domain.model.Appointment;
import com.healthnove.schedulingHealthNove.domain.model.Doctor;
import com.healthnove.schedulingHealthNove.domain.model.User;
import com.healthnove.schedulingHealthNove.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
public class AppointmentService {

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentRepository repository;

    public AppointmentResponseDto findAppointmentById(Long id) {
        return new AppointmentResponseDto(this.findById(id));
    }

    public Stream<AppointmentResponseDto> findByUserId(Long id) {
        List<Appointment> appointments = repository.findByUserIdAndStatus(id, Status.SCHEDULED);
        return appointments.stream().map(AppointmentResponseDto::new);
    }

    public Stream<AppointmentResponseDto> findByDoctorId(Long id) {
        List<Appointment> appointments = repository.findByDoctorIdAndStatus(id, Status.SCHEDULED);
        return appointments.stream().map(AppointmentResponseDto::new);
    }

    public AppointmentResponseDto scheduleAppointment(AppointmentRequestDto requestDto) {
        User user = userService.findByIdAndActiveTrue(requestDto.userId());
        Doctor doctor = doctorService.findDoctorById(requestDto.doctorId());

        Appointment appointment = new Appointment(requestDto.appointmentDate(), user, doctor);

        return new AppointmentResponseDto(repository.save(appointment));
    }

    @Transactional
    public void cancelAppointment(Long id) {
        Appointment appointment = this.findById(id);
        appointment.setStatus(Status.CANCELED);
    }

    private Appointment findById(Long id) {
        return repository.findByIdAndStatus(id, Status.SCHEDULED).orElseThrow(UnscheduledAppointmentException::new);
    }

}

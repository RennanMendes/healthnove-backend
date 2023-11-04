package com.healthnove.schedulingHealthNove.api.controller;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentResponseDto;
import com.healthnove.schedulingHealthNove.domain.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Stream;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findAppointmentById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Stream<AppointmentResponseDto>> findByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findByUserId(id));
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<Stream<AppointmentResponseDto>> findByDoctorId(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findByDoctorId(id));
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> scheduleAppointment(@RequestBody @Valid AppointmentRequestDto requestDto, UriComponentsBuilder uriBuilder){
        AppointmentResponseDto responseDto = appointmentService.scheduleAppointment(requestDto);
        URI uri = uriBuilder.path("/appointments/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Stream<AppointmentResponseDto>> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

}

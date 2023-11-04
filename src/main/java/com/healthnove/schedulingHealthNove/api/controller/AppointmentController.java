package com.healthnove.schedulingHealthNove.api.controller;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentResponseDto;
import com.healthnove.schedulingHealthNove.domain.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Stream;

@RestController
@RequestMapping("/appointments")
@SecurityRequirement(name = "bearer-key")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @GetMapping("/{id}")
    @Operation(summary = "Busca consulta por id")
    public ResponseEntity<AppointmentResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findAppointmentById(id));
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Busca consultas por usuário")
    public ResponseEntity<Stream<AppointmentResponseDto>> findByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findByUserId(id));
    }

    @GetMapping("/doctor/{id}")
    @Operation(summary = "Busca consultas por médico")
    public ResponseEntity<Stream<AppointmentResponseDto>> findByDoctorId(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findByDoctorId(id));
    }

    @PostMapping
    @Operation(summary = "Agenda nova consulta")
    public ResponseEntity<AppointmentResponseDto> scheduleAppointment(@RequestBody @Valid AppointmentRequestDto requestDto, UriComponentsBuilder uriBuilder){
        AppointmentResponseDto responseDto = appointmentService.scheduleAppointment(requestDto);
        URI uri = uriBuilder.path("/appointments/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Cancela consulta")
    public ResponseEntity<Stream<AppointmentResponseDto>> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

}

package com.healthnove.schedulingHealthNove.api.controller;

import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorUpdateDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import com.healthnove.schedulingHealthNove.domain.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/doctors")
@SecurityRequirement(name = "bearer-key")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    @Operation(summary = "Lista todos os médicos")
    public ResponseEntity<Page<DoctorResponseDto>> findAll(Pageable page) {
        return ResponseEntity.ok(doctorService.findAll(page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca médico por id")
    public ResponseEntity<DoctorResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.findById(id));
    }

    @GetMapping("/specialties/{speciality}")
    @Operation(summary = "Busca médico por especialidade")
    public ResponseEntity<Page<DoctorResponseDto>> findBySpeciality(Pageable page, @PathVariable Speciality speciality) {
        return ResponseEntity.ok(doctorService.findBySpeciality(page, speciality));
    }

    @PostMapping("/{id}")
    @Operation(summary = "Cadastra médico por id de usuário")
    public ResponseEntity<DoctorResponseDto> create(@PathVariable Long id, @Valid @RequestBody DoctorRequestDto requestDto, UriComponentsBuilder uriBuilder) {
        DoctorResponseDto responseDto = doctorService.create(id, requestDto);
        URI uri = uriBuilder.path("/doctors/{id}").buildAndExpand(responseDto.id()).toUri();

        return ResponseEntity.created(uri).body(responseDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza médico por id")
    public ResponseEntity<DoctorResponseDto> update(@PathVariable Long id, @Valid @RequestBody DoctorUpdateDto requestDto) {
        return ResponseEntity.ok().body(doctorService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui médico por id")
    public ResponseEntity delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

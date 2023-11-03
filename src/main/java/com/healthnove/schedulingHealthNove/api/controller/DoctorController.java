package com.healthnove.schedulingHealthNove.api.controller;

import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorUpdateDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import com.healthnove.schedulingHealthNove.domain.service.DoctorService;
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
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<Page<DoctorResponseDto>> findAll(Pageable page) {
        return ResponseEntity.ok(doctorService.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.findById(id));
    }

    @GetMapping("/specialties/{speciality}")
    public ResponseEntity<Page<DoctorResponseDto>> findBySpeciality(Pageable page, @PathVariable Speciality speciality) {
        return ResponseEntity.ok(doctorService.findBySpeciality(page, speciality));
    }

    @PostMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> create(@PathVariable Long id, @Valid @RequestBody DoctorRequestDto requestDto, UriComponentsBuilder uriBuilder) {
        DoctorResponseDto responseDto = doctorService.create(id, requestDto);
        URI uri = uriBuilder.path("/doctors/{id}").buildAndExpand(responseDto.id()).toUri();

        return ResponseEntity.created(uri).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> update(@PathVariable Long id, @Valid @RequestBody DoctorUpdateDto requestDto) {
        return ResponseEntity.ok().body(doctorService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

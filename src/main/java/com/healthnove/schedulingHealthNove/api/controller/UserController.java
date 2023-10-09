package com.healthnove.schedulingHealthNove.api.controller;

import com.healthnove.schedulingHealthNove.domain.dto.UserRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.UserResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.UserUpdateDto;
import com.healthnove.schedulingHealthNove.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> findAll(Pageable page){
        Page<UserResponseDto> userResponse = userService.findAll(page);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<UserResponseDto> findById(@PathVariable Long id){
        UserResponseDto userResponse = userService.findById(id);
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserRequestDto userData, UriComponentsBuilder uriBuilder){
        UserResponseDto userResponse =  userService.create(userData);
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(userResponse.id()).toUri();
        return  ResponseEntity.created(uri).body(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto){
        UserResponseDto userResponse = userService.update(id,userUpdateDto);
        return ResponseEntity.ok().body(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

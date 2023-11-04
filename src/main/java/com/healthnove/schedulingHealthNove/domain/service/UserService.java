package com.healthnove.schedulingHealthNove.domain.service;

import com.healthnove.schedulingHealthNove.domain.dto.user.UserRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.user.UserResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.user.UserUpdateDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.UserType;
import com.healthnove.schedulingHealthNove.domain.exception.UserNotFoundException;
import com.healthnove.schedulingHealthNove.domain.model.User;
import com.healthnove.schedulingHealthNove.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<UserResponseDto> findAll(Pageable page) {
        Page<User> users = repository.findByActiveTrue(page);
        return users.map(UserResponseDto::new);
    }

    public UserResponseDto findById(Long id) {
        return new UserResponseDto(this.findByIdAndActiveTrue(id));
    }

    public UserResponseDto create(UserRequestDto userData) {
        User user = new User(userData);
        user.setPassword(encryptPassword(userData.password()));
        return new UserResponseDto(repository.save(user));
    }

    @Transactional
    public UserResponseDto update(Long id, UserUpdateDto userData) {
        User user = this.findByIdAndActiveTrue(id);
        user.update(userData);

        return new UserResponseDto(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = this.findByIdAndActiveTrue(id);
        user.delete();
    }

    public User findByIdAndActiveTrue(Long id) {
        return repository.findByIdAndActiveTrue(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User setUserAsDoctor(Long id) {
        User user = this.findByIdAndActiveTrue(id);
        user.setUserType(UserType.DOCTOR);
        return user;
    }

    @Transactional
    public User setUserAsPatient(Long id) {
        User user = this.findByIdAndActiveTrue(id);
        user.setUserType(UserType.PATIENT);
        return user;
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
package com.healthnove.schedulingHealthNove.domain.repository;

import com.healthnove.schedulingHealthNove.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndActiveIsTrue(String email);
    Optional<User> findByIdAndActiveTrue(Long id);
    Page<User> findByActiveTrue(Pageable page);
}

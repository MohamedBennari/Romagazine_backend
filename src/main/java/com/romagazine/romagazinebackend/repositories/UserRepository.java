package com.romagazine.romagazinebackend.repositories;

import com.romagazine.romagazinebackend.entities.User;
import com.romagazine.romagazinebackend.enums.Role;
import com.romagazine.romagazinebackend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByVerificationToken(String token);
    Optional<User> findByResetToken(String token);
    List<User> findByRole(Role role);
    List<User> findByStatus(Status status);
}
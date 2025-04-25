package com.romagazine.romagazinebackend.repositories;

import com.romagazine.romagazinebackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional findByEmail(String email);
    Optional findByUsername(String username);
}
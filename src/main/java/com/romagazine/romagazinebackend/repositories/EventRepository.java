package com.romagazine.romagazinebackend.repositories;

import com.romagazine.romagazinebackend.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime; import java.util.List;

@RepositoryRestResource(exported = false)
public interface EventRepository extends JpaRepository<Event, Long> {
    List findByLocation(String location);
    List findByStartDateBetween(LocalDateTime start, LocalDateTime end);
    List findByBannerTrue(); List findByIsActiveTrue();
}
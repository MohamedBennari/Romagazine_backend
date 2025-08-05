package com.romagazine.romagazinebackend.repositories;

import com.romagazine.romagazinebackend.entities.PinnedEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface PinnedEventsRepository extends JpaRepository<PinnedEvents, Long> {
    List<PinnedEvents> findByOrderByPositionAsc();
    Optional<PinnedEvents> findByEventId(Long eventId);
    Optional<PinnedEvents> findByPosition(int position);
}
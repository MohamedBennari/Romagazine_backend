package com.romagazine.romagazinebackend.services;

import com.romagazine.romagazinebackend.entities.PinnedEvents;
import com.romagazine.romagazinebackend.repositories.PinnedEventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PinnedEventsService {

    @Autowired
    private PinnedEventsRepository pinnedEventsRepository;

    @Transactional(readOnly = true)
    public List<PinnedEvents> getAllPinnedEvents() {
        return pinnedEventsRepository.findByOrderByPositionAsc();
    }

    @Transactional(readOnly = true)
    public Optional<PinnedEvents> getPinnedEventById(Long id) {
        return pinnedEventsRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<PinnedEvents> getPinnedEventByEventId(Long eventId) {
        return pinnedEventsRepository.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public Optional<PinnedEvents> getPinnedEventByPosition(int position) {
        return pinnedEventsRepository.findByPosition(position);
    }

    @Transactional
    public PinnedEvents createPinnedEvent(PinnedEvents pinnedEvent) {
        // Ensure position is unique
        Optional<PinnedEvents> existing = pinnedEventsRepository.findByPosition(pinnedEvent.getPosition());
        if (existing.isPresent()) {
            throw new RuntimeException("Position " + pinnedEvent.getPosition() + " is already taken.");
        }
        return pinnedEventsRepository.save(pinnedEvent);
    }

    @Transactional
    public PinnedEvents updatePinnedEvent(Long id, PinnedEvents pinnedEventDetails) {
        PinnedEvents pinnedEvent = pinnedEventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pinned event not found with id " + id));
        // Ensure position is unique if it’s being changed
        if (pinnedEvent.getPosition() != pinnedEventDetails.getPosition()) {
            Optional<PinnedEvents> existing = pinnedEventsRepository.findByPosition(pinnedEventDetails.getPosition());
            if (existing.isPresent()) {
                throw new RuntimeException("Position " + pinnedEventDetails.getPosition() + " is already taken.");
            }
        }
        pinnedEvent.setEvent(pinnedEventDetails.getEvent());
        pinnedEvent.setPosition(pinnedEventDetails.getPosition());
        return pinnedEventsRepository.save(pinnedEvent);
    }

    @Transactional
    public void deletePinnedEvent(Long id) {
        PinnedEvents pinnedEvent = pinnedEventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pinned event not found with id " + id));
        pinnedEventsRepository.delete(pinnedEvent);
    }

}
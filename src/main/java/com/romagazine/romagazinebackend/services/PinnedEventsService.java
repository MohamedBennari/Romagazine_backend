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
        // If another event is already pinned at this position, delete it
        pinnedEventsRepository.findByPosition(pinnedEvent.getPosition())
                .ifPresent(existing -> pinnedEventsRepository.delete(existing));

        // If this event is already pinned elsewhere, delete it too
        pinnedEventsRepository.findByEventId(pinnedEvent.getEvent().getId())
                .ifPresent(existing -> pinnedEventsRepository.delete(existing));

        return pinnedEventsRepository.save(pinnedEvent);
    }

    @Transactional
    public PinnedEvents updatePinnedEvent(Long id, PinnedEvents pinnedEventDetails) {
        PinnedEvents existing = pinnedEventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pinned event not found with id " + id));

        // If position is changed and already taken by another, delete the other
        pinnedEventsRepository.findByPosition(pinnedEventDetails.getPosition()).ifPresent(other -> {
            if (!other.getId().equals(id)) {
                pinnedEventsRepository.delete(other);
            }
        });

        // If event is changed and already pinned elsewhere, delete the other
        pinnedEventsRepository.findByEventId(pinnedEventDetails.getEvent().getId()).ifPresent(other -> {
            if (!other.getId().equals(id)) {
                pinnedEventsRepository.delete(other);
            }
        });

        existing.setEvent(pinnedEventDetails.getEvent());
        existing.setPosition(pinnedEventDetails.getPosition());
        return pinnedEventsRepository.save(existing);
    }

    @Transactional
    public void deletePinnedEvent(Long id) {
        PinnedEvents pinnedEvent = pinnedEventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pinned event not found with id " + id));
        pinnedEventsRepository.delete(pinnedEvent);
    }
}
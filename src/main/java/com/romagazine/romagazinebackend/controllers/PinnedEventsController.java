package com.romagazine.romagazinebackend.controllers;

import com.romagazine.romagazinebackend.entities.PinnedEvents;
import com.romagazine.romagazinebackend.services.PinnedEventsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pinned-events")
public class PinnedEventsController {

    @Autowired
    private PinnedEventsService pinnedEventsService;

    @GetMapping
    public List<PinnedEvents> getAllPinnedEvents() {
        return pinnedEventsService.getAllPinnedEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PinnedEvents> getPinnedEventById(@PathVariable Long id) {
        Optional<PinnedEvents> pinnedEvent = pinnedEventsService.getPinnedEventById(id);
        return pinnedEvent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/event/{eventId}")
    public ResponseEntity<PinnedEvents> getPinnedEventByEventId(@PathVariable Long eventId) {
        Optional<PinnedEvents> pinnedEvent = pinnedEventsService.getPinnedEventByEventId(eventId);
        return pinnedEvent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/position/{position}")
    public ResponseEntity<PinnedEvents> getPinnedEventByPosition(@PathVariable int position) {
        Optional<PinnedEvents> pinnedEvent = pinnedEventsService.getPinnedEventByPosition(position);
        return pinnedEvent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PinnedEvents> createPinnedEvent(@Valid @RequestBody PinnedEvents pinnedEvent) {
        PinnedEvents savedPinnedEvent = pinnedEventsService.createPinnedEvent(pinnedEvent);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPinnedEvent.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedPinnedEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PinnedEvents> updatePinnedEvent(@PathVariable Long id, @Valid @RequestBody PinnedEvents pinnedEvent) {
        PinnedEvents updatedPinnedEvent = pinnedEventsService.updatePinnedEvent(id, pinnedEvent);
        return ResponseEntity.ok(updatedPinnedEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePinnedEvent(@PathVariable Long id) {
        pinnedEventsService.deletePinnedEvent(id);
        return ResponseEntity.noContent().build();
    }

}
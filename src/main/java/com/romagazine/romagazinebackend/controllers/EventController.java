package com.romagazine.romagazinebackend.controllers;

import com.romagazine.romagazinebackend.entities.Event;
import com.romagazine.romagazinebackend.entities.User;
import com.romagazine.romagazinebackend.services.EventService;
import com.romagazine.romagazinebackend.services.FileStorageService;
import com.romagazine.romagazinebackend.services.UserService;
import com.romagazine.romagazinebackend.utils.ImageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;


    @GetMapping("/active")
    public List<Event> getAllactiveEvents() {
        return eventService.getAllactiveEvents();
    }


    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventService.getEventById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/location/{location}")
    public List<Event> getEventsByLocation(@PathVariable String location) {
        return eventService.getEventsByLocation(location);
    }


    @GetMapping("/date-range")
    public List<Event> getEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return eventService.getEventsByDateRange(start, end);
    }


    @GetMapping("/banner")
    public List<Event> getBannerEvents() {
        return eventService.getBannerEvents();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        Event savedEvent = eventService.createEvent(event);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEvent.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event event) {
        Event updatedEvent = eventService.updateEvent(id, event);
        return ResponseEntity.ok(updatedEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{eventId}/interested/{userId}")
    public ResponseEntity<Event> addInterestedUser(@PathVariable Long eventId, @PathVariable Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        Event updatedEvent = eventService.addInterestedUser(eventId, user);
        return ResponseEntity.ok(updatedEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{eventId}/interested/{userId}")
    public ResponseEntity<Event> removeInterestedUser(@PathVariable Long eventId, @PathVariable Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        Event updatedEvent = eventService.removeInterestedUser(eventId, user);
        return ResponseEntity.ok(updatedEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Upload event image", description = "Upload an image for a specific event (main, second, or third image)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Event.class))),
        @ApiResponse(responseCode = "400", description = "Invalid image file or image type"),
        @ApiResponse(responseCode = "404", description = "Event not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Event> uploadEventImage(
            @Parameter(description = "Event ID", required = true) @PathVariable Long id,
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Type of image (main/second/third)", required = true)
            @RequestParam("imageType") String imageType) {
        try {
            if (!ImageUtils.isValidImage(file)) {
                return ResponseEntity.badRequest().build();
            }

            String filename = fileStorageService.storeFile(file);
            String imageUrl = ImageUtils.generateImageUrl(filename);

            Event event = eventService.getEventById(id)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            switch (imageType.toLowerCase()) {
                case "main" -> event.setImage(imageUrl);
                case "second" -> event.setSecondImage(imageUrl);
                case "third" -> event.setThirdImage(imageUrl);
                default -> throw new RuntimeException("Invalid image type");
            }

            Event updatedEvent = eventService.updateEvent(id, event);
            return ResponseEntity.ok(updatedEvent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
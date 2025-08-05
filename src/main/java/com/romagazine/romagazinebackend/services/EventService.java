package com.romagazine.romagazinebackend.services;

import com.romagazine.romagazinebackend.entities.Artist;
import com.romagazine.romagazinebackend.entities.Event;
import com.romagazine.romagazinebackend.entities.User;
import com.romagazine.romagazinebackend.repositories.ArtistRepository;
import com.romagazine.romagazinebackend.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ArtistRepository artistRepository; // Ajoute l'import et l'injection


    public List<Event> getAllactiveEvents() {
        return eventRepository.findByIsActiveTrue();
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getEventsByLocation(String location) {
        return eventRepository.findByLocation(location);
    }

    public List<Event> getEventsByDateRange(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByStartDateBetween(start, end);
    }

    public List<Event> getBannerEvents() {
        return eventRepository.findByBannerTrue();
    }

    public Event createEvent(Event event) {
        event.setCreatedAt(LocalDateTime.now());
        // If this event is set as banner, remove banner from all others
        if (event.isBanner()) {
            List<Event> bannerEvents = eventRepository.findByBannerTrue();
            for (Event e : bannerEvents) {
                e.setBanner(false);
            }
            eventRepository.saveAll(bannerEvents);
        }

        // Recharge la lineup depuis la base
        List<Artist> managedLineup = new ArrayList<>();
        if (event.getLineup() != null) {
            for (Artist a : event.getLineup()) {
                Artist managedArtist = artistRepository.findById(a.getId())
                        .orElseThrow(() -> new RuntimeException("Artist not found: " + a.getId()));
                managedLineup.add(managedArtist);
            }
        }
        event.setLineup(managedLineup);

        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));

        // If this event is set as banner, remove banner from all others
        if (eventDetails.isBanner()) {
            List<Event> bannerEvents = eventRepository.findByBannerTrue();
            for (Event e : bannerEvents) {
                if (!e.getId().equals(id)) {
                    e.setBanner(false);
                }
            }
            eventRepository.saveAll(bannerEvents);
        }

        event.setName(eventDetails.getName());
        event.setDescription(eventDetails.getDescription());
        event.setImage(eventDetails.getImage());
        event.setSecondImage(eventDetails.getSecondImage());
        event.setThirdImage(eventDetails.getThirdImage());
        event.setVideoLink(eventDetails.getVideoLink());
        event.setStartDate(eventDetails.getStartDate());
        event.setEndDate(eventDetails.getEndDate());
        event.setLocation(eventDetails.getLocation());
        event.setLatitude(eventDetails.getLatitude());
        event.setLongitude(eventDetails.getLongitude());
        event.setBanner(eventDetails.isBanner());
        event.setLineup(eventDetails.getLineup());
        event.setSecondLineup(eventDetails.getSecondLineup());
        event.setInterested(eventDetails.getInterested());
        event.setTicketLink(eventDetails.getTicketLink());
        event.setActive(eventDetails.isActive());
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));
        eventRepository.delete(event);
    }

    public Event addInterestedUser(Long eventId, User user) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + eventId));
        event.getInterested().add(user);
        return eventRepository.save(event);
    }

    public Event removeInterestedUser(Long eventId, User user) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + eventId));
        event.getInterested().remove(user);
        return eventRepository.save(event);
    }

}
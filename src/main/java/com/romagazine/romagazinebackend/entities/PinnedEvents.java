package com.romagazine.romagazinebackend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pinned_events")
@Data
public class PinnedEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(nullable = false, unique = true)
    private int position;

    public PinnedEvents() {
    }

    public PinnedEvents(Long id, Event event, int position) {
        this.id = id;
        this.event = event;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
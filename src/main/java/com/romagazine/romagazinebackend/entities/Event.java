package com.romagazine.romagazinebackend.entities;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String image;

    @Column
    private String secondImage;

    @Column
    private String thirdImage;

    @Column
    private String videoLink;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String location;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(nullable = false)
    private boolean banner = false;

    @ManyToMany
    @JoinTable(
            name = "artist_events",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Artist> lineup = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "event_second_lineup", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "artist_name")
    private List<String> secondLineup = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "event_interested_users",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<User> interested = new ArrayList<>();

    @Column
    private String ticketLink;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean isActive = true;

    public Event() {
    }

    public Event(Long id, String name, String description, String image, String secondImage, String thirdImage, String videoLink, LocalDateTime startDate, LocalDateTime endDate, String location, Double latitude, Double longitude, boolean banner, List<Artist> lineup, List<String> secondLineup, List<User> interested, String ticketLink, LocalDateTime createdAt, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.secondImage = secondImage;
        this.thirdImage = thirdImage;
        this.videoLink = videoLink;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.banner = banner;
        this.lineup = lineup;
        this.secondLineup = secondLineup;
        this.interested = interested;
        this.ticketLink = ticketLink;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSecondImage() {
        return secondImage;
    }

    public void setSecondImage(String secondImage) {
        this.secondImage = secondImage;
    }

    public String getThirdImage() {
        return thirdImage;
    }

    public void setThirdImage(String thirdImage) {
        this.thirdImage = thirdImage;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isBanner() {
        return banner;
    }

    public void setBanner(boolean banner) {
        this.banner = banner;
    }

    public List<Artist> getLineup() {
        return lineup;
    }

    public void setLineup(List<Artist> lineup) {
        this.lineup = lineup;
    }

    public List<String> getSecondLineup() {
        return secondLineup;
    }

    public void setSecondLineup(List<String> secondLineup) {
        this.secondLineup = secondLineup;
    }

    public List<User> getInterested() {
        return interested;
    }

    public void setInterested(List<User> interested) {
        this.interested = interested;
    }

    public String getTicketLink() {
        return ticketLink;
    }

    public void setTicketLink(String ticketLink) {
        this.ticketLink = ticketLink;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", secondImage='" + secondImage + '\'' +
                ", thirdImage='" + thirdImage + '\'' +
                ", videoLink='" + videoLink + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", banner=" + banner +
                ", lineup=" + lineup +
                ", secondLineup=" + secondLineup +
                ", interested=" + interested +
                ", ticketLink='" + ticketLink + '\'' +
                ", createdAt=" + createdAt +
                ", isActive=" + isActive +
                '}';
    }
}

package com.romagazine.romagazinebackend.entities;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Table(name = "releases")
@Data
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id", nullable = false)
    @JsonIgnoreProperties({"events", "user.password", "user.phoneNumber", "user.isActive"})
    private Artist artist;

    @Column(nullable = false)
    private LocalDateTime releaseDate;

    @Column
    private String soundcloudLink;

    @Column
    private String purchaseLink;

    @Column(nullable = false)
    private String type; //EP/ALBUM/LP/SINGLE

    @Column(nullable = false)
    private boolean veilhushRelease = false;

    @Column
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Release() {
    }

    public Release(Long id, String title, Artist artist, LocalDateTime releaseDate, String soundcloudLink, String purchaseLink, String type, boolean veilhushRelease, String image, String description, boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.soundcloudLink = soundcloudLink;
        this.purchaseLink = purchaseLink;
        this.type = type;
        this.veilhushRelease = veilhushRelease;
        this.image = image;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSoundcloudLink() {
        return soundcloudLink;
    }

    public void setSoundcloudLink(String soundcloudLink) {
        this.soundcloudLink = soundcloudLink;
    }

    public String getPurchaseLink() {
        return purchaseLink;
    }

    public void setPurchaseLink(String purchaseLink) {
        this.purchaseLink = purchaseLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVeilhushRelease() {
        return veilhushRelease;
    }

    public void setVeilhushRelease(boolean veilhushRelease) {
        this.veilhushRelease = veilhushRelease;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
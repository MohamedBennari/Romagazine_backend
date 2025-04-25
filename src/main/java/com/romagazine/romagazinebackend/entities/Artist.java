package com.romagazine.romagazinebackend.entities;
import jakarta.persistence.*; import lombok.Data;

import java.time.LocalDateTime; import java.util.ArrayList; import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity @Table(name = "artists")
@Data
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"password", "phoneNumber", "isActive"})
    private User user;

    @Column
    private String name;

    @Column
    private String lastName;

    @Column(nullable = false)
    private String stageName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(nullable = false)
    private boolean veilhushArtist = false;

    @Column
    private String profilePicture;

    @Column
    private String location;

    @Column(nullable = false)
    private int followerCount = 0;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column
    private String soundcloudLink;

    @Column
    private String instagramLink;

    @Column(name = "ra_link")
    @JsonProperty("raLink")
    private String RALink;

    @Column
    private String facebookLink;

    @Column
    private String bandcampLink;

    @ManyToMany
    @JoinTable(
            name = "artist_events",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Artist() {
    }

    public Artist(Long id, User user, String name, String lastName, String stageName, String bio, boolean veilhushArtist, String profilePicture, String location, int followerCount, boolean isActive, String soundcloudLink, String instagramLink, String RALink, String facebookLink, String bandcampLink, List<Event> events, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.lastName = lastName;
        this.stageName = stageName;
        this.bio = bio;
        this.veilhushArtist = veilhushArtist;
        this.profilePicture = profilePicture;
        this.location = location;
        this.followerCount = followerCount;
        this.isActive = isActive;
        this.soundcloudLink = soundcloudLink;
        this.instagramLink = instagramLink;
        this.RALink = RALink;
        this.facebookLink = facebookLink;
        this.bandcampLink = bandcampLink;
        this.events = events;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isVeilhushArtist() {
        return veilhushArtist;
    }

    public void setVeilhushArtist(boolean veilhushArtist) {
        this.veilhushArtist = veilhushArtist;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getSoundcloudLink() {
        return soundcloudLink;
    }

    public void setSoundcloudLink(String soundcloudLink) {
        this.soundcloudLink = soundcloudLink;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    @JsonIgnore
    public String getRALink() {
        return RALink;
    }

    public void setRALink(String RALink) {
        this.RALink = RALink;
    }

    @JsonProperty("raLink")
    public String getRaLink() {
        return RALink;
    }

    @JsonIgnore
    public void setRaLink(String raLink) {
        this.RALink = raLink;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getBandcampLink() {
        return bandcampLink;
    }

    public void setBandcampLink(String bandcampLink) {
        this.bandcampLink = bandcampLink;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", stageName='" + stageName + '\'' +
                ", bio='" + bio + '\'' +
                ", veilhushArtist=" + veilhushArtist +
                ", profilePicture='" + profilePicture + '\'' +
                ", location='" + location + '\'' +
                ", followerCount=" + followerCount +
                ", isActive=" + isActive +
                ", soundcloudLink='" + soundcloudLink + '\'' +
                ", instagramLink='" + instagramLink + '\'' +
                ", RALink='" + RALink + '\'' +
                ", facebookLink='" + facebookLink + '\'' +
                ", bandcampLink='" + bandcampLink + '\'' +
                ", events=" + events +
                ", createdAt=" + createdAt +
                '}';
    }
}

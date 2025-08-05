package com.romagazine.romagazinebackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column
    private com.romagazine.romagazinebackend.enums.Role role;

    @Enumerated(EnumType.STRING)
    @Column
    private com.romagazine.romagazinebackend.enums.Status status;

    @Column
    private String profilePicture;

    @Column
    private String name;

    @Column
    private String lastName;

    @Column
    private String phoneNumber;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    @JsonIgnore
    private String resetToken;

    @Column
    @JsonIgnore
    private LocalDateTime resetTokenExpiry;

    @Column
    @JsonIgnore
    private String verificationToken;

    @Column
    @JsonIgnore
    private LocalDateTime verificationTokenExpiry;

    @ManyToMany(mappedBy = "interested")
    @JsonIgnore
    private List<Event> interestedEvents = new ArrayList<>();

    public User() {
    }

    public User(Long id, String username, String email, String password, com.romagazine.romagazinebackend.enums.Role role, com.romagazine.romagazinebackend.enums.Status status, String profilePicture, String lastName, String name, String phoneNumber, boolean isActive, LocalDateTime createdAt, List<Event> interestedEvents) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.profilePicture = profilePicture;
        this.lastName = lastName;
        this.name= name;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive();
        this.createdAt = createdAt;
        this.interestedEvents = interestedEvents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public com.romagazine.romagazinebackend.enums.Role getRole() {
        return role;
    }

    public void setRole(com.romagazine.romagazinebackend.enums.Role role) {
        this.role = role;
    }

    public com.romagazine.romagazinebackend.enums.Status getStatus() {
        return status;
    }

    public void setStatus(com.romagazine.romagazinebackend.enums.Status status) {
        this.status = status;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Event> getInterestedEvents() {
        return interestedEvents;
    }

    public void setInterestedEvents(List<Event> interestedEvents) {
        this.interestedEvents = interestedEvents;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public LocalDateTime getVerificationTokenExpiry() {
        return verificationTokenExpiry;
    }

    public void setVerificationTokenExpiry(LocalDateTime verificationTokenExpiry) {
        this.verificationTokenExpiry = verificationTokenExpiry;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", active=" + isActive +
                ", createdAt=" + createdAt +
                ", interestedEvents=" + interestedEvents +
                '}';
    }

}

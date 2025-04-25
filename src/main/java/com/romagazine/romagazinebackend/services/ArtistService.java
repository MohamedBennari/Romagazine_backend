package com.romagazine.romagazinebackend.services;

import com.romagazine.romagazinebackend.entities.Artist;
import com.romagazine.romagazinebackend.entities.User;
import com.romagazine.romagazinebackend.repositories.ArtistRepository;
import com.romagazine.romagazinebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }

    public Artist getArtistByStageName(String stageName) {
        return artistRepository.findByStageName(stageName);
    }

    public Artist createArtist(Artist artist) {
        User user = userRepository.findById(artist.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + artist.getUser().getId()));
        artist.setUser(user);
        artist.setCreatedAt(LocalDateTime.now());
        return artistRepository.save(artist);
    }

    public Artist updateArtist(Long id, Artist artistDetails) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id " + id));
        artist.setUser(artistDetails.getUser());
        artist.setName(artistDetails.getName());
        artist.setLastName(artistDetails.getLastName());
        artist.setStageName(artistDetails.getStageName());
        artist.setBio(artistDetails.getBio());
        artist.setVeilhushArtist(artistDetails.isVeilhushArtist());
        artist.setProfilePicture(artistDetails.getProfilePicture());
        artist.setLocation(artistDetails.getLocation());
        artist.setFollowerCount(artistDetails.getFollowerCount());
        artist.setActive(artistDetails.isActive());
        artist.setSoundcloudLink(artistDetails.getSoundcloudLink());
        artist.setInstagramLink(artistDetails.getInstagramLink());
        artist.setRALink(artistDetails.getRALink());
        artist.setFacebookLink(artistDetails.getFacebookLink());
        artist.setBandcampLink(artistDetails.getBandcampLink());
        artist.setEvents(artistDetails.getEvents());
        return artistRepository.save(artist);
    }

    public void deleteArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id " + id));
        artistRepository.delete(artist);
    }

}
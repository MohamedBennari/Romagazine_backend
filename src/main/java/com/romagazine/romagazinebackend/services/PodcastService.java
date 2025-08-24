package com.romagazine.romagazinebackend.services;

import com.romagazine.romagazinebackend.entities.Artist;
import com.romagazine.romagazinebackend.entities.Podcast;
import com.romagazine.romagazinebackend.repositories.ArtistRepository;
import com.romagazine.romagazinebackend.repositories.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PodcastService {

    @Autowired
    private PodcastRepository podcastRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public List<Podcast> getAllPodcasts() {
        return podcastRepository.findByIsActiveTrue();
    }

    public Optional<Podcast> getPodcastById(Long id) {
        return podcastRepository.findById(id);
    }

    public List<Podcast> getPodcastsByArtist(Long artistId) {
        return podcastRepository.findByArtistsId(artistId);
    }

    public Podcast createPodcast(Podcast podcast) {
        // Load artists properly
        List<Artist> artists = podcast.getArtists().stream()
                .map(artist -> artistRepository.findById(artist.getId())
                        .orElseThrow(() -> new RuntimeException("Artist not found with id " + artist.getId())))
                .collect(Collectors.toList());
        podcast.setArtists(artists);
        podcast.setCreatedAt(LocalDateTime.now());
        return podcastRepository.save(podcast);
    }

    public Podcast updatePodcast(Long id, Podcast podcastDetails) {
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Podcast not found with id " + id));

        // Only update allowed fields
        podcast.setTitle(podcastDetails.getTitle());
        podcast.setDescription(podcastDetails.getDescription());
        podcast.setSoundcloudLink(podcastDetails.getSoundcloudLink());
        podcast.setImage(podcastDetails.getImage());
        podcast.setPublishedAt(podcastDetails.getPublishedAt());
        podcast.setActive(podcastDetails.isActive());
        podcast.setUpdatedAt(LocalDateTime.now());
        podcast.setSecondArtists(podcastDetails.getSecondArtists());

        // Load artists properly if they're being updated
        if (podcastDetails.getArtists() != null && !podcastDetails.getArtists().isEmpty()) {
            List<Artist> artists = podcastDetails.getArtists().stream()
                    .map(artist -> artistRepository.findById(artist.getId())
                            .orElseThrow(() -> new RuntimeException("Artist not found with id " + artist.getId())))
                    .collect(Collectors.toList());
            podcast.setArtists(artists);
        }

        return podcastRepository.save(podcast);
    }

    public void deletePodcast(Long id) {
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Podcast not found with id " + id));
        podcastRepository.delete(podcast);
    }

}
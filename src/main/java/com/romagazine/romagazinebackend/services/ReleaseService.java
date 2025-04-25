package com.romagazine.romagazinebackend.services;

import com.romagazine.romagazinebackend.entities.Artist;
import com.romagazine.romagazinebackend.entities.Release;
import com.romagazine.romagazinebackend.repositories.ArtistRepository;
import com.romagazine.romagazinebackend.repositories.ReleaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReleaseService {

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public List<Release> getAllReleases() {
        return releaseRepository.findByIsActiveTrue();
    }

    public Optional<Release> getReleaseById(Long id) {
        return releaseRepository.findById(id);
    }

    public List<Release> getReleasesByArtist(Long artistId) {
        return releaseRepository.findByArtistId(artistId);
    }

    public List<Release> getReleasesByType(String type) {
        return releaseRepository.findByType(type);
    }

    public List<Release> getVeilhushReleases() {
        return releaseRepository.findByVeilhushReleaseTrue();
    }

    public Release createRelease(Release release) {
        Artist artist = artistRepository.findById(release.getArtist().getId())
                .orElseThrow(() -> new RuntimeException("Artist not found with id " + release.getArtist().getId()));
        release.setArtist(artist);
        release.setCreatedAt(LocalDateTime.now());
        return releaseRepository.save(release);
    }

    public Release updateRelease(Long id, Release releaseDetails) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Release not found with id " + id));
        
        if (releaseDetails.getArtist() != null && releaseDetails.getArtist().getId() != null) {
            Artist artist = artistRepository.findById(releaseDetails.getArtist().getId())
                    .orElseThrow(() -> new RuntimeException("Artist not found with id " + releaseDetails.getArtist().getId()));
            release.setArtist(artist);
        }
        
        release.setTitle(releaseDetails.getTitle());
        release.setReleaseDate(releaseDetails.getReleaseDate());
        release.setSoundcloudLink(releaseDetails.getSoundcloudLink());
        release.setPurchaseLink(releaseDetails.getPurchaseLink());
        release.setType(releaseDetails.getType());
        release.setVeilhushRelease(releaseDetails.isVeilhushRelease());
        release.setImage(releaseDetails.getImage());
        release.setDescription(releaseDetails.getDescription());
        release.setActive(releaseDetails.isActive());
        return releaseRepository.save(release);
    }

    public void deleteRelease(Long id) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Release not found with id " + id));
        releaseRepository.delete(release);
    }

}
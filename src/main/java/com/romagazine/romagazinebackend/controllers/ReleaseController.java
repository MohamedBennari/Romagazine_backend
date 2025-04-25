package com.romagazine.romagazinebackend.controllers;

import com.romagazine.romagazinebackend.entities.Release;
import com.romagazine.romagazinebackend.services.ReleaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/releases")
public class ReleaseController {

    @Autowired
    private ReleaseService releaseService;

    @GetMapping
    public List<Release> getAllReleases() {
        return releaseService.getAllReleases();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Release> getReleaseById(@PathVariable Long id) {
        Optional<Release> release = releaseService.getReleaseById(id);
        return release.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/artist/{artistId}")
    public List<Release> getReleasesByArtist(@PathVariable Long artistId) {
        return releaseService.getReleasesByArtist(artistId);
    }

    @GetMapping("/type/{type}")
    public List<Release> getReleasesByType(@PathVariable String type) {
        return releaseService.getReleasesByType(type);
    }

    @GetMapping("/veilhush")
    public List<Release> getVeilhushReleases() {
        return releaseService.getVeilhushReleases();
    }

    @PostMapping
    public ResponseEntity<Release> createRelease(@Valid @RequestBody Release release) {
        Release savedRelease = releaseService.createRelease(release);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedRelease.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedRelease);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Release> updateRelease(@PathVariable Long id, @Valid @RequestBody Release release) {
        Release updatedRelease = releaseService.updateRelease(id, release);
        return ResponseEntity.ok(updatedRelease);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        releaseService.deleteRelease(id);
        return ResponseEntity.noContent().build();
    }

}
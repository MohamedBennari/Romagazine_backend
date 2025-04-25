package com.romagazine.romagazinebackend.controllers;

import com.romagazine.romagazinebackend.entities.Podcast;
import com.romagazine.romagazinebackend.services.PodcastService;
import com.romagazine.romagazinebackend.services.FileStorageService;
import com.romagazine.romagazinebackend.utils.ImageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/podcasts")
public class PodcastController {

    @Autowired
    private PodcastService podcastService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<Podcast> getAllPodcasts() {
        return podcastService.getAllPodcasts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Podcast> getPodcastById(@PathVariable Long id) {
        Optional<Podcast> podcast = podcastService.getPodcastById(id);
        return podcast.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/artist/{artistId}")
    public List<Podcast> getPodcastsByArtist(@PathVariable Long artistId) {
        return podcastService.getPodcastsByArtist(artistId);
    }

    @PostMapping
    public ResponseEntity<Podcast> createPodcast(@Valid @RequestBody Podcast podcast) {
        Podcast savedPodcast = podcastService.createPodcast(podcast);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPodcast.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedPodcast);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Podcast> updatePodcast(@PathVariable Long id, @Valid @RequestBody Podcast podcast) {
        Podcast updatedPodcast = podcastService.updatePodcast(id, podcast);
        return ResponseEntity.ok(updatedPodcast);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePodcast(@PathVariable Long id) {
        podcastService.deletePodcast(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload podcast image", description = "Upload an image for a specific podcast")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Podcast.class))),
        @ApiResponse(responseCode = "400", description = "Invalid image file"),
        @ApiResponse(responseCode = "404", description = "Podcast not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    
    @PostMapping(value = "/{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Podcast> uploadPodcastImage(
            @Parameter(description = "Podcast ID", required = true) @PathVariable Long id,
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            if (!ImageUtils.isValidImage(file)) {
                return ResponseEntity.badRequest().build();
            }

            String filename = fileStorageService.storeFile(file);
            String imageUrl = ImageUtils.generateImageUrl(filename);

            Podcast podcast = podcastService.getPodcastById(id)
                    .orElseThrow(() -> new RuntimeException("Podcast not found"));

            podcast.setImage(imageUrl);
            Podcast updatedPodcast = podcastService.updatePodcast(id, podcast);
            return ResponseEntity.ok(updatedPodcast);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
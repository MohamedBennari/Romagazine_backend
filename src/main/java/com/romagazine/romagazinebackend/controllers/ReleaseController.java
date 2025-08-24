package com.romagazine.romagazinebackend.controllers;

import com.romagazine.romagazinebackend.entities.Podcast;
import com.romagazine.romagazinebackend.entities.Release;
import com.romagazine.romagazinebackend.services.ReleaseService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/releases")
public class ReleaseController {

    @Autowired
    private ReleaseService releaseService;

    @Autowired
    private FileStorageService fileStorageService;

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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Release> createRelease(@Valid @RequestBody Release release) {
        Release savedRelease = releaseService.createRelease(release);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedRelease.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedRelease);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Release> updateRelease(@PathVariable Long id, @Valid @RequestBody Release release) {
        Release updatedRelease = releaseService.updateRelease(id, release);
        return ResponseEntity.ok(updatedRelease);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        releaseService.deleteRelease(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Upload Release image", description = "Upload an image for a specific Release")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Release.class))),
            @ApiResponse(responseCode = "400", description = "Invalid image file"),
            @ApiResponse(responseCode = "404", description = "Release not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping(value = "/{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Release> uploadReleaseImage(
            @Parameter(description = "Release ID", required = true) @PathVariable Long id,
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            if (!ImageUtils.isValidImage(file)) {
                return ResponseEntity.badRequest().build();
            }

            String filename = fileStorageService.storeFile(file);
            String imageUrl = ImageUtils.generateImageUrl(filename);

            Release release = releaseService.getReleaseById(id)
                    .orElseThrow(() -> new RuntimeException("Podcast not found"));

            release.setImage(imageUrl);
            Release updatedRelease = releaseService.updateRelease(id, release);
            return ResponseEntity.ok(updatedRelease);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
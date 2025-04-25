package com.romagazine.romagazinebackend.repositories;

import com.romagazine.romagazinebackend.entities.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface PodcastRepository extends JpaRepository<Podcast, Long> {
    List findByArtistsId(Long artistId);
    List findByIsActiveTrue();
}
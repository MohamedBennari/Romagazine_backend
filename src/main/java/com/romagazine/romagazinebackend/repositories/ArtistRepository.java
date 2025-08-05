package com.romagazine.romagazinebackend.repositories;

import com.romagazine.romagazinebackend.entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findByStageName(String stageName);
    List<Artist> findByVeilhushArtistTrue();
}
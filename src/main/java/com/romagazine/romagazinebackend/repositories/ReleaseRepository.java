package com.romagazine.romagazinebackend.repositories;

import com.romagazine.romagazinebackend.entities.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ReleaseRepository extends JpaRepository<Release, Long> {
    List findByArtistId(Long artistId);
    List findByType(String type);
    List findByVeilhushReleaseTrue();
    List findByIsActiveTrue();
}
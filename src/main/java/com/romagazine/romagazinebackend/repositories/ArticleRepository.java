package com.romagazine.romagazinebackend.repositories;

import com.romagazine.romagazinebackend.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(exported = false)
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List findByAuthorId(Long authorId);
    List findByCategory(String category);
    List findByIsActiveTrue();
    List findByTagsContaining(String tag);

}


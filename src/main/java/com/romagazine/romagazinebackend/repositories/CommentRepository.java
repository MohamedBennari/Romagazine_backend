package com.romagazine.romagazinebackend.repositories;

import com.romagazine.romagazinebackend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List findByArticleId(Long articleId);
    List findByAuthorId(Long authorId);
    List findByParentCommentId(Long parentCommentId);
    List findByIsActiveTrue();
}
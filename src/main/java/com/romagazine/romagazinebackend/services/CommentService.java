package com.romagazine.romagazinebackend.services;

import com.romagazine.romagazinebackend.entities.Article;
import com.romagazine.romagazinebackend.entities.Comment;
import com.romagazine.romagazinebackend.entities.User;
import com.romagazine.romagazinebackend.repositories.ArticleRepository;
import com.romagazine.romagazinebackend.repositories.CommentRepository;
import com.romagazine.romagazinebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public List<Comment> getAllComments() {
        return commentRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByArticle(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByAuthor(Long authorId) {
        return commentRepository.findByAuthorId(authorId);
    }

    @Transactional(readOnly = true)
    public List<Comment> getReplies(Long parentCommentId) {
        return commentRepository.findByParentCommentId(parentCommentId);
    }

    @Transactional
    public Comment createComment(Comment comment) {
        // Load author and article properly
        User author = userRepository.findById(comment.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + comment.getAuthor().getId()));
        comment.setAuthor(author);

        if (comment.getArticle() != null) {
            Article article = articleRepository.findById(comment.getArticle().getId())
                    .orElseThrow(() -> new RuntimeException("Article not found with id " + comment.getArticle().getId()));
            comment.setArticle(article);
        }

        // Handle parent comment if it exists
        if (comment.getParentComment() != null) {
            Comment parentComment = commentRepository.findById(comment.getParentComment().getId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found with id " + comment.getParentComment().getId()));
            comment.setParentComment(parentComment);
        }

        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + id));

        // Load author and article properly if they're being updated
        if (commentDetails.getAuthor() != null) {
            User author = userRepository.findById(commentDetails.getAuthor().getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + commentDetails.getAuthor().getId()));
            comment.setAuthor(author);
        }

        if (commentDetails.getArticle() != null) {
            Article article = articleRepository.findById(commentDetails.getArticle().getId())
                    .orElseThrow(() -> new RuntimeException("Article not found with id " + commentDetails.getArticle().getId()));
            comment.setArticle(article);
        }

        // Handle parent comment update if it exists
        if (commentDetails.getParentComment() != null) {
            Comment parentComment = commentRepository.findById(commentDetails.getParentComment().getId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found with id " + commentDetails.getParentComment().getId()));
            comment.setParentComment(parentComment);
        }

        comment.setContent(commentDetails.getContent());
        comment.setActive(commentDetails.isActive());
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + id));
        commentRepository.delete(comment);
    }
}
package com.romagazine.romagazinebackend.services;

import com.romagazine.romagazinebackend.entities.Article;
import com.romagazine.romagazinebackend.entities.User;
import com.romagazine.romagazinebackend.repositories.ArticleRepository;
import com.romagazine.romagazinebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findByIsActiveTrue();
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> getArticlesByAuthor(Long authorId) {
        return articleRepository.findByAuthorId(authorId);
    }

    public List<Article> getArticlesByCategory(String category) {
        return articleRepository.findByCategory(category);
    }

    public List<Article> getArticlesByTag(String tag) {
        return articleRepository.findByTagsContaining(tag);
    }

    public Article createArticle(Article article) {
        User author = userRepository.findById(article.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("Author not found with id " + article.getAuthor().getId()));
        article.setAuthor(author);
        article.setCreatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }

    public Article updateArticle(Long id, Article articleDetails) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id));
        
        if (articleDetails.getAuthor() != null && articleDetails.getAuthor().getId() != null) {
            User author = userRepository.findById(articleDetails.getAuthor().getId())
                    .orElseThrow(() -> new RuntimeException("Author not found with id " + articleDetails.getAuthor().getId()));
            article.setAuthor(author);
        }
        
        article.setTitle(articleDetails.getTitle());
        article.setSubtitle(articleDetails.getSubtitle());
        article.setBody(articleDetails.getBody());
        article.setCategory(articleDetails.getCategory());
        article.setTags(articleDetails.getTags());
        article.setImage(articleDetails.getImage());
        article.setSecondImage(articleDetails.getSecondImage());
        article.setThirdImage(articleDetails.getThirdImage());
        article.setPublishedAt(articleDetails.getPublishedAt());
        article.setActive(articleDetails.isActive());
        article.setUpdatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id));
        articleRepository.delete(article);
    }

}
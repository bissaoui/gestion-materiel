package com.gestion.materiel.controller;

import com.gestion.materiel.model.Article;
import com.gestion.materiel.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleService.getArticleById(id);
        return article.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article newArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(newArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        if (!articleService.getArticleById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        article.setId(id);
        Article updatedArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(updatedArticle);
    }

    @PutMapping("qte/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Article> optionalArticle = articleService.getArticleById(id);
        if (!optionalArticle.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Article article = optionalArticle.get();

        // Update only the 'quantite' field if present in the request
        if (updates.containsKey("quantite")) {
            article.setQte((Integer) updates.get("quantite"));

        }

        Article updatedArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(updatedArticle);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        if (!articleService.getArticleById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}

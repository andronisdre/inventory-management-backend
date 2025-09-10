package se.vgregion.inventory_management_backend.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import se.vgregion.inventory_management_backend.models.Article;
import se.vgregion.inventory_management_backend.services.ArticleService;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    //POST an article
    @PostMapping
    public Article createArticle(@Valid @RequestBody Article article) {
        return articleService.addArticle(article);
    }

    //GET ALL articles
    @GetMapping("/all")
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    //GET article by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Article getAuctionModelsById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    //Remove an article
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteAuctionModels(@PathVariable Long id) {
        return articleService.deleteArticle(id);
    }
}

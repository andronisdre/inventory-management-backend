package se.vgregion.inventory_management_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.inventory_management_backend.models.Article;
import se.vgregion.inventory_management_backend.repository.ArticleRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private final ArticleRepository articleRepository;

    public ArticleService (ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // POST Article
    public Article addArticle(Article article) {
        return articleRepository.save(article);
    }

    // GET ALL Articles
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // GET Article by id
    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Article id: " + id));
    }

    // DELETE Article
    public String deleteArticle(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article != null) {
            articleRepository.deleteById(id);
            return "Article deleted";
        } else {
            return "Article id doesn't exist";
        }
    }

    // PUT update article
    public Article updateArticle(Long id, Article updatedArticle) {
        Article exisingArticle = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Article id: " + id));

        if (updatedArticle.getUnit() != null) {
            exisingArticle.setUnit(updatedArticle.getUnit());
        }
        if (updatedArticle.getName() != null) {
            exisingArticle.setName(updatedArticle.getName());
        }
        if (updatedArticle.getMinimumAmount() != 0) {
            exisingArticle.setMinimumAmount(updatedArticle.getMinimumAmount());
        }

        exisingArticle.setCreatedAt(exisingArticle.getCreatedAt());
        exisingArticle.setUpdatedAt(new Date());

        return articleRepository.save(exisingArticle);
    }

    // PATCH Article amount
    public Article patchArticleAmount(Long id, int newAmount) {
        Article exisingArticle = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Article id: " + id));

        if (newAmount >= 0) {
            exisingArticle.setAmount(newAmount);
        }

        exisingArticle.setUpdatedAt(new Date());

        return articleRepository.save(exisingArticle);
    }

    // GET Articles with low amount
    public List<Article> getAllArticlesWithLowAmount() {
        return articleRepository.findAll().stream().filter(article -> article.getMinimumAmount()>=(article.getAmount()))
                .collect(Collectors.toList());
    }
}

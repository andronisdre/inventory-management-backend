package se.vgregion.inventory_management_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.inventory_management_backend.models.Article;
import se.vgregion.inventory_management_backend.repository.ArticleRepository;

import java.util.List;
import java.util.NoSuchElementException;

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
        return articleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Invalid Article id: " + id));
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

    // PATCH Article amount

    // GET Article with low amount

}

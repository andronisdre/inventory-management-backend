package se.vgregion.inventory_management_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.vgregion.inventory_management_backend.dto.ArticleResponseDTO;
import se.vgregion.inventory_management_backend.dto.CreateArticleDTO;
import se.vgregion.inventory_management_backend.dto.PatchAmountDTO;
import se.vgregion.inventory_management_backend.dto.UpdateArticleDTO;
import se.vgregion.inventory_management_backend.models.Article;
import se.vgregion.inventory_management_backend.repository.ArticleRepository;

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
    public ArticleResponseDTO addArticle(CreateArticleDTO createArticleDTO) {

        //creates an article and assigns the values of the DTO to the Article type.
        Article article = new Article();
        article.setName(createArticleDTO.getName());
        article.setAmount(createArticleDTO.getAmount());
        article.setMinimumAmount(createArticleDTO.getMinimumAmount());
        article.setUnit(createArticleDTO.getUnit());

        Article savedArticle = articleRepository.save(article);
        return new ArticleResponseDTO(savedArticle);
    }

    // GET ALL Articles, with pagination always enabled and optional search function
    public Page<ArticleResponseDTO> getAllArticlesPaginated(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Article> articles;
        if (search != null && !search.trim().isEmpty()) {
            articles = articleRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        } else {
            articles = articleRepository.findAll(pageable);
        }

        return articles.map(ArticleResponseDTO::new);
    }

    // GET Article by id
    public ArticleResponseDTO getArticleById(Long id) {
        return new ArticleResponseDTO(articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Article id: " + id)));
    }

    // DELETE Article
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Article id: " + id));
        articleRepository.deleteById(id);
    }

    // PUT update article
    public ArticleResponseDTO updateArticle(Long id, UpdateArticleDTO updateArticleDTO) {
        Article existingArticle = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Article id: " + id));

        if (updateArticleDTO.getUnit() != null) {
            existingArticle.setUnit(updateArticleDTO.getUnit());
        }
        if (updateArticleDTO.getName() != null) {
            existingArticle.setName(updateArticleDTO.getName());
        }
        //since amount and minimumAmount are Integer types in the UpdateArticleDTOs, they are nullable, unlike primitive int types which cant be nullable.
        //This is useful since you can now ensure that a user has to type an amount and minimum amount and that it won't automatically be  the value 0.
        //You can still assign the value 0.
        //The Article model itself (the one that is actually saved) still uses int
        //which means that memory is still optimized, since int type uses only 4 bytes compared to Integer which uses 16.
        if (updateArticleDTO.getAmount() != null) {
            existingArticle.setAmount(updateArticleDTO.getAmount());
        }
        if (updateArticleDTO.getMinimumAmount() != null) {
            existingArticle.setMinimumAmount(updateArticleDTO.getMinimumAmount());
        }

        articleRepository.save(existingArticle);
        return new ArticleResponseDTO(existingArticle);
    }

    // PATCH Article amount
    public ArticleResponseDTO patchArticleAmount(Long id, PatchAmountDTO patchAmountDTO) {
        Article exisingArticle = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Article id: " + id));

        if (patchAmountDTO.getAmount() != null) {
            exisingArticle.setAmount(patchAmountDTO.getAmount());
        }

        articleRepository.save(exisingArticle);
        return new ArticleResponseDTO(exisingArticle);
    }

    // GET Articles with low amount
    public List<ArticleResponseDTO> getAllArticlesWithLowAmount() {
        //find all articles that match the filter where amount is smaller or equal to minimumAmount, then maps them into ArticleResponseDTOs for the correct response format.
        return articleRepository.findAll().stream()
                .filter(article -> article.getAmount() <= article.getMinimumAmount())
                .map(ArticleResponseDTO::new)
                .collect(Collectors.toList());
    }
}

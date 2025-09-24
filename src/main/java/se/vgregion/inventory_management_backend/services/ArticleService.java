package se.vgregion.inventory_management_backend.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.inventory_management_backend.dto.ArticleResponseDTO;
import se.vgregion.inventory_management_backend.dto.CreateArticleDTO;
import se.vgregion.inventory_management_backend.dto.PatchAmountDTO;
import se.vgregion.inventory_management_backend.dto.UpdateArticleDTO;
import se.vgregion.inventory_management_backend.enums.ECategory;
import se.vgregion.inventory_management_backend.models.Article;
import se.vgregion.inventory_management_backend.repository.ArticleRepository;

//Transactional annotation makes it so that all operations either fully succeed or fully fail, preventing partial updates
@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService (ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // POST Article
    public ArticleResponseDTO addArticle(CreateArticleDTO createArticleDTO) {

        //creates an article using the constructor and assigns the values of the DTO to the Article type.
        Article article = new Article(
            createArticleDTO.getName(),
            createArticleDTO.getAmount(),
            createArticleDTO.getMinimumAmount(),
            createArticleDTO.getUnit(), createArticleDTO.getCategory()
        );

        Article savedArticle = articleRepository.save(article);
        return new ArticleResponseDTO(savedArticle);
    }

    // GET ALL Articles, with pagination always enabled and optional search function,
    // marked with transactional (readonly true) which improves efficiency since spring boot starts the transaction in read-only mode
    // you can also sort by fields and decide whether they should be sorted in ascending or descending order.
    // in the frontend im currently only making use of ordering by name, createAt and unit
    @Transactional(readOnly = true)
    public Page<ArticleResponseDTO> getAllArticlesPaginated(
            int page,
            int size,
            String search,
            boolean onlyLowStockArticles,
            String categoryFilter,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        ECategory categoryEnum = null;
        if (categoryFilter != null && !categoryFilter.isBlank() && !"ALL".equalsIgnoreCase(categoryFilter)) {
            try {
                categoryEnum = ECategory.valueOf(categoryFilter.toUpperCase());
            } catch (IllegalArgumentException e) {
                categoryEnum = null;
            }
        }

        Page<Article> articles = articleRepository.findArticlesWithFilters(
                (search != null && !search.trim().isEmpty()) ? search.trim() : null,
                categoryEnum,
                onlyLowStockArticles,
                pageable
        );

        return articles.map(ArticleResponseDTO::new);
    }

    // GET Article by id
    @Transactional(readOnly = true)
    public ArticleResponseDTO getArticleById(Long id) {
        return new ArticleResponseDTO(articleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id)));
    }

    // DELETE Article
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new EntityNotFoundException("Article not found with id: " + id);
        }
        articleRepository.deleteById(id);
    }

    // PUT update article
    public ArticleResponseDTO updateArticle(Long id, UpdateArticleDTO updateArticleDTO) {
        Article existingArticle = articleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));

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

    // PATCH Article amount, add. I have two separate endpoints for adding or subtrtacting, following the single responibility principle.
    // This improves readability and also makes the code less susceptible to logical errors.
    public ArticleResponseDTO patchArticleAmountAdd(Long id, PatchAmountDTO patchAmountDTO) {
        Article exisingArticle = articleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));

        exisingArticle.setAmount(patchAmountDTO.getAmount() + exisingArticle.getAmount());

        articleRepository.save(exisingArticle);
        return new ArticleResponseDTO(exisingArticle);
    }

    // PATCH Article amount, subtract
    public ArticleResponseDTO patchArticleAmountRemove(Long id, PatchAmountDTO patchAmountDTO) {
        Article exisingArticle = articleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));

        if ((exisingArticle.getAmount() - patchAmountDTO.getAmount()) >= 0) {
            exisingArticle.setAmount(exisingArticle.getAmount() - patchAmountDTO.getAmount());
        } else throw new IllegalArgumentException("You cant subtract more than total amount!");

        articleRepository.save(exisingArticle);
        return new ArticleResponseDTO(exisingArticle);
    }
}

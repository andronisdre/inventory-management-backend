package se.vgregion.inventory_management_backend.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import se.vgregion.inventory_management_backend.dto.ArticleResponseDTO;
import se.vgregion.inventory_management_backend.dto.CreateArticleDTO;
import se.vgregion.inventory_management_backend.dto.PatchAmountDTO;
import se.vgregion.inventory_management_backend.dto.UpdateArticleDTO;
import se.vgregion.inventory_management_backend.enums.EUnit;
import se.vgregion.inventory_management_backend.models.Article;
import se.vgregion.inventory_management_backend.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    private Article createTestArticle() {
        Article article = new Article();
        article.setId(1L);
        article.setName("Test Article");
        article.setAmount(100);
        article.setMinimumAmount(10);
        article.setUnit(EUnit.PIECES);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        return article;
    }

    @Test
    void testAddArticle_Success() {
        // Arrange
        CreateArticleDTO createDTO = new CreateArticleDTO();
        createDTO.setName("New Article");
        createDTO.setAmount(50);
        createDTO.setMinimumAmount(5);
        createDTO.setUnit(EUnit.PIECES);

        Article savedArticle = new Article();
        savedArticle.setId(1L);
        savedArticle.setName("New Article");
        savedArticle.setAmount(50);
        savedArticle.setMinimumAmount(5);
        savedArticle.setUnit(EUnit.PIECES);

        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);

        // Act
        ArticleResponseDTO result = articleService.addArticle(createDTO);

        // Assert
        assertNotNull(result.getId(), "Saved article should have an ID");
        assertEquals("New Article", result.getName(), "Article name should match");
        assertEquals(50, result.getAmount(), "Article amount should match");
        assertEquals(5, result.getMinimumAmount(), "Article minimum amount should match");
        assertEquals(EUnit.PIECES, result.getUnit(), "Article unit should match");

        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void testGetAllArticles_Success() {
        // Arrange
        Article article1 = createTestArticle();
        Article article2 = createTestArticle();
        article2.setId(2L);
        article2.setName("Second Article");

        List<Article> articles = Arrays.asList(article1, article2);
        when(articleRepository.findAll()).thenReturn(articles);

        // Act
        Page<ArticleResponseDTO> result = articleService.getAllArticlesPaginated(0,5,"",true, "", "");

        // Assert
        assertEquals(2, result.getContent().size(), "Should return 2 articles");
        assertEquals("Test Article", result.getContent().get(0).getName(), "First article name should match");
        assertEquals("Second Article", result.getContent().get(1).getName(), "Second article name should match");

        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void testGetArticleById_Success() {
        // Arrange
        Article article = createTestArticle();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // Act
        ArticleResponseDTO result = articleService.getArticleById(1L);

        // Assert
        assertEquals(1L, result.getId(), "Article ID should match");
        assertEquals("Test Article", result.getName(), "Article name should match");
        assertEquals(100, result.getAmount(), "Article amount should match");

        verify(articleRepository, times(1)).findById(1L);
    }

    @Test
    void testGetArticleById_NotFound() {
        // Arrange
        when(articleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> articleService.getArticleById(999L),
                "Should throw IllegalArgumentException for article that doesnt exist");

        verify(articleRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteArticle_Success() {
        // Arrange
        Article article = createTestArticle();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // Act
        articleService.deleteArticle(1L);

        // Assert
        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteArticle_NotFound() {
        // Arrange
        when(articleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> articleService.deleteArticle(999L),
                "Should throw IllegalArgumentException for article that doesnt exist");

        verify(articleRepository, times(1)).findById(999L);
        verify(articleRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateArticle_Success() {
        // Arrange
        Article existingArticle = createTestArticle();
        UpdateArticleDTO updateDTO = new UpdateArticleDTO();
        updateDTO.setName("Updated Article");
        updateDTO.setAmount(75);
        updateDTO.setMinimumAmount(15);
        updateDTO.setUnit(EUnit.GRAMS);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        // Act
        ArticleResponseDTO result = articleService.updateArticle(1L, updateDTO);

        // Assert
        assertEquals("Updated Article", result.getName(), "Article name should be updated");
        assertEquals(75, result.getAmount(), "Article amount should be updated");
        assertEquals(15, result.getMinimumAmount(), "Article minimum amount should be updated");
        assertEquals(EUnit.GRAMS, result.getUnit(), "Article unit should be updated");

        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).save(existingArticle);
    }

    @Test
    void testUpdateArticle_PartialUpdate() {
        // Arrange
        Article existingArticle = createTestArticle();
        UpdateArticleDTO updateDTO = new UpdateArticleDTO();
        updateDTO.setName("Updated Article");
        // amount, minimumAmount, and unit are null, so they should not be updated

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        // Act
        ArticleResponseDTO result = articleService.updateArticle(1L, updateDTO);

        // Assert
        assertEquals("Updated Article", result.getName(), "Article name should be updated");
        assertEquals(100, result.getAmount(), "Article amount should remain unchanged");
        assertEquals(10, result.getMinimumAmount(), "Article minimum amount should remain unchanged");
        assertEquals(EUnit.PIECES, result.getUnit(), "Article unit should remain unchanged");

        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).save(existingArticle);
    }

    @Test
    void testPatchArticleAmount_Success() {
        // Arrange
        Article existingArticle = createTestArticle();
        PatchAmountDTO patchDTO = new PatchAmountDTO();
        patchDTO.setAmount(25);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        // Act
        ArticleResponseDTO result = articleService.patchArticleAmount(1L, patchDTO);

        // Assert
        assertEquals(25, result.getAmount(), "Article amount should be updated");
        assertEquals("Test Article", result.getName(), "Other fields should remain unchanged");

        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).save(existingArticle);
    }
}
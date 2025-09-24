package se.vgregion.inventory_management_backend.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import se.vgregion.inventory_management_backend.dto.ArticleResponseDTO;
import se.vgregion.inventory_management_backend.dto.CreateArticleDTO;
import se.vgregion.inventory_management_backend.dto.PatchAmountDTO;
import se.vgregion.inventory_management_backend.dto.UpdateArticleDTO;
import se.vgregion.inventory_management_backend.enums.ECategory;
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

    private CreateArticleDTO createTestCreateDTO() {
        CreateArticleDTO dto = new CreateArticleDTO();
        dto.setName("New Article");
        dto.setAmount(50);
        dto.setMinimumAmount(5);
        dto.setUnit(EUnit.PIECES);
        return dto;
    }

    private UpdateArticleDTO createTestUpdateDTO() {
        UpdateArticleDTO dto = new UpdateArticleDTO();
        dto.setName("Updated Article");
        dto.setAmount(75);
        dto.setMinimumAmount(15);
        dto.setUnit(EUnit.GRAMS);
        return dto;
    }

    private PatchAmountDTO createTestPatchDTO(int amount) {
        PatchAmountDTO dto = new PatchAmountDTO();
        dto.setAmount(amount);
        return dto;
    }

    //all tests follow the arrange, act, assert pattern
    @Test
    void testAddArticle_Success() {
        //arrange
        CreateArticleDTO createDTO = createTestCreateDTO();
        Article savedArticle = new Article("New Article", 50, 5, EUnit.PIECES, ECategory.OTHER);
        savedArticle.setId(1L);

        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);

        //act
        ArticleResponseDTO result = articleService.addArticle(createDTO);

        //assert
        assertNotNull(result.getId());
        assertEquals("New Article", result.getName());
        assertEquals(50, result.getAmount());
        assertEquals(5, result.getMinimumAmount());
        assertEquals(EUnit.PIECES, result.getUnit());
        assertEquals(ECategory.OTHER, result.getCategory());
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    //makes sure pagination works
    @Test
    void testGetAllArticlesPaginated_Success() {
        Article article1 = createTestArticle();
        Article article2 = createTestArticle();
        article2.setId(2L);
        article2.setName("Second Article");

        List<Article> articles = Arrays.asList(article1, article2);
        Page<Article> articlePage = new PageImpl<>(articles, PageRequest.of(0, 10), 2);

        when(articleRepository.findArticlesWithFilters(any(), any(), any(Boolean.class), any(Pageable.class)))
                .thenReturn(articlePage);

        Page<ArticleResponseDTO> result = articleService.getAllArticlesPaginated(0, 10, "", false, "", "name", "asc");

        assertEquals(2, result.getContent().size());
        assertEquals("Test Article", result.getContent().get(0).getName());
        assertEquals("Second Article", result.getContent().get(1).getName());
        verify(articleRepository, times(1)).findArticlesWithFilters(any(), any(), any(Boolean.class), any(Pageable.class));
    }

    @Test
    void testGetAllArticlesPaginated_WithSearch() {
        Article article = createTestArticle();
        List<Article> articles = Arrays.asList(article);
        Page<Article> articlePage = new PageImpl<>(articles, PageRequest.of(0, 10), 1);

        when(articleRepository.findArticlesWithFilters(eq("Test"), any(), any(Boolean.class), any(Pageable.class)))
                .thenReturn(articlePage);

        Page<ArticleResponseDTO> result = articleService.getAllArticlesPaginated(0, 10, "Test", false, "", "name", "asc");

        assertEquals(1, result.getContent().size());
        assertEquals("Test Article", result.getContent().get(0).getName());
        verify(articleRepository, times(1)).findArticlesWithFilters(eq("Test"), any(), any(Boolean.class), any(Pageable.class));
    }

    @Test
    void testGetAllArticlesPaginated_LowStockOnly() {
        Article lowStockArticle = createTestArticle();
        lowStockArticle.setAmount(5);
        List<Article> articles = Arrays.asList(lowStockArticle);
        Page<Article> articlePage = new PageImpl<>(articles, PageRequest.of(0, 10), 1);

        when(articleRepository.findArticlesWithFilters(any(), any(), eq(true), any(Pageable.class)))
                .thenReturn(articlePage);

        Page<ArticleResponseDTO> result = articleService.getAllArticlesPaginated(0, 10, "", true, "", "name", "asc");

        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0).isLowStock());
        verify(articleRepository, times(1)).findArticlesWithFilters(any(), any(), eq(true), any(Pageable.class));
    }

    @Test
    void testGetAllArticlesPaginated_DescendingSort() {
        Article article = createTestArticle();
        List<Article> articles = Arrays.asList(article);
        Page<Article> articlePage = new PageImpl<>(articles, PageRequest.of(0, 10, Sort.by("name").descending()), 1);

        when(articleRepository.findArticlesWithFilters(any(), any(), any(Boolean.class), any(Pageable.class)))
                .thenReturn(articlePage);

        Page<ArticleResponseDTO> result = articleService.getAllArticlesPaginated(0, 10, "", false, "", "name", "desc");

        assertEquals(1, result.getContent().size());
        verify(articleRepository, times(1)).findArticlesWithFilters(any(), any(), any(Boolean.class), any(Pageable.class));
    }

    @Test
    void testGetArticleById_Success() {
        Article article = createTestArticle();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        ArticleResponseDTO result = articleService.getArticleById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Test Article", result.getName());
        assertEquals(100, result.getAmount());
        verify(articleRepository, times(1)).findById(1L);
    }

    @Test
    void testGetArticleById_NotFound() {
        when(articleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> articleService.getArticleById(999L));

        verify(articleRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteArticle_Success() {
        when(articleRepository.existsById(1L)).thenReturn(true);

        articleService.deleteArticle(1L);

        verify(articleRepository, times(1)).existsById(1L);
        verify(articleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteArticle_NotFound() {
        when(articleRepository.existsById(999L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> articleService.deleteArticle(999L));

        verify(articleRepository, times(1)).existsById(999L);
        verify(articleRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateArticle_Success() {
        Article existingArticle = createTestArticle();
        UpdateArticleDTO updateDTO = createTestUpdateDTO();

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        ArticleResponseDTO result = articleService.updateArticle(1L, updateDTO);

        assertEquals("Updated Article", result.getName());
        assertEquals(75, result.getAmount());
        assertEquals(15, result.getMinimumAmount());
        assertEquals(EUnit.GRAMS, result.getUnit());
        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).save(existingArticle);
    }

    //fields not specified when calling update should remain unchanged
    @Test
    void testUpdateArticle_PartialUpdate() {
        Article existingArticle = createTestArticle();
        UpdateArticleDTO updateDTO = new UpdateArticleDTO();
        updateDTO.setName("Updated Article");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        ArticleResponseDTO result = articleService.updateArticle(1L, updateDTO);

        assertEquals("Updated Article", result.getName());
        assertEquals(100, result.getAmount());
        assertEquals(10, result.getMinimumAmount());
        assertEquals(EUnit.PIECES, result.getUnit());
        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).save(existingArticle);
    }

    @Test
    void testUpdateArticle_NotFound() {
        UpdateArticleDTO updateDTO = createTestUpdateDTO();
        when(articleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> articleService.updateArticle(999L, updateDTO));

        verify(articleRepository, times(1)).findById(999L);
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void testPatchArticleAmountAdd_Success() {
        Article existingArticle = createTestArticle();
        PatchAmountDTO patchDTO = createTestPatchDTO(25);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        ArticleResponseDTO result = articleService.patchArticleAmountAdd(1L, patchDTO);

        assertEquals(125, result.getAmount());
        assertEquals("Test Article", result.getName());
        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).save(existingArticle);
    }

    @Test
    void testPatchArticleAmountAdd_NotFound() {
        PatchAmountDTO patchDTO = createTestPatchDTO(25);
        when(articleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> articleService.patchArticleAmountAdd(999L, patchDTO));

        verify(articleRepository, times(1)).findById(999L);
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void testPatchArticleAmountRemove_Success() {
        Article existingArticle = createTestArticle();
        PatchAmountDTO patchDTO = createTestPatchDTO(25);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        ArticleResponseDTO result = articleService.patchArticleAmountRemove(1L, patchDTO);

        assertEquals(75, result.getAmount());
        assertEquals("Test Article", result.getName());
        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).save(existingArticle);
    }

    //if you try to remove more than current amount, it should return an error
    @Test
    void testPatchArticleAmountRemove_InsufficientStock() {
        Article existingArticle = createTestArticle();
        PatchAmountDTO patchDTO = createTestPatchDTO(150);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));

        assertThrows(IllegalArgumentException.class,
                () -> articleService.patchArticleAmountRemove(1L, patchDTO));

        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, never()).save(any(Article.class));
    }

    //makes sure you can remove all of the amount from an article without edgecase issues
    @Test
    void testPatchArticleAmountRemove_ExactAmount() {
        Article existingArticle = createTestArticle();
        PatchAmountDTO patchDTO = createTestPatchDTO(100);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        ArticleResponseDTO result = articleService.patchArticleAmountRemove(1L, patchDTO);

        assertEquals(0, result.getAmount());
        verify(articleRepository, times(1)).findById(1L);
        verify(articleRepository, times(1)).save(existingArticle);
    }

    @Test
    void testPatchArticleAmountRemove_NotFound() {
        PatchAmountDTO patchDTO = createTestPatchDTO(25);
        when(articleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> articleService.patchArticleAmountRemove(999L, patchDTO));

        verify(articleRepository, times(1)).findById(999L);
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void testLowStockDetection() {
        Article lowStockArticle = createTestArticle();
        lowStockArticle.setAmount(5);

        ArticleResponseDTO dto = new ArticleResponseDTO(lowStockArticle);

        assertTrue(dto.isLowStock());
    }

    @Test
    void testNormalStockDetection() {
        Article normalStockArticle = createTestArticle();
        normalStockArticle.setAmount(50);

        ArticleResponseDTO dto = new ArticleResponseDTO(normalStockArticle);

        assertFalse(dto.isLowStock());
    }

    //makes sure that when amount is equal to minimum amount, lowStock is true
    @Test
    void testBoundaryStockDetection() {
        Article boundaryStockArticle = createTestArticle();
        boundaryStockArticle.setAmount(10);

        ArticleResponseDTO dto = new ArticleResponseDTO(boundaryStockArticle);

        assertTrue(dto.isLowStock());
    }
}
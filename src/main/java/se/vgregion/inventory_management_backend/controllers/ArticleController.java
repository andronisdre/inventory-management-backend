package se.vgregion.inventory_management_backend.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.vgregion.inventory_management_backend.dto.ArticleResponseDTO;
import se.vgregion.inventory_management_backend.dto.CreateArticleDTO;
import se.vgregion.inventory_management_backend.dto.PatchAmountDTO;
import se.vgregion.inventory_management_backend.dto.UpdateArticleDTO;
import se.vgregion.inventory_management_backend.services.ArticleService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    //POST an article
    @PostMapping
    public ResponseEntity<ArticleResponseDTO> createArticle(@Valid @RequestBody CreateArticleDTO createArticleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.addArticle(createArticleDTO));
    }

    //GET ALL articles
    @GetMapping
    public ResponseEntity<?> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) boolean onlyLowStockArticles,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Page<ArticleResponseDTO> pageResult = articleService.getAllArticlesPaginated(
                page, size, search, onlyLowStockArticles, sortBy, sortDir
        );

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageResult.getContent());
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("pageSize", pageResult.getSize());
        response.put("hasNext", pageResult.hasNext());
        response.put("hasPrevious", pageResult.hasPrevious());

        return ResponseEntity.ok(response);
    }

    //GET article by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ArticleResponseDTO> getArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

    //Remove an article
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    //UPDATE article by id
    @PutMapping(value = "/{id}")
    public ResponseEntity<ArticleResponseDTO> updateArticle(@PathVariable Long id, @Valid @RequestBody UpdateArticleDTO updateArticleDTO) {
        return ResponseEntity.ok(articleService.updateArticle(id, updateArticleDTO));
    }

    //PATCH article amount by id, Add
    @PatchMapping("/{id}/changeAmount/add")
    public ResponseEntity<ArticleResponseDTO> patchArticleAmountAdd(@PathVariable Long id, @Valid @RequestBody PatchAmountDTO patchAmountDTO) {
        return ResponseEntity.ok(articleService.patchArticleAmountAdd(id, patchAmountDTO));
    }

    //PATCH article amount by id, Remove
    @PatchMapping("/{id}/changeAmount/subtract")
    public ResponseEntity<ArticleResponseDTO> patchArticleAmountRemove(@PathVariable Long id, @Valid @RequestBody PatchAmountDTO patchAmountDTO) {
        return ResponseEntity.ok(articleService.patchArticleAmountRemove(id, patchAmountDTO));
    }
}

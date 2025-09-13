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
import java.util.List;
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
            @RequestParam(required = false) String search
    ) {
            Page<ArticleResponseDTO> pageResult = articleService.getAllArticlesPaginated(page, size, search);

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

    //GET articles with low amount

    //add pagination
    @GetMapping("/lowAmount")
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticlesWithLowAmount() {
        return ResponseEntity.ok(articleService.getAllArticlesWithLowAmount());
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

    //PATCH article amount by id
    @PatchMapping("/{id}/changeAmount")
    public ResponseEntity<ArticleResponseDTO> patchArticleAmount(@PathVariable Long id, @Valid @RequestBody PatchAmountDTO patchAmountDTO) {
        return ResponseEntity.ok(articleService.patchArticleAmount(id, patchAmountDTO));
    }

    //add search by name function

    //add order by name, order by status functions
}

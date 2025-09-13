package se.vgregion.inventory_management_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.inventory_management_backend.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

package se.vgregion.inventory_management_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.vgregion.inventory_management_backend.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE " +
            "(:search IS NULL OR :search = '' OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:onlyLowStock = false OR a.amount <= a.minimumAmount)")
    Page<Article> findArticlesWithFilters(
            @Param("search") String search,
            @Param("onlyLowStock") boolean onlyLowStock,
            Pageable pageable
    );
}

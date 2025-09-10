package se.vgregion.inventory_management_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.inventory_management_backend.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}

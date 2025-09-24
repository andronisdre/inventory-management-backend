package se.vgregion.inventory_management_backend.dto;

import se.vgregion.inventory_management_backend.enums.ECategory;
import se.vgregion.inventory_management_backend.enums.EUnit;
import se.vgregion.inventory_management_backend.models.Article;

import java.time.LocalDateTime;

public class ArticleResponseDTO {
    private Long id;
    private String name;
    private int amount;
    private int minimumAmount;
    private EUnit unit;
    private ECategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean lowStock;

    public ArticleResponseDTO(Article article) {
        this.id = article.getId();
        this.name = article.getName();
        this.amount = article.getAmount();
        this.minimumAmount = article.getMinimumAmount();
        this.unit = article.getUnit();
        this.category = article.getCategory();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        //flag for if an article has low stock or not, only visible when fetching data, not stored.
        this.lowStock = article.getAmount() <= article.getMinimumAmount();
    }

    public ArticleResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public int getMinimumAmount() { return minimumAmount; }
    public void setMinimumAmount(int minimumAmount) { this.minimumAmount = minimumAmount; }

    public EUnit getUnit() { return unit; }
    public void setUnit(EUnit unit) { this.unit = unit; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isLowStock() { return lowStock; }
    public void setLowStock(boolean lowStock) { this.lowStock = lowStock; }

    public ECategory getCategory() {
        return category;
    }

    public void setCategory(ECategory category) {
        this.category = category;
    }
}

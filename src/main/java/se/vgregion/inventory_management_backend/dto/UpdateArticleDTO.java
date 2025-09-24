package se.vgregion.inventory_management_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import se.vgregion.inventory_management_backend.enums.ECategory;
import se.vgregion.inventory_management_backend.enums.EUnit;

public class UpdateArticleDTO {
    @Size(max = 50, message = "Name cannot be more than 100 characters!")
    private String name;

    @Min(value = 0, message = "Amount cannot be negative")
    @Max(value = 100000000, message = "Amount cannot exceed 100,000,000!")
    private Integer amount;

    @Min(value = 0, message = "Minimum amount cannot be negative!")
    @Max(value = 100000000, message = "Minimum amount cannot exceed 100,000,000!")
    private Integer minimumAmount;

    private EUnit unit;

    @NotNull(message = "Category is required!")
    private ECategory category;

    public UpdateArticleDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public Integer getMinimumAmount() { return minimumAmount; }
    public void setMinimumAmount(Integer minimumAmount) { this.minimumAmount = minimumAmount; }

    public EUnit getUnit() { return unit; }
    public void setUnit(EUnit unit) { this.unit = unit; }

    public ECategory getCategory() {
        return category;
    }

    public void setCategory(ECategory category) {
        this.category = category;
    }
}

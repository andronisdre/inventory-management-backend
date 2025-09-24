package se.vgregion.inventory_management_backend.dto;

import jakarta.validation.constraints.*;
import se.vgregion.inventory_management_backend.enums.ECategory;
import se.vgregion.inventory_management_backend.enums.EUnit;

public class CreateArticleDTO {
    //Validation annotations such as NotNull Size and NotNull to ensure that when using @Valid in controller functions, A user has to follow these rules for input.
    @NotBlank(message = "Name is required!")
    @Size(max = 50, message = "Name cannot be more than 100 characters!")
    private String name;

    //max and min annotations to ensure that the user cant make negative amount articles and that numbers aren't unreasonably big. the values of max can be adjusted
    @Min(value = 0, message = "Amount cannot be negative")
    @Max(value = 100000000, message = "Amount cannot exceed 100,000,000!")
    @NotNull(message = "Amount is required!")
    private Integer amount;

    @Min(value = 0, message = "Minimum amount cannot be negative!")
    @Max(value = 100000000, message = "Minimum amount cannot exceed 100,000,000!")
    @NotNull(message = "Minimum amount is required!")
    private Integer minimumAmount;

    @NotNull(message = "Unit is required!")
    private EUnit unit;

    @NotNull(message = "Category is required!")
    private ECategory category;

    public CreateArticleDTO() {}

    public CreateArticleDTO(String name, Integer amount, Integer minimumAmount, EUnit unit, ECategory category) {
        this.name = name;
        this.amount = amount;
        this.minimumAmount = minimumAmount;
        this.unit = unit;
        this.category = category;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public int getMinimumAmount() { return minimumAmount; }
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

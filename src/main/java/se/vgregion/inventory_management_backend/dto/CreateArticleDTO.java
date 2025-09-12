package se.vgregion.inventory_management_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import se.vgregion.inventory_management_backend.enums.EUnit;

public class CreateArticleDTO {
    //Validation annotations such as NotNull Size and NotNull to ensure that when using @Valid in controller functions, A user has to follow these rules for input.
    @NotBlank(message = "Name is required!")
    @Size(max = 50, message = "Name cannot be more than 100 characters!")
    private String name;

    @NotNull(message = "Amount is required!")
    @Min(value = 0, message = "Amount cannot be negative!")
    private Integer amount;

    @NotNull(message = "Minimum amount is required!")
    @Min(value = 0, message = "Minimum amount cannot be negative!")
    private Integer minimumAmount;

    @NotNull(message = "Unit is required!")
    private EUnit unit;

    public CreateArticleDTO() {}

    public CreateArticleDTO(String name, Integer amount, Integer minimumAmount, EUnit unit) {
        this.name = name;
        this.amount = amount;
        this.minimumAmount = minimumAmount;
        this.unit = unit;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public int getMinimumAmount() { return minimumAmount; }
    public void setMinimumAmount(Integer minimumAmount) { this.minimumAmount = minimumAmount; }

    public EUnit getUnit() { return unit; }
    public void setUnit(EUnit unit) { this.unit = unit; }
}

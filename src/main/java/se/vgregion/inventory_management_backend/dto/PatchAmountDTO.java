package se.vgregion.inventory_management_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PatchAmountDTO {
    @NotNull(message = "Amount is required!")
    @Min(value = 0, message = "Amount cannot be negative!")
    @Max(value = 100000000, message = "Amount cannot exceed 100,000,000!")
    private Integer amount;

    public PatchAmountDTO() {}

    public PatchAmountDTO(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
}

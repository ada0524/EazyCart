package org.example.onlineshoppingcontent.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemRequest {
    @NotBlank(message = "Product ID cannot be blank")
    private long productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}

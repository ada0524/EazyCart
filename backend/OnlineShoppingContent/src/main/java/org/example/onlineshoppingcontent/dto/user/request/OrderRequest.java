package org.example.onlineshoppingcontent.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.onlineshoppingcontent.model.OrderItem;

import javax.validation.constraints.NotBlank;
import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    @NotBlank(message = "quantity cannot be blank")
    @Valid
    private List<OrderItemRequest> order;
}

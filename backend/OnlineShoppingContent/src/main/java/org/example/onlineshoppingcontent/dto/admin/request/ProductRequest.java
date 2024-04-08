package org.example.onlineshoppingcontent.dto.admin.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private double wholesalePrice;
    private double retailPrice;
    private int quantity;
}
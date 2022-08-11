package com.therap.supply.chain.admin.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductHistoryDTO {

    private Long id;

    private String status;

    private Long dealerId;

    private Long updatedProduct;

    private Long stockInInventory;

    private ProductDTO productDTO;

}

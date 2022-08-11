package com.therap.supply.chain.admin.dto;

import com.therap.supply.chain.admin.entities.ProductHistory;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 100, message = "product's name must be min of 3 to 100 character")
    private String name;

    @NotBlank
    @Size(min = 3, max = 100, message = "dealer's description must be min of 3 to 100 character")
    private String description;

    @Min(value = 0)
    private Double price;

    @Min(value = 0)
    private Double discountPrice;

    @Min(value = 0)
    private Long total;

    @Min(value = 0)
    private Long addProduct;

    private Long stock;

    private Long discount;

    private boolean isEnable;

    private AttachmentDTO attachmentDTO;

    private List<ProductHistoryDTO> productHistoryDTOS;

    private List<RequisitionProductHistoryDTO> requisitionProductHistoryDTOS;

}

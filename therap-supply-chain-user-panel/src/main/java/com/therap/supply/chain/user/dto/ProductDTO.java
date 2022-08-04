package com.therap.supply.chain.user.dto;

import lombok.*;

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

    private String name;

    private String description;

    private Double price;

    private Double discountPrice;

    private Long total;

    private Long addProduct;

    private Long stock;

    private Long discount;

    private boolean isEnable;

    private AttachmentDTO attachmentDTO;

    private List<RequisitionProductHistoryDTO> requisitionProductHistoryDTOS;

}

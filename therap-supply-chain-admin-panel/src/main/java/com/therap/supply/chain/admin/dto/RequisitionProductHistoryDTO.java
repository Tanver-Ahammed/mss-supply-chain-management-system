package com.therap.supply.chain.admin.dto;

import lombok.*;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequisitionProductHistoryDTO {

    private Long id;

    private String reqProductName;

    private boolean isDeleted;

    private Long quantity;

    // here set the price because price is fluctuating
    private Double price;

    private RequisitionDTO requisitionDTO;

    private ProductDTO productDTO;

}

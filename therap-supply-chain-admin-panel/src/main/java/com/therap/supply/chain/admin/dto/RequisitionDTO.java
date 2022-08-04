package com.therap.supply.chain.admin.dto;

import lombok.*;

import java.util.Date;
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
public class RequisitionDTO {

    private Long id;

    private Date date;

    private Double totalAmountPrice;

    private Double paidAmount;

    private boolean isPaid;

    private boolean isSubmittedByDealer;

    private String isApproveByInventoryManager;

    private String isApproveByAccountManager;

    private String isDelivered;

    private String verificationCode;

    private DealerDTO dealerDTO;

    private List<RequisitionProductHistoryDTO> requisitionProductHistoryDTOS;

    private List<PaymentHistoryDTO> paymentHistoryDTOS;

}

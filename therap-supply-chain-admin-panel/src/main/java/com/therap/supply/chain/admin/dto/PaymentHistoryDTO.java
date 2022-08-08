package com.therap.supply.chain.admin.dto;

import lombok.*;

import java.util.Date;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaymentHistoryDTO {

    private Long id;

    private Date date;

    // bkash, bank, accountant, delivery manager
    private String medium;

    // bank acc no, bkash no, accountant/delivery manager name
    private String accountNo;

    private String transactionId;

    private Double amount;

    private String isApproveByAccountManager;

    private RequisitionDTO requisitionDTO;

}

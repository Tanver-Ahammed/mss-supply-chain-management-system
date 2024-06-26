package com.therap.supply.chain.admin.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Entity
@Table(name = "payment_histories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@ToString
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    // bkash, bank, accountant, delivery manager
    private String medium;

    // bank acc no, bkash no, accountant/delivery manager name
    private String accountNo;

    private String transactionId;

    private Double amount;

    private String isApproveByAccountManager;

    @ManyToOne
    @JoinColumn(name = "requisition_id_fk", referencedColumnName = "id")
    private Requisition requisition;

    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Attachment attachment;

}

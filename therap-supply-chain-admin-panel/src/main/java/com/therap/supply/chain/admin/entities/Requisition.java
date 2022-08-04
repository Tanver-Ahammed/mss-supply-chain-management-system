package com.therap.supply.chain.admin.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@Entity
@Table(name = "requisitions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Requisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "dealer_id_fk", referencedColumnName = "id")
    private Dealer dealer;

    @OneToMany(mappedBy = "requisition", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RequisitionProductHistory> requisitionProductHistories;

    @OneToMany(mappedBy = "requisition", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PaymentHistory> paymentHistories;


}

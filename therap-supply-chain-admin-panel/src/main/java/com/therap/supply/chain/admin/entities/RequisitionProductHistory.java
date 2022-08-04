package com.therap.supply.chain.admin.entities;

import lombok.*;

import javax.persistence.*;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@Entity
@Table(name = "requisition_product_histories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequisitionProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reqProductName;

    private boolean isDeleted;

    private Long quantity;

    // here set the price because price is fluctuating
    private Double price;

    @ManyToOne
    @JoinColumn(name = "requisition_id_fk", referencedColumnName = "id")
    private Requisition requisition;

    @ManyToOne
    @JoinColumn(name = "product_id_fk", referencedColumnName = "id")
    private Product product;

}

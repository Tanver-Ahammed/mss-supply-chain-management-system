package com.therap.supply.chain.user.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne
    private Attachment attachment;

    @OneToMany(mappedBy = "product", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RequisitionProductHistory> requisitionProductHistories;

}

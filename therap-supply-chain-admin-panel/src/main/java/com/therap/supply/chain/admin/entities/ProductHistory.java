package com.therap.supply.chain.admin.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_histories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    private Long dealerId;

    private Long updatedProduct;

    private Long stockInInventory;

    @ManyToOne
    @JoinColumn(name = "product_id_fk", referencedColumnName = "id")
    private Product product;

}

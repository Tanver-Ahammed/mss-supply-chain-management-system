package com.therap.supply.chain.admin.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Entity
@Table(name = "dealers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Dealer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String contact;

    @Column(unique = true)
    private String email;

    private Integer age;

    private String Address;

    private String password;

    private boolean isActivate;

    private String isActiveByDealerApprover;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name = "dealers_attachments", joinColumns = {
//            @JoinColumn(name = "dealer_id_fk", referencedColumnName = "id")},
//            inverseJoinColumns = {
//                    @JoinColumn(name = "attachment_id_fk", referencedColumnName = "id")})
    private List<Attachment> attachments = new ArrayList<>();

    private String verificationCode;

    private String role;

    @OneToMany(mappedBy = "dealer",orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Requisition> requisitions;

}

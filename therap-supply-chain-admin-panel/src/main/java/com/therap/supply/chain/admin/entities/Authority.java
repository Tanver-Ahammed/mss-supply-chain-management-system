package com.therap.supply.chain.admin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "authorities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@ToString
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String contact;

    @Column(unique = true)
    private String email;

    private int age;

    private String bloodGroup;

    private String address;

    private Boolean isEnable;

    private String password;

    //    private String role;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonIgnore
    @JoinTable(name = "authorities_roles",
            joinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    private String verificationCode;

    private String activity;

    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_id_fk", referencedColumnName = "id")
    private Attachment attachment;

}

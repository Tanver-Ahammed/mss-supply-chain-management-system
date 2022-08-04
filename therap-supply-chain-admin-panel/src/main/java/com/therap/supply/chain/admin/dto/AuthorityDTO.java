package com.therap.supply.chain.admin.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthorityDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 50, message = "authority's name must be min of 3 to 50 character")
    private String name;

    @NotBlank
    private String contact;

    private String email;

    @Min(value = 18, message = "blood donor's age must be minimum 18 years")
    @Max(value = 100, message = "blood donor's age must be maximum 50 years")
    private int age;

    private String bloodGroup;

    @NotBlank
    private String address;

    private String password;

    private Boolean isEnable;

//    @NotBlank
//    private String role;

    private Set<RoleDTO> roleDTOS;

    private String verificationCode;

    private String activity;

    private AttachmentDTO attachmentDTO;

}

package com.therap.supply.chain.admin.dto;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DealerDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 100, message = "dealer's name must be min of 3 to 100 character")
    private String name;

    @NotBlank
    private String contact;

    @NotBlank
    @Email
    @UniqueElements
    private String email;

    @Min(value = 18, message = "blood donor's age must be minimum 18 years")
    @Max(value = 60, message = "blood donor's age must be maximum 60 years")
    private Integer age;

    @NotBlank
    private String address;

    private String password;

    private String verificationCode;

    private String role;

    private boolean isActivate;

    private String isActiveByDealerApprover;

    private List<AttachmentDTO> attachmentDTOS = new ArrayList<>();

    private List<RequisitionDTO> requisitionDTOS;

}

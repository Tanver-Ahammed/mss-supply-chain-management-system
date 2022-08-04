package com.therap.supply.chain.user.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 * */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AttachmentDTO {

    private Long id;

    private String attachmentPath;

    private String attachmentName;

    @NotEmpty
    private String attachmentFileName;

    @NotEmpty
    private String attachmentType;

}

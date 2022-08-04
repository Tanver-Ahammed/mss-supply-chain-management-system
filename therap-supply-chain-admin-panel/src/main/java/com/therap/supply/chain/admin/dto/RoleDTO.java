package com.therap.supply.chain.admin.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoleDTO implements Comparable<RoleDTO> {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 25, message = "role's must be min of 2 to 25 character")
    private String role;

    @NotBlank
    @Size(min = 2, max = 25, message = "role name must be min of 2 to 25 character")
    private String roleName;

    @Override
    public int compareTo(RoleDTO roleDTO) {
        if (id > roleDTO.id)
            return 1;
        else
            return -1;
    }
}

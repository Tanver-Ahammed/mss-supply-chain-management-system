package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.RoleDTO;

import java.util.Set;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

public interface RoleService {

    RoleDTO addRole(RoleDTO RoleDTO);

    RoleDTO getSingleRole(Long RoleId);

    Set<RoleDTO> getAllRoles();

    RoleDTO updateRole(Long RoleId, RoleDTO RoleDTO);

}

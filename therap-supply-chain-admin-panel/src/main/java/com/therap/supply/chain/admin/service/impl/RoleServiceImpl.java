package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.dto.RoleDTO;
import com.therap.supply.chain.admin.entities.Role;
import com.therap.supply.chain.admin.exception.ResourceNotFoundException;
import com.therap.supply.chain.admin.repository.RoleRepository;
import com.therap.supply.chain.admin.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public RoleDTO addRole(RoleDTO roleDTO) {
        return this.modelMapper
                .map(this.roleRepository.save(this.modelMapper.map(roleDTO, Role.class)),
                        RoleDTO.class);
    }

    @Override
    public RoleDTO getSingleRole(Long roleId) {
        return this.modelMapper.map(this.roleRepository.findById(roleId).orElseThrow(() ->
                new ResourceNotFoundException("role", "id", roleId)), RoleDTO.class);
    }

    @Override
    public Set<RoleDTO> getAllRoles() {
        return this.roleRepository
                .findAll()
                .stream()
                .map(role -> this.modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public RoleDTO updateRole(Long RoleId, RoleDTO RoleDTO) {
        return null;
    }
}

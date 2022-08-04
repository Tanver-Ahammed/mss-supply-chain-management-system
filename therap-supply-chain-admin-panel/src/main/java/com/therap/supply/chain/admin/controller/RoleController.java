package com.therap.supply.chain.admin.controller;

import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.dto.RoleDTO;
import com.therap.supply.chain.admin.service.impl.AuthorityServiceImpl;
import com.therap.supply.chain.admin.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/role")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private AuthorityServiceImpl authorityService;

    @GetMapping(path = "/add")
    public String addRole(Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        model.addAttribute("roleDTO", new RoleDTO());
        model.addAttribute("message", "");
        return "role/add-role";
    }

    @PostMapping(path = "/save")
    public String saveRole(@Valid @ModelAttribute("roleDTO") RoleDTO roleDTO, BindingResult result,
                           Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        this.roleService.addRole(roleDTO);

        if (result.hasErrors()) {
            model.addAttribute("roleDTO", roleDTO);
            model.addAttribute("message", "");
            return "role/add-role";
        }

        model.addAttribute("roleDTO", new RoleDTO());
        model.addAttribute("message", "role is successfully added...");
        return "role/add-role";
    }

}

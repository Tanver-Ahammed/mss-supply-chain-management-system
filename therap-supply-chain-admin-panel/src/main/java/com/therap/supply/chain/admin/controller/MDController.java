package com.therap.supply.chain.admin.controller;

import com.therap.supply.chain.admin.config.AppConstants;
import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.dto.RoleDTO;
import com.therap.supply.chain.admin.service.impl.AuthorityServiceImpl;
import com.therap.supply.chain.admin.service.impl.FileServiceImpl;
import com.therap.supply.chain.admin.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Controller
@RequestMapping(path = "/md/authority")
public class MDController {

    @Autowired
    private AuthorityServiceImpl authorityService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    private AuthorityDTO authority;

    @ModelAttribute
    public void setAuthorityDTOIfLoggedIn(Principal principal) {
        authority =  this.authorityService.getAuthorityDTOIfLoggedIn(principal);
    }

    @GetMapping(path = "/add")
    public String addAuthority(Principal principal, Model model) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        model.addAttribute("authority", authority);

        Set<RoleDTO> roleDTOS = this.roleService.getAllRoles();
        model.addAttribute("roleDTOS", roleDTOS);
        model.addAttribute("authorityDTO", new AuthorityDTO());
        model.addAttribute("message", "");
        return "authority/registration-authority";
    }

    @PostMapping(path = "/save")
    public String registrationAuthority(@Valid @ModelAttribute("authorityDTO") AuthorityDTO authorityDTO, BindingResult result,
                                        @RequestParam("userRole") Long[] userRoles,
                                        @RequestParam("authorityImage") MultipartFile authorityImage,
                                        Model model, Principal principal) throws IOException {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        model.addAttribute("authority", authority);

        // passing role
        model.addAttribute("authorityDTO", authorityDTO);
        Set<RoleDTO> roleDTOS = this.roleService.getAllRoles();
        model.addAttribute("roleDTOS", roleDTOS);

        if (result.hasErrors()) {
            return "authority/registration-authority";
        }

        // unique identity email and contact
        Boolean isDuplicateEmailOrContact = this.authorityService
                .isDuplicateAuthorityByEmailOrContact(authorityDTO.getEmail(), authorityDTO.getContact());
        if (isDuplicateEmailOrContact) {
            model.addAttribute("dangerMessage", "email or contact already exist!!");
            return "authority/registration-authority";
        }

        AuthorityDTO resultAuthorityDTO = this.authorityService
                .registrationAuthority(authorityDTO, userRoles, authorityImage);

        // if image is not add
        if (resultAuthorityDTO == null) {
            model.addAttribute("authorityDTO", authorityDTO);
            model.addAttribute("message", "please enter authority image...");
            return "authority/registration-authority";
        }

        model.addAttribute("authorityDTO", new AuthorityDTO());
        model.addAttribute("message", "authority is successfully added...");
        return "authority/registration-authority";
    }


    //get all status authority
    @GetMapping(path = "/status/{activity}")
    public String getAllAuthoritiesByStatus(@PathVariable("activity") String activityStatus,
                                            Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        model.addAttribute("authority", authority);

        // send all roles
        Set<RoleDTO> roleDTOS = this.roleService.getAllRoles();
        System.out.println(roleDTOS.size());
        model.addAttribute("roleDTOS", roleDTOS);

        List<AuthorityDTO> authorityDTOS;
        if (activityStatus.equalsIgnoreCase("all"))
            authorityDTOS = this.authorityService.getAllAuthorities();
        else
            authorityDTOS = this.authorityService.getAllAuthoritiesByStatus(activityStatus.toUpperCase());


        model.addAttribute("authorityDTOS", authorityDTOS);
        model.addAttribute("message", "");
        return "authority/status/show-all-authorities-status";
    }

    //get all status authority
    @GetMapping(path = "/role/{roleId}")
    public String getAllAuthoritiesByRole(@PathVariable("roleId") Long roleId,
                                          Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        model.addAttribute("authority", authority);

        // send all roles
        Set<RoleDTO> roleDTOS = this.roleService.getAllRoles();
        model.addAttribute("roleDTOS", roleDTOS);

        List<AuthorityDTO> authorityDTOS;
        if (roleId == 0)
            authorityDTOS = this.authorityService.getAllAuthorities();
        else
            authorityDTOS = this.authorityService.getAllAuthoritiesByRole(roleId);

        model.addAttribute("authorityDTOS", authorityDTOS);
        model.addAttribute("message", "");
        return "authority/status/show-all-authorities-status";
    }


    // get a single authority
    @GetMapping("/status/get/{authorityId}")
    public String getSingleAuthority(@PathVariable("authorityId") long authorityId,
                                     Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        model.addAttribute("authority", authority);

        // is controller managing director
        Set<Boolean> isLoginPersonMD = authority.getRoleDTOS().stream().map(roleDTO ->
                roleDTO.getRole().equalsIgnoreCase("MD")).collect(Collectors.toSet());
        if (isLoginPersonMD.size() > 0)
            model.addAttribute("isLoginPersonMD", "true");

        AuthorityDTO authorityDTO = this.authorityService.getSingleAuthorityById(authorityId);
        model.addAttribute("authorityDTO", authorityDTO);
        model.addAttribute("message", "");
        return "authority/status/show-status-change-authority";
    }

    // authority approve
    @GetMapping(path = "/status/{activityStatus}/{authorityId}")
    public String authorityChangeStatus(@PathVariable("authorityId") Long authorityId,
                                        @PathVariable("activityStatus") String activityStatus) throws IOException {
        AuthorityDTO authorityDTO = this.authorityService.getSingleAuthorityById(authorityId);
        if (activityStatus.equalsIgnoreCase(AppConstants.active))
            authorityDTO.setActivity(AppConstants.active);
        else if (activityStatus.equalsIgnoreCase(AppConstants.inactive))
            authorityDTO.setActivity(AppConstants.inactive);
        else if (activityStatus.equalsIgnoreCase(AppConstants.cancel))
            authorityDTO.setActivity(AppConstants.cancel);


        this.authorityService.updateAuthority(authorityId, authorityDTO);
        return "redirect:/md/authority/status/all";
    }

    // edit authority details
    @GetMapping(path = "/edit/role/{authorityId}")
    public String editAuthorityRole(@PathVariable("authorityId") long authorityId,
                                    Principal principal, Model model) {

        // get logged-in username
        if (principal == null)
            return "authority/login";
        model.addAttribute("authority", authority);

        AuthorityDTO authorityDTO = this.authorityService.getSingleAuthorityById(authorityId);
        System.err.println(authorityId);
        model.addAttribute("authorityDTO", authorityDTO);
        Set<RoleDTO> roleDTOS = this.roleService.getAllRoles();
        model.addAttribute("roleDTOS", roleDTOS);
        model.addAttribute("message", "");
        return "authority/update-authority-role";
    }

    // update authority
    @PostMapping("/update/role/{authorityId}")
    public String updateAuthoritySuccess(@Valid @ModelAttribute(value = "authorityDTO") AuthorityDTO authorityDTO, BindingResult result,
                                         @RequestParam(value = "userRole", required = false) Long[] userRoles,
                                         @PathVariable("authorityId") long authorityId,
                                         Model model, Principal principal) throws IOException {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        model.addAttribute("authority", authority);

        if (result.hasErrors()) {
            model.addAttribute("authorityDTO", authorityDTO);
            return "authority/update-authority-role";
        }
        authorityDTO = this.authorityService
                .updateAuthority(authorityId, authorityDTO, userRoles, null);    // save the authority database
        model.addAttribute("authorityDTO", new AuthorityDTO());
        return "redirect:/md/authority/status/all";
    }


}

package com.therap.supply.chain.admin.controller;

import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.service.impl.AuthorityServiceImpl;
import com.therap.supply.chain.admin.service.impl.FileServiceImpl;
import com.therap.supply.chain.admin.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Controller
@RequestMapping(path = "/authority")
public class AuthorityController {

    @Autowired
    private AuthorityServiceImpl authorityService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    //get all authority
    @GetMapping(path = "/all")
    public String showAllAuthorities(Model model, Principal principal) {

        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        List<AuthorityDTO> authorityDTOS = this.authorityService.getAllAuthorities();
        model.addAttribute("authorityDTOS", authorityDTOS);
        model.addAttribute("message", "");
        return "authority/show-all-authorities";
    }

    // get a single authority
    @GetMapping("/get/{authorityId}")
    public String getSingleAuthority(@PathVariable("authorityId") long authorityId,
                                     Principal principal, Model model) {

        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        AuthorityDTO authorityDTO = this.authorityService.getSingleAuthorityById(authorityId);
        model.addAttribute("authorityDTO", authorityDTO);
        model.addAttribute("message", "");
        return "authority/show-single-authority";
    }

    // view profile
    @GetMapping(path = "/my/profile")
    public String myProfile(Principal principal, Model model) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        AuthorityDTO authorityDTO = this.authorityService.getSingleAuthorityById(authority.getId());

        model.addAttribute("authorityDTO", authorityDTO);

        model.addAttribute("message", "");
        model.addAttribute("dangerMessage", "");
        return "authority/profile-authority";
    }

    // edit authority details
    @GetMapping(path = "/edit/my/profile")
    public String editAuthority(Principal principal, Model model) {

        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        AuthorityDTO authorityDTO = this.authorityService.getSingleAuthorityById(authority.getId());
        authorityDTO.setPassword(null);
        authorityDTO.setRoleDTOS(null);

        model.addAttribute("authorityDTO", authorityDTO);

        model.addAttribute("message", "");
        model.addAttribute("dangerMessage", "");
        return "authority/update-profile";
    }

    // update authority
    @PostMapping("/update")
    public String updateAuthoritySuccess(@Valid @ModelAttribute(value = "authorityDTO") AuthorityDTO authorityDTO, BindingResult result,
                                         @RequestParam(value = "authorityImage", required = false) MultipartFile authorityImage,
                                         Model model, Principal principal) throws IOException {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        if (result.hasErrors()) {
            model.addAttribute("authorityDTO", authorityDTO);
            return "authority/update-profile";
        }
        authorityDTO = this.authorityService
                .updateAuthority(authority.getId(), authorityDTO, new Long[]{}, authorityImage);    // save the authority database
        model.addAttribute("authorityDTO", new AuthorityDTO());
        return "redirect:/authority/my/profile";
    }

    // get image
    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response,
            Principal principal, Model model) throws IOException {
        // get logged-in username
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
//        if (authority == null)
//            return "authority/login";
        model.addAttribute("authority", authority);

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}

package com.therap.supply.chain.admin.controller;

import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.service.impl.AuthorityServiceImpl;
import com.therap.supply.chain.admin.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@CrossOrigin
@Controller
public class HomeController {

    @Autowired
    private AuthorityServiceImpl authorityService;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    @GetMapping(path = "/index")
    public String index() {
        return "test/index";
    }

    @GetMapping(path = {"/", "/home"})
    public String home(Principal principal, Model model) {
        if (principal != null) {
            // get logged-in username
            AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
            model.addAttribute("authority", authority);
            return "authority/home";
        }

        return "home";
    }

    // email verify it's always logged out
    @GetMapping(path = "/auth/verify/{authorityId}/{verificationCode}")
    public String verifyingAuthority(@PathVariable("authorityId") Long authorityId,
                                     @PathVariable("verificationCode") String verificationCode,
                                     Model model) {
        model.addAttribute("authority", "null");
        AuthorityDTO authorityDTO = this.authorityService.verifyingAuthority(authorityId, verificationCode);
        if (authorityDTO != null) {
            model.addAttribute("message", "Your email verifying successfully....");
            model.addAttribute("authorityDTO", authorityDTO);
            return "authority/set-authority-password";
        } else {
            model.addAttribute("message", "Your email don't verifying successfully....");
            return "home";
        }
    }

    // set password
    @PostMapping(path = "/auth/set/password")
    public String setAuthorityPassword(AuthorityDTO authorityDTO, Model model, Principal principal) {
        if (principal != null) {
            // get logged-in username
            AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
            model.addAttribute("authority", authority);
        } else
            model.addAttribute("authority", "null");

        Boolean isSetPassword = this.authorityService.setPassword(authorityDTO.getId(), authorityDTO);
        if (isSetPassword) {
            model.addAttribute("message", "set you password successfully");
            return "authority/login";
        } else {
            model.addAttribute("dangerMessage", "don't you password. Your are not valid");
            return "authority/home";
        }
    }

    // forget password
    @GetMapping(path = "/forget/password")
    public String forgetPassword(Model model) {
        model.addAttribute("message", "Please enter your password");
        return "authority/forget-password";
    }

    // forget password
    @PostMapping(path = "/forget/password")
    public String forgetPasswordSave(@RequestParam("email") String email,
                                     Model model) {
        Boolean isHeAuthority = this.authorityService.forgetAuthorityPassword(email);
        if (isHeAuthority) {
            model.addAttribute("message", "Please check your email and set password...");
            model.addAttribute("dangerMessage", "");
        } else {
            model.addAttribute("message", "");
            model.addAttribute("dangerMessage", "You are not valid authority!!!");
        }
        return "authority/forget-password";
    }

    // forget password set
    @GetMapping(path = "/forget/password/{authorityId}/{verificationCode}")
    public String setForgetPassword(@PathVariable("authorityId") Long authorityId,
                                    @PathVariable("verificationCode") String verificationCode,
                                    Model model) {
        AuthorityDTO authorityDTO = this.authorityService.verifyingAuthority(authorityId, verificationCode);
        if (authorityDTO != null) {
            model.addAttribute("message", "Set the password....");
            model.addAttribute("authorityDTO", authorityDTO);
            return "authority/set-forget-password";
        } else {
            model.addAttribute("message", "Your email don't verifying successfully....");
            return "home";
        }
    }

    // forget password set
    @PostMapping(path = "/set/forget/password")
    public String setForgetPassword(@ModelAttribute("authorityDTO") AuthorityDTO authorityDTO,
                                    Model model) {
        Boolean isSetPassword = this.authorityService.setPassword(authorityDTO.getId(), authorityDTO);
        if (isSetPassword != null) {
            model.addAttribute("message", "Your email verifying successfully and login....");
            model.addAttribute("authorityDTO", new AuthorityDTO());
            return "authority/login";
        } else {
            model.addAttribute("message", "Your email don't verifying successfully....");
            return "redirect:/home";
        }
    }

    // login authority
    @GetMapping(path = "auth/login")
    public String authorityLogin(Model model) {
        model.addAttribute("message", "");
        return "authority/login";
    }

    @GetMapping(path = "/developer")
    public String developer(Principal principal, Model model) {
        if (principal != null) {
            // get logged-in username
            AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
            model.addAttribute("authority", authority);

            return "authority/developer";
        }

        return "developer";
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

package com.therap.supply.chain.user.controller;

import com.therap.supply.chain.user.dto.DealerDTO;
import com.therap.supply.chain.user.service.impl.DealerServiceImpl;
import com.therap.supply.chain.user.service.impl.FileServiceImpl;
import com.therap.supply.chain.user.service.impl.RequisitionServiceImpl;
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

@Controller
public class HomeController {

    @Autowired
    private DealerServiceImpl dealerService;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    // login dealer
    @GetMapping(path = "dealer/login")
    public String dealerLogin(Model model) {
        model.addAttribute("message", "");
        return "dealer/login";
    }

    @GetMapping(path = {"/", "/home"})
    public String home(Principal principal, Model model) {
        // get logged-in username
        if (principal != null) {
            DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
            model.addAttribute("dealer", dealer);
            model.addAttribute("totalProduct", this.requisitionService
                    .getLastRequisitionByDealer(dealer.getId()).getRequisitionProductHistoryDTOS().size());
            return "dealer/home";
        }
        model.addAttribute("message", "");
        return "home";
    }

    // email verify it's always logged out
    @GetMapping(path = "/verify/{dealerId}/{verificationCode}")
    public String verifyingDealer(@PathVariable("dealerId") Long dealerId,
                                  @PathVariable("verificationCode") String verificationCode,
                                  Model model) {
        model.addAttribute("dealer", "null");
        DealerDTO dealerDTO = this.dealerService.verifyingDealer(dealerId, verificationCode);
        if (dealerDTO != null) {
            model.addAttribute("message", "Your email verifying successfully and login....");
            model.addAttribute("dealerDTO", dealerDTO);
            return "dealer/login";
        } else {
            model.addAttribute("message", "Your email don't verifying successfully....");
            return "home";
        }
    }

    // forget password
    @GetMapping(path = "/forget/password")
    public String forgetPassword(Model model) {
        model.addAttribute("message", "Please enter your password");
        return "dealer/forget-password";
    }

    // forget password
    @PostMapping(path = "/forget/password")
    public String forgetPasswordSave(@RequestParam("email") String email,
                                     Model model) {
        Boolean isHeDealer = this.dealerService.forgetDealerPassword(email);
        if (isHeDealer)
            model.addAttribute("message", "Please check your email and set password...");
        else
            model.addAttribute("message", "You are not valid dealer...");
        return "dealer/forget-password";
    }

    // forget password set
    @GetMapping(path = "/forget/password/{dealerId}/{verificationCode}")
    public String setForgetPassword(@PathVariable("dealerId") Long dealerId,
                                    @PathVariable("verificationCode") String verificationCode,
                                    Model model) {
        DealerDTO dealerDTO = this.dealerService.verifyingDealer(dealerId, verificationCode);
        if (dealerDTO != null) {
            model.addAttribute("message", "Set the password....");
            model.addAttribute("dealerDTO", dealerDTO);
            return "dealer/set-forget-password";
        } else {
            model.addAttribute("message", "Your email don't verifying successfully....");
            return "home";
        }
    }

    // forget password set
    @PostMapping(path = "/set/forget/password")
    public String setForgetPassword(@ModelAttribute("dealerDTO") DealerDTO dealerDTO,
                                    Model model) {
        Boolean isSetPassword = this.dealerService.setForgetPassword(dealerDTO.getId(), dealerDTO);
        if (isSetPassword != null) {
            model.addAttribute("message", "Your email verifying successfully and login....");
            model.addAttribute("dealerDTO", new DealerDTO());
            return "dealer/login";
        } else {
            model.addAttribute("message", "Your email don't verifying successfully....");
            return "redirect:/home";
        }
    }

    @GetMapping(path = "/developer")
    public String developer(Principal principal, Model model) {
        // get logged-in username
        if (principal != null) {
            DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
            model.addAttribute("dealer", dealer);
            model.addAttribute("totalProduct", this.requisitionService
                    .getLastRequisitionByDealer(dealer.getId()).getRequisitionProductHistoryDTOS().size());
            return "dealer/developer";
        }

        return "developer";
    }

    // get image
    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response, Principal principal, Model model
    ) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}

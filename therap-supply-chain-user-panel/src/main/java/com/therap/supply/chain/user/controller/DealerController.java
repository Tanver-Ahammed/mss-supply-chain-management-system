package com.therap.supply.chain.user.controller;

import com.therap.supply.chain.user.dto.DealerDTO;
import com.therap.supply.chain.user.service.DealerService;
import com.therap.supply.chain.user.service.impl.DealerServiceImpl;
import com.therap.supply.chain.user.service.impl.RequisitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Set;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Controller
@RequestMapping(path = "/dealer")
public class DealerController {

    @Autowired
    private DealerServiceImpl dealerService;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @GetMapping(path = "/add")
    public String addDealer(Model model, Principal principal) {

        model.addAttribute("dealerDTO", new DealerDTO());
        model.addAttribute("message", "");

        // get logged-in username
        if (principal != null) {
            DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
            model.addAttribute("dealer", dealer);
            model.addAttribute("totalItemProduct", this.requisitionService
                    .getLastRequisitionByDealer(dealer.getId()).getRequisitionProductHistoryDTOS().size());
            return "dealer/authenticated-registration-dealer";
        }
        return "dealer/registration-dealer";
    }

    @PostMapping(path = "/save")
    public String registrationDealer(@Valid @ModelAttribute("dealerDTO") DealerDTO dealerDTO, BindingResult result,
                                     @RequestParam(value = "dealerImage", required = false) MultipartFile dealerImage,
                                     @RequestParam(value = "dealerNID", required = false) MultipartFile dealerNID,
                                     @RequestParam(value = "dealerTIN", required = false) MultipartFile dealerTIN,
                                     Model model, Principal principal) throws IOException {
        model.addAttribute("dealerDTO", dealerDTO);
        if (result.hasErrors()) {
            return "dealer/registration-dealer";
        }

        MultipartFile[] files = {dealerImage, dealerNID, dealerTIN};

        // unique identity email and contact
        Boolean isDuplicateEmailOrContact = this.dealerService
                .isDuplicateDealerByEmailOrContact(dealerDTO.getEmail(), dealerDTO.getContact());
        if (isDuplicateEmailOrContact) {
            model.addAttribute("dangerMessage", "email or contact already exist!!");
            return "dealer/registration-dealer";
        }

        DealerDTO resultDealerDTO = this.dealerService.registrationDealer(dealerDTO, files);

        // if image is not add
        if (resultDealerDTO == null) {
            model.addAttribute("dealerDTO", dealerDTO);
            model.addAttribute("message", "please enter dealer image...");
            return "dealer/registration-dealer";
        }

        model.addAttribute("dealerDTO", new DealerDTO());
        model.addAttribute("message", "dealer is successfully added...");

        // get logged-in username
        if (principal != null) {
            DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
            model.addAttribute("dealer", dealer);
            model.addAttribute("totalItemProduct", this.requisitionService
                    .getLastRequisitionByDealer(dealer.getId()).getRequisitionProductHistoryDTOS().size());
            return "dealer/authenticated-registration-dealer";
        }
        return "dealer/registration-dealer";
    }

    // view profile
    @GetMapping(path = "/my/profile")
    public String myProfile(Principal principal, Model model) {
        // get logged-in username
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        DealerDTO dealerDTO = this.dealerService.getSingleDealerById(dealer.getId());

        model.addAttribute("dealerDTO", dealerDTO);

        model.addAttribute("message", "");
        model.addAttribute("dangerMessage", "");
        model.addAttribute("totalItemProduct", this.requisitionService
                .getLastRequisitionByDealer(dealer.getId()).getRequisitionProductHistoryDTOS().size());
        return "dealer/profile-dealer";
    }


    // edit dealer details
    @GetMapping(path = "/edit/my/profile")
    public String editDealer(Principal principal, Model model) {

        // get logged-in username
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        DealerDTO dealerDTO = this.dealerService.getSingleDealerById(dealer.getId());

        model.addAttribute("dealerDTO", dealerDTO);

        model.addAttribute("message", "");
        model.addAttribute("dangerMessage", "");
        model.addAttribute("totalItemProduct", this.requisitionService
                .getLastRequisitionByDealer(dealer.getId()).getRequisitionProductHistoryDTOS().size());
        return "dealer/update-dealer";
    }

    // update dealer
    @PostMapping("/update")
    public String updateDealerSuccess(@Valid @ModelAttribute(value = "dealerDTO") DealerDTO dealerDTO, BindingResult result,
                                      @RequestParam(value = "dealerImage", required = false) MultipartFile dealerImage,
                                      Model model, Principal principal) throws IOException {
        // get logged-in username
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);

        if (result.hasErrors()) {
            model.addAttribute("dealerDTO", dealerDTO);
            return "dealer/update-dealer";
        }
        dealerDTO = this.dealerService
                .updateDealer(dealerDTO, dealer.getId(), dealerImage);    // save the dealer database
        model.addAttribute("dealerDTO", new DealerDTO());

        model.addAttribute("totalItemProduct", this.requisitionService
                .getLastRequisitionByDealer(dealer.getId()).getRequisitionProductHistoryDTOS().size());
        return "redirect:/home";
    }


}

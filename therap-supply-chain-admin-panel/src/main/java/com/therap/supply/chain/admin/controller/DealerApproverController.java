package com.therap.supply.chain.admin.controller;

import com.therap.supply.chain.admin.config.AppConstants;
import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.dto.DealerDTO;
import com.therap.supply.chain.admin.service.impl.AuthorityServiceImpl;
import com.therap.supply.chain.admin.service.impl.DealerServiceImpl;
import com.therap.supply.chain.admin.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Controller
@RequestMapping(path = "/approver/dealer")
public class DealerApproverController {

    @Autowired
    private DealerServiceImpl dealerService;

    @Autowired
    private AuthorityServiceImpl authorityService;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    //get all unapproved dealer
    @GetMapping(path = "/{activity}")
    public String getAllUnapprovedDealers(@PathVariable("activity") String activityStatus,
                                          Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        System.out.println(activityStatus);
        List<DealerDTO> dealerDTOS;
        if (activityStatus.equalsIgnoreCase("all"))
            dealerDTOS = this.dealerService.getAllDealers();
        else
            dealerDTOS = this.dealerService.getAllDealersByApproverByStatus(activityStatus.toUpperCase());

        model.addAttribute("dealerDTOS", dealerDTOS);
        model.addAttribute("message", "");
        return "dealer/approver/show-all-unapproved-dealers";
    }

    // get a single dealer
    @GetMapping("/get/{dealerId}")
    public String getSingleDealer(@PathVariable("dealerId") long dealerId,
                                  Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        DealerDTO dealerDTO = this.dealerService.getSingleDealerById(dealerId);
        model.addAttribute("dealerDTO", dealerDTO);
        model.addAttribute("message", "");
        return "dealer/approver/show-single-dealer";
    }

    // dealer approve
    @GetMapping(path = "/{activityStatus}/{dealerId}")
    public String dealerApproveChangeStatus(@PathVariable("dealerId") Long dealerId,
                                            @PathVariable("activityStatus") String activityStatus) throws IOException {
        DealerDTO dealerDTO = this.dealerService.getSingleDealerById(dealerId);
        if (activityStatus.equalsIgnoreCase(AppConstants.active))
            dealerDTO.setIsActiveByDealerApprover(AppConstants.active);
        else if (activityStatus.equalsIgnoreCase(AppConstants.cancel)) {
            dealerDTO.setIsActiveByDealerApprover(AppConstants.cancel);
        }
        this.dealerService.updateDealerActivityByApprover(dealerDTO, dealerId);
        return "redirect:/approver/dealer/all";
    }

    // get image
    @GetMapping(value = "/image/{imageName}", produces = MediaType.APPLICATION_PDF_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response, Principal principal, Model model
    ) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", "attachment; filename=receipt.pdf");
        StreamUtils.copy(resource, response.getOutputStream());
    }

}

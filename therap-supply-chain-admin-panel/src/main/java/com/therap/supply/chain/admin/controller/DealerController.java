package com.therap.supply.chain.admin.controller;

import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.dto.DealerDTO;
import com.therap.supply.chain.admin.dto.ProductDTO;
import com.therap.supply.chain.admin.dto.RequisitionDTO;
import com.therap.supply.chain.admin.service.impl.*;
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
@RequestMapping(path = "/dealer")
public class DealerController {

    @Autowired
    private DealerServiceImpl dealerService;

    @Autowired
    private AuthorityServiceImpl authorityService;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    //get all dealer
    @GetMapping(path = "/all")
    public String getAllDealers(Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        List<DealerDTO> dealerDTOS = this.dealerService.getAllDealers();
        model.addAttribute("dealerDTOS", dealerDTOS);
        model.addAttribute("message", "");
        return "dealer/show-all-dealers";
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
        return "dealer/show-single-dealer";
    }

    // get all requisition by dealer
    @GetMapping(path = "{dealerId}/requisition/{requisitionId}")
    public String getSingleRequisitionByDealer(@PathVariable("dealerId") Long dealerId,
                                               @PathVariable("requisitionId") Long requisitionId,
                                               Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        RequisitionDTO requisitionDTO = this.requisitionService.getSingleRequisitionById(requisitionId);

        model.addAttribute("requisitionDTO", requisitionDTO);
        model.addAttribute("message", "");
        model.addAttribute("totalProduct",
                this.requisitionService.getTotalProductRequisition(requisitionDTO));
        return "requisition/show-single-requisition-by-delaer";
    }

    // get single details requisition by dealer
    @GetMapping(path = "{dealerId}/requisitions")
    public String getAllRequisitionByDealer(@PathVariable("dealerId") Long dealerId,
                                            Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        List<RequisitionDTO> requisitionDTOS = this.requisitionService.getAllRequisitionByDealerId(dealerId);

        model.addAttribute("requisitionDTOS", requisitionDTOS);
        model.addAttribute("message", "");
        return "requisition/show-all-requisition-by-dealer";
    }

    @GetMapping(path = "/product/get/{productId}")
    public String getProductId(@PathVariable("productId") Long productId,
                               Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        ProductDTO productDTO = this.productService.getSingleProduct(productId);
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("message", "");
        return "product/show-single-product";
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

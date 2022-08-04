package com.therap.supply.chain.user.controller;

import com.therap.supply.chain.user.dto.DealerDTO;
import com.therap.supply.chain.user.dto.ProductDTO;
import com.therap.supply.chain.user.service.impl.DealerServiceImpl;
import com.therap.supply.chain.user.service.impl.FileServiceImpl;
import com.therap.supply.chain.user.service.impl.ProductServiceImpl;
import com.therap.supply.chain.user.service.impl.RequisitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Controller
@RequestMapping(path = "/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private DealerServiceImpl dealerService;

    @Autowired
    private RequisitionServiceImpl requisitionService;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    //get all product
    @GetMapping(path = "/all")
    public String showAllProducts(Model model, Principal principal) {

        List<ProductDTO> productDTOS = this.productService.getAllProducts();
        model.addAttribute("productDTOS", productDTOS);
        model.addAttribute("message", "");

        // get logged-in username
        if (principal != null) {
            DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
            model.addAttribute("dealer", dealer);
            model.addAttribute("totalProduct", this.requisitionService
                    .getLastRequisitionByDealer(dealer.getId()).getRequisitionProductHistoryDTOS().size());
            return "product/authenticated/show-all-products";
        }
        return "product/show-all-products";
    }

    // get a single product
    @GetMapping("/get/{productId}")
    public String getSingleProduct(@PathVariable("productId") long productId, Model model, Principal principal) {

        ProductDTO productDTO = this.productService.getSingleProduct(productId);
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("message", "");

        // get logged-in username
        if (principal != null) {
            DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
            model.addAttribute("dealer", dealer);
            model.addAttribute("totalProduct", this.requisitionService
                    .getLastRequisitionByDealer(dealer.getId()).getRequisitionProductHistoryDTOS().size());
            return "product/authenticated/show-single-product";
        }
        return "product/show-single-product";
    }

    // edit product details
    @GetMapping(path = "/edit/{productId}")
    public String editProduct(@PathVariable("productId") long productId, Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "dealer/login";
        DealerDTO dealer = this.dealerService.getDealerDTOIfLoggedIn(principal);
        model.addAttribute("dealer", dealer);
        model.addAttribute("totalProduct", this.requisitionService
                .getLastRequisitionByDealer(dealer.getId()).getRequisitionProductHistoryDTOS().size());

        ProductDTO productDTO = this.productService.getSingleProduct(productId);
        System.err.println(productId);
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("message", "");
        return "product/update-product";
    }

}
package com.therap.supply.chain.admin.controller;

import com.therap.supply.chain.admin.dto.AuthorityDTO;
import com.therap.supply.chain.admin.dto.ProductDTO;
import com.therap.supply.chain.admin.service.impl.AuthorityServiceImpl;
import com.therap.supply.chain.admin.service.impl.FileServiceImpl;
import com.therap.supply.chain.admin.service.impl.ProductServiceImpl;
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
@RequestMapping(path = "/inventory/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private AuthorityServiceImpl authorityService;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    @GetMapping(path = "/add")
    public String addProduct(Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("message", "");
        return "product/add-product";
    }

    @PostMapping(path = "/save")
    public String saveProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO, BindingResult result,
                              @RequestParam(value = "productImage", required = false) MultipartFile productImage,
                              Model model, Principal principal) throws IOException {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        if (result.hasErrors()) {
            model.addAttribute("productDTO", productDTO);
            model.addAttribute("message", "");
            return "product/add-product";
        }

        ProductDTO resultProductDTO = this.productService.addProduct(productDTO, productImage);

        // if image is not add
        if (resultProductDTO == null) {
            model.addAttribute("productDTO", productDTO);
            model.addAttribute("message", "please enter product image...");
            return "product/add-product";
        }

        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("message", "Product is successfully added...");
        return "product/add-product";
    }

    //get all product
    @GetMapping(path = "/all")
    public String showAllProducts(Model model, Principal principal) {

        List<ProductDTO> productDTOS = this.productService.getAllProducts();
        model.addAttribute("productDTOS", productDTOS);
        model.addAttribute("message", "");

        // get logged-in username
        if (principal != null) {
            AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
            model.addAttribute("authority", authority);
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
            AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
            model.addAttribute("authority", authority);
            return "product/authenticated/show-single-product";
        }
        return "product/show-single-product";
    }

    // edit product details
    @GetMapping(path = "/edit/{productId}")
    public String editProduct(@PathVariable("productId") long productId, Model model, Principal principal) {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        ProductDTO productDTO = this.productService.getSingleProduct(productId);
        System.err.println(productId);
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("message", "");
        return "product/update-product";
    }

    // update product
    @PostMapping("/update/{productId}")
    public String updateProductSuccess(@Valid @ModelAttribute(value = "productDTO") ProductDTO productDTO, BindingResult result,
                                       @RequestParam(value = "productImage", required = false) MultipartFile productImage,
                                       @PathVariable("productId") long productId,
                                       Model model, Principal principal) throws IOException {
        // get logged-in username
        if (principal == null)
            return "authority/login";
        AuthorityDTO authority = this.authorityService.getAuthorityDTOIfLoggedIn(principal);
        model.addAttribute("authority", authority);

        if (result.hasErrors()) {
            model.addAttribute("productDTO", productDTO);
            return "product/update-product";
        }
        productDTO = this.productService.updateProduct(productId, productDTO, productImage);    // save the product database
        model.addAttribute("productDTO", new ProductDTO());
        return "redirect:/inventory/product/all/";
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

package com.therap.supply.chain.user.service.impl;

import com.therap.supply.chain.user.dto.AttachmentDTO;
import com.therap.supply.chain.user.dto.ProductDTO;
import com.therap.supply.chain.user.email.EmailSenderService;
import com.therap.supply.chain.user.entities.Product;
import com.therap.supply.chain.user.exception.ResourceNotFoundException;
import com.therap.supply.chain.user.repository.ProductRepository;
import com.therap.supply.chain.user.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AttachmentServiceImpl attachmentService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO getSingleProduct(Long productId) {
//        Product product = getProduct(productId);
//        ProductDTO productDTO = this.productToProductDTO(product);
        return this.productToProductDTO(this.getProduct(productId));
    }

    @Override
    public List<ProductDTO> getAllProducts() {
//        List<ProductDTO> productDTOS = new ArrayList<>();
//        List<Product> products = this.productRepository.findAll();
//
//        for (Product product : products) {
//            ProductDTO productDTO = this.productToProductDTO(product);
//            productDTOS.add(productDTO);
//        }
//        return productDTOS;

        return this.productRepository
                .findAll()
                .stream()
                .map(this::productToProductDTO)
                .collect(Collectors.toList());
    }

    // helper method for get product
    public Product getProduct(Long productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
    }

    // product to productDTO
    public ProductDTO productToProductDTO(Product product) {
        ProductDTO productDTO = this.modelMapper.map(product, ProductDTO.class);
        AttachmentDTO attachmentDTO = this.attachmentService.attachmentToAttachmentDTO(product.getAttachment());
        productDTO.setAttachmentDTO(attachmentDTO);
        return productDTO;
    }

}

package com.therap.supply.chain.admin.service.impl;

import com.therap.supply.chain.admin.dto.AttachmentDTO;
import com.therap.supply.chain.admin.dto.ProductDTO;
import com.therap.supply.chain.admin.email.EmailSenderService;
import com.therap.supply.chain.admin.entities.Attachment;
import com.therap.supply.chain.admin.entities.Dealer;
import com.therap.supply.chain.admin.entities.Product;
import com.therap.supply.chain.admin.exception.ResourceNotFoundException;
import com.therap.supply.chain.admin.repository.ProductRepository;
import com.therap.supply.chain.admin.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public ProductDTO addProduct(ProductDTO productDTO, MultipartFile productImage) throws IOException {
        if (Objects.equals(productImage.getOriginalFilename(), ""))
            return null;

        Attachment productAttachment = this.modelMapper
                .map(this.attachmentService
                        .addSingleAttachment(productImage), Attachment.class);
        productAttachment.setAttachmentName("Image");
        Product product = this.modelMapper.map(productDTO, Product.class);
        product.setAttachment(productAttachment);
        product.setStock(productDTO.getTotal());
        product.setEnable(true);
        product.setDiscountPrice(product.getPrice() * (100 - product.getDiscount()) / 100);

        product = this.productRepository.save(product);

        return this.modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO getSingleProduct(Long productId) {
        Product product = getProduct(productId);
        ProductDTO productDTO = this.modelMapper.map(product, ProductDTO.class);
        AttachmentDTO attachmentDTO = this.modelMapper.map(product.getAttachment(), AttachmentDTO.class);
        productDTO.setAttachmentDTO(attachmentDTO);
        return productDTO;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> productDTOS = new ArrayList<>();
        List<Product> products = this.productRepository.findAll();

        for (Product product : products) {
            ProductDTO productDTO = this.modelMapper.map(product, ProductDTO.class);
            AttachmentDTO attachmentDTO = this.modelMapper.map(product.getAttachment(), AttachmentDTO.class);
            productDTO.setAttachmentDTO(attachmentDTO);
            productDTOS.add(productDTO);
        }

//        return this.productRepository
//                .findAll()
//                .stream()
//                .map(product -> this.modelMapper.map(product, ProductDTO.class))
//                .collect(Collectors.toList());
        return productDTOS;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO, MultipartFile productImage) throws IOException {
        Product product = getProduct(productId);
        if (!Objects.equals(productImage.getOriginalFilename(), "")) {
            boolean previousProductImageIsDeleted = this.fileService.deleteImage(path, product.getAttachment().getAttachmentFileName());
            String productImageName = this.fileService.uploadImage(path, productImage);
            product.getAttachment().setAttachmentFileName(productImageName);
        }

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        product.setAddProduct(productDTO.getAddProduct());
        product.setDiscountPrice(productDTO.getPrice() * (100 - productDTO.getDiscount()) / 100);

        // stock & total product
        long total = product.getTotal() + productDTO.getAddProduct();
        long stock = product.getStock() + productDTO.getAddProduct();
        product.setTotal(total);
        product.setStock(stock);
        product.setEnable(product.getStock() > 0);

        product = this.productRepository.save(product);

        return this.modelMapper.map(product, ProductDTO.class);

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

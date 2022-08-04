package com.therap.supply.chain.admin.service;

import com.therap.supply.chain.admin.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO, MultipartFile productImage) throws IOException;

    ProductDTO getSingleProduct(Long productId);

    List<ProductDTO> getAllProducts();

    ProductDTO updateProduct(Long productId, ProductDTO productDTO, MultipartFile productImage) throws IOException;

}

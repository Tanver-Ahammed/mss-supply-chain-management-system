package com.therap.supply.chain.user.service;

import com.therap.supply.chain.user.dto.ProductDTO;

import java.util.List;

/**
 * @Author: Md. Tanver Ahammed,
 * ICT, MBSTU
 */

public interface ProductService {

    ProductDTO getSingleProduct(Long productId);

    List<ProductDTO> getAllProducts();

}

package com.rkit.e.commerce.service;

import com.rkit.e.commerce.dto.ProductDTO;
import com.rkit.e.commerce.entity.Product;
import com.rkit.e.commerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDTO addProduct(ProductDTO productDTO){
        Product product =new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());
        productDTO.setId(productRepository.save(product).getId());
        return productDTO;
    }

    public List<ProductDTO> addProductList(List<ProductDTO> productDTOList){
       return productDTOList.stream().peek(productDTO -> {
            Product product =new Product();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setStock(productDTO.getStock());
            productDTO.setId(productRepository.save(product).getId());
       }).collect(Collectors.toList());

    }

    public List<ProductDTO> fetchAllProducts(){
        return productRepository.findAll()
                .stream().map(this::getProductDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> searchProduct(String keyword, BigDecimal minPrice, BigDecimal maxPrice){


        return productRepository.searchProducts(keyword, minPrice, maxPrice)
                .stream().map(this::getProductDTO).collect(Collectors.toList());
    }

    private ProductDTO getProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setName(product.getName());
        productDTO.setStock(product.getStock());
        return productDTO;
    }
}

package com.rkit.e.commerce.controller;

import com.rkit.e.commerce.dto.ProductDTO;
import com.rkit.e.commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO){
        return productService.addProduct(productDTO);
    }

    @PostMapping("/products")
    public List<ProductDTO> addProductList(@RequestBody List<ProductDTO> productDTOList){
        return productService.addProductList(productDTOList);
    }

    @GetMapping("/products")
    public List<ProductDTO> getProducts(){
        return productService.fetchAllProducts();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProduct(String keyword, @RequestParam(required = false) BigDecimal minPrice , @RequestParam(required = false) BigDecimal maxPrice){

            return ResponseEntity.ok(productService.searchProduct(keyword, minPrice, maxPrice));

    }
}

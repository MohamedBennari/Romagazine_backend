package com.romagazine.romagazinebackend.controllers;

import com.romagazine.romagazinebackend.entities.Product;
import com.romagazine.romagazinebackend.services.ProductService;
import com.romagazine.romagazinebackend.services.FileStorageService;
import com.romagazine.romagazinebackend.utils.ImageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageService fileStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/type/{type}")
    public List<Product> getProductsByType(@PathVariable String type) {
        return productService.getProductsByType(type);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/in-stock")
    public List<Product> getProductsInStock() {
        return productService.getProductsInStock();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Upload product image", description = "Upload an image for a specific product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Invalid image file"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> uploadProductImage(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id,
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            if (!ImageUtils.isValidImage(file)) {
                return ResponseEntity.badRequest().build();
            }

            String filename = fileStorageService.storeFile(file);
            String imageUrl = ImageUtils.generateImageUrl(filename);

            Product product = productService.getProductById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setImage(imageUrl);
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
package com.romagazine.romagazinebackend.repositories;

import com.romagazine.romagazinebackend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ProductRepository extends JpaRepository<Product, Long> {
    List findByType(String type);
    List findByIsActiveTrue();
    List findByStockGreaterThan(int stock);
}
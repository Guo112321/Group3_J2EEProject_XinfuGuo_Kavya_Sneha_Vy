package org.studentrecord.smarthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studentrecord.smarthub.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryIgnoreCase(String category);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Product> findByAvailable(boolean available);
    List<Product> findByStoreLocationIgnoreCase(String location);

}

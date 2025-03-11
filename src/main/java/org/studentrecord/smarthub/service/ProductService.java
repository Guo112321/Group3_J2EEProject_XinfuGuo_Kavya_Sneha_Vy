package org.studentrecord.smarthub.service;

import org.springframework.stereotype.Service;
import org.studentrecord.smarthub.model.Product;
import org.studentrecord.smarthub.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice, Boolean available, String location) {
        return productRepository.findAll().stream()
                .filter(p -> (name == null || p.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(p -> (category == null || p.getCategory().equalsIgnoreCase(category)))
                .filter(p -> (minPrice == null || maxPrice == null || (p.getPrice() >= minPrice && p.getPrice() <= maxPrice)))
                .filter(p -> (available == null || p.isAvailable() == available))
                .filter(p -> (location == null || p.getStoreLocation().equalsIgnoreCase(location)))
                .toList();
    }


    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

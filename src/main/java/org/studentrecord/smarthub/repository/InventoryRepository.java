package org.studentrecord.smarthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.studentrecord.smarthub.model.Inventory;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {

    List<Inventory> findByItemNameContainingIgnoreCase(String itemName);


    List<Inventory> findByAvailable(boolean available);


    @Query("SELECT i FROM Inventory i WHERE " +
            "(:name IS NULL OR LOWER(i.itemName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:category IS NULL OR LOWER(i.category) = LOWER(:category)) AND " +
            "(:minPrice IS NULL OR i.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR i.price <= :maxPrice)")
    List<Inventory> advancedSearch(
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}

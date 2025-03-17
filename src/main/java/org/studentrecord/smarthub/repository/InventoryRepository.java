package org.studentrecord.smarthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.studentrecord.smarthub.model.Inventory;

import java.util.List;

@Repository
// InventoryRepository.java
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Custom query to find products by name (case-insensitive search)
    List<Inventory> findByItemNameContainingIgnoreCase(String itemName);

    List<Inventory> findByAvailable(boolean available); // Custom query to find only available items
}

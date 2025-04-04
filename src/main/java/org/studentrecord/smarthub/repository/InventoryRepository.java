package org.studentrecord.smarthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.studentrecord.smarthub.model.Inventory;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {

    // Basic search
    List<Inventory> findByItemNameContainingIgnoreCase(String itemName);

    // Simple availability filter
    List<Inventory> findByAvailable(boolean available);
}

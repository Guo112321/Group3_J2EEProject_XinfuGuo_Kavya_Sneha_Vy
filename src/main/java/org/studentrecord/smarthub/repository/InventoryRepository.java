package org.studentrecord.smarthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studentrecord.smarthub.model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {}

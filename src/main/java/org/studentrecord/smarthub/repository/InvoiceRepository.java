package org.studentrecord.smarthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studentrecord.smarthub.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
 }

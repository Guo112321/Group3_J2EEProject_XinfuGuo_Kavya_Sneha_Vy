package org.studentrecord.smarthub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studentrecord.smarthub.model.Invoice;
import org.studentrecord.smarthub.model.Order;
import org.studentrecord.smarthub.repository.InvoiceRepository;

import java.util.Date;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice generateInvoice(Order order) {
        Invoice invoice = Invoice.builder()
                .order(order)
                .createdDate(new Date())
                .invoiceNumber("INV-" + order.getId() + "-" + System.currentTimeMillis())
                .totalAmount(order.getTotalAmount())
                .build();

        return invoiceRepository.save(invoice);
    }

    public Invoice findById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId).orElse(null);
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }
}

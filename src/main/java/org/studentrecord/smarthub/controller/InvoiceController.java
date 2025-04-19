package org.studentrecord.smarthub.controller;

import org.springframework.web.bind.annotation.*;
import org.studentrecord.smarthub.model.Invoice;
import org.studentrecord.smarthub.service.InvoiceService;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{id}")
    public Invoice getInvoice(@PathVariable Long id) {
        return invoiceService.findById(id);
    }
}



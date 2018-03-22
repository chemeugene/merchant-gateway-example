package ru.allrecipes.merchant.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.allrecipes.merchant.domain.Invoice;
import ru.allrecipes.merchant.domain.InvoicePaymentRequest;
import ru.allrecipes.merchant.service.InvoiceService;

@RestController
@RequestMapping(value = "/api/v1", method = RequestMethod.GET, produces = {
    MimeTypeUtils.APPLICATION_JSON_VALUE })
@Api(value = "Invoice")
public class InvoiceController {

  private InvoiceService invoiceService;

  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @GetMapping(value = "/invoicesByCustomerUsername")
  @ApiOperation(value = "Retrieves invoices for user")
  public List<Invoice> getInvoicesByCustomerUsername(@RequestParam("username") String username) {
    return invoiceService.getInvoicesByCustomerUsername(username);
  }

  @PostMapping(value = "/payInvoice")
  @ApiOperation(value = "Pay invoices (1..n)")
  public void payInvoice(@RequestBody InvoicePaymentRequest request) {
    invoiceService.payInvoice(null);
  }

}

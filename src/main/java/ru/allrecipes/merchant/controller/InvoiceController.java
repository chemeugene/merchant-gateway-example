package ru.allrecipes.merchant.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import ru.allrecipes.merchant.Constants;
import ru.allrecipes.merchant.domain.Card;
import ru.allrecipes.merchant.domain.Invoice;
import ru.allrecipes.merchant.domain.InvoicePaymentRequest;
import ru.allrecipes.merchant.domain.InvoicePaymentResponse;
import ru.allrecipes.merchant.domain.InvoicePaymentResponse.BulkStatus;
import ru.allrecipes.merchant.service.InvoiceService;
import ru.allrecipes.merchant.system.InvoiceSession;
import ru.paymentgate.engine.webservices.merchant.PaymentOrderResult;

@RestController
@RequestMapping(value = "/api/v1", produces = { MimeTypeUtils.APPLICATION_JSON_VALUE })
@Api(value = "Invoice")
public class InvoiceController {

  private InvoiceService invoiceService;

  private InvoiceSession invoiceSession;

  public InvoiceController(InvoiceService invoiceService, InvoiceSession invoiceSession) {
    this.invoiceService = invoiceService;
    this.invoiceSession = invoiceSession;
  }

  @GetMapping(value = "/invoicesByCustomerUsername")
  @ApiOperation(value = "Retrieves invoices for user")
  public List<Invoice> getInvoicesByCustomerUsername(@RequestParam("username") String username) {
    return invoiceService.getInvoicesByCustomerUsername(username);
  }

  @PostMapping(value = "/payInvoice")
  @ApiOperation(value = "Pay invoices (1..n)")
  public InvoicePaymentResponse payInvoice(@RequestBody InvoicePaymentRequest request,
      HttpServletRequest httpRequest, RedirectAttributes redirectAttrs)
      throws IOException, URISyntaxException {
    InvoicePaymentResponse response = invoiceService.payInvoice(request);
    if (response.getBulkStatus() != BulkStatus.OK) {
      return response;
    } else {
      URI requestUri = new URI(httpRequest.getRequestURL().toString());
      URI contextUri = new URI(requestUri.getScheme(), requestUri.getAuthority(),
          httpRequest.getContextPath(), null, null);
      response.setSuccessFormUrl(UriComponentsBuilder.fromUri(contextUri).path("/payment.html")
          .queryParam(Constants.TOTAL_AMOUNT, response.getTotalAmount())
          .queryParam(Constants.BULK_KEY_ATTR, request.hashCode()).build().encode().toUriString());
      return response;
    }
  }

  @PostMapping(value = "/doPayment")
  public String doPayment(@ModelAttribute("card") Card card, BindingResult result, ModelMap model) {
    List<PaymentOrderResult> errors = invoiceService.performPayment(card,
        invoiceSession.getOrdersByInvoiceKey(Integer.valueOf(card.getBulkRequestId())));
    if (errors != null) {
      String errorsString = errors.stream().map(error -> error.getErrorMessage())
          .collect(Collectors.joining(","));
      return "errorPage: " + errorsString;
    } else {
      return "successPage";
    }
  }

}

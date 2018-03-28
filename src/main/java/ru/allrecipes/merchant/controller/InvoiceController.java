package ru.allrecipes.merchant.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import ru.allrecipes.merchant.Constants;
import ru.allrecipes.merchant.domain.Card;
import ru.allrecipes.merchant.domain.Invoice;
import ru.allrecipes.merchant.domain.InvoicePaymentRequest;
import ru.allrecipes.merchant.domain.InvoicePaymentResponse;
import ru.allrecipes.merchant.domain.InvoicePaymentResponse.BulkStatus;
import ru.allrecipes.merchant.domain.InvoicesWrapper;
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

  /**
   * Accepts invoices form.
   * 
   * @param invoiceWrapper - invoiceWrapper
   * @param httpRequest - httpRequest
   * @param redirectAttrs - redirectAttrs
   * @return redirect to payment page
   * @throws IOException - IOException
   * @throws URISyntaxException - URISyntaxException
   */
  @RequestMapping(value = { "/payInvoiceForm" }, method = RequestMethod.POST)
  public RedirectView payInvoiceForm(
      @ModelAttribute("invoiceWrapper") InvoicesWrapper invoiceWrapper,
      HttpServletRequest httpRequest, RedirectAttributes redirectAttrs)
      throws IOException, URISyntaxException {
    Set<Integer> invoiceIds = invoiceWrapper.getItems().stream().filter(item -> item.isEnabled())
        .map(item -> item.getInvoiceId()).collect(Collectors.toSet());
    InvoicePaymentRequest req = new InvoicePaymentRequest(invoiceIds);
    InvoicePaymentResponse response = payInvoice(req, httpRequest, redirectAttrs);
    RedirectView redirectView = new RedirectView(response.getSuccessFormUrl());
    return redirectView;
  }

  /**
   * Bulk operation - pay invoices.
   * 
   * @param request
   *          - request
   * @param httpRequest
   *          - httpRequest
   * @param redirectAttrs
   *          - redirectAttrs
   * @return {@link InvoicePaymentResponse}
   * @throws IOException
   *           - IOException
   * @throws URISyntaxException
   *           - URISyntaxException
   */
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

  /**
   * Payment form processor.
   * 
   * @param card
   *          - card
   * @param result
   *          - result
   * @param model
   *          - model
   * @return page
   */
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

package ru.allrecipes.merchant.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ru.allrecipes.merchant.Constants;
import ru.allrecipes.merchant.domain.Card;
import ru.allrecipes.merchant.domain.Invoice;
import ru.allrecipes.merchant.domain.InvoicesWrapper;
import ru.allrecipes.merchant.dto.InvoiceDto;
import ru.allrecipes.merchant.service.InvoiceService;

@Controller
@RequestMapping("/")
public class RootController {

  private InvoiceService invoiceService;

  public RootController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  /**
   * Returns payment form.
   * 
   * @param bulkKey
   *          - bulkKey
   * @param totalAmount
   *          - totalAmount
   * @return page
   */
  @RequestMapping(value = "/payment.html", method = RequestMethod.GET)
  public ModelAndView paymentPage(@RequestParam("bulkKey") String bulkKey,
      @RequestParam("totalAmount") Double totalAmount) {
    ModelAndView modelAndView = new ModelAndView("payment", "Card",
        Card.builder().cardHolder("Inav Petrov").cardNumber("5555 5555 5555 5599")
            .validThrough("2019/12").cvc("123").bulkRequestId(bulkKey).build());
    modelAndView.addObject(Constants.TOTAL_AMOUNT, totalAmount);
    return modelAndView;
  }

  /**
   * Index page.
   * 
   * @return index page
   */
  @RequestMapping(value = "/")
  public ModelAndView index() {
    List<Invoice> invoices = invoiceService.getInvoicesByCustomerUsername("customer");
    List<InvoiceDto> dtos = invoices.stream()
        .map(invoice -> new InvoiceDto(false, invoice.getInvoiceId(), invoice.getId(),
            invoice.getAmount(), invoice.getSupplier().getName()))
        .collect(Collectors.toList());
    ModelAndView modelAndView = new ModelAndView("index", "invoiceWrapper",
        new InvoicesWrapper(dtos));
    return modelAndView;
  }

}

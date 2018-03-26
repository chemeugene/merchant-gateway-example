package ru.allrecipes.merchant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ru.allrecipes.merchant.Constants;
import ru.allrecipes.merchant.domain.Card;

@Controller
public class RootController {

  @RequestMapping(value = "/payment.html", method = RequestMethod.GET)
  public ModelAndView paymentPage(@RequestParam("bulkKey") String bulkKey,
      @RequestParam("totalAmount") Double totalAmount) {
    ModelAndView modelAndView = new ModelAndView("payment", "Card",
        Card.builder().cardHolder("Inav Petrov").cardNumber("5555 5555 5555 5599")
            .validThrough("2019/12").cvc("123").bulkRequestId(bulkKey).build());
    modelAndView.addObject(Constants.TOTAL_AMOUNT, totalAmount);
    return modelAndView;
  }

}

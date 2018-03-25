package ru.allrecipes.merchant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ru.allrecipes.merchant.domain.Card;

@Controller
public class RootController {

  @RequestMapping(value = "/payment.html", method = RequestMethod.GET)
  public ModelAndView paymentPage(@ModelAttribute("bulkKey") String bulkKey) {
    return new ModelAndView("payment", "Card",
        Card.builder().cardHolder("Inav Petrov").cardNumber("4111 1111 1111 1111")
            .validThrough("2019/12").cvc("123").bulkRequestId(bulkKey).build());
  }

}

package ru.allrecipes.merchant.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Card {

  private String cardNumber;
  
  private String cardHolder;
  
  private String cvc;
  
  private String validThrough;
  
  private String bulkRequestId;
}

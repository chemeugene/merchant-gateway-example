package ru.allrecipes.merchant.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

  private String cardNumber;
  
  private String cardHolder;
  
  private String cvc;
  
  private String validThrough;
  
  private String bulkRequestId;
}

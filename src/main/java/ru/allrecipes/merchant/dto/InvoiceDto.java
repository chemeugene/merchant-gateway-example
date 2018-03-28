package ru.allrecipes.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

  private boolean enabled;
  
  private Integer invoiceId;
  
  private Long id;
  
  private Double amount;
  
  private String supplier;
}

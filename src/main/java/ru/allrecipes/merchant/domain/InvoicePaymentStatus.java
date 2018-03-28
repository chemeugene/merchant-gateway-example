package ru.allrecipes.merchant.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class InvoicePaymentStatus {
  
  private Long invoiceId;
  
  private OperationResult operationResult;
  
  private String errorCode;

  private String errorDescription;

  public static enum OperationResult {
    OK, ALREADY_PAID, LOCKED, ERROR, MERCHANT_ERROR;
  }
}

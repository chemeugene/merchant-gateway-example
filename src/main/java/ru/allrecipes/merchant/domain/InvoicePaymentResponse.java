package ru.allrecipes.merchant.domain;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class InvoicePaymentResponse {

  private List<InvoicePaymentStatus> statuses;
  
  private BulkStatus bulkStatus;
  
  private Double totalAmount;
  
  private String successFormUrl;
  
  public static enum BulkStatus {
    OK, PARTIAL, REJECTED
  }
}

package ru.allrecipes.merchant.domain;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePaymentRequest {

  @NotNull
  @Size(min = 1)
  private Set<Integer> invoiceIds;

}

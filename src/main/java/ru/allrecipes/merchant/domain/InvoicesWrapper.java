package ru.allrecipes.merchant.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.allrecipes.merchant.dto.InvoiceDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoicesWrapper {

  private List<InvoiceDto> items;
}

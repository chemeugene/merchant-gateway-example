package ru.allrecipes.merchant.system;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import ru.paymentgate.engine.webservices.merchant.RegisterOrderResponse;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class InvoiceSession {

  private Map<Integer, List<RegisterOrderResponse>> ordersByBulkInvoiceKey = 
      new ConcurrentHashMap<>();

  public void addOrders(Integer bulkInvoiceKey, List<RegisterOrderResponse> orders) {
    ordersByBulkInvoiceKey.put(bulkInvoiceKey, orders);
  }

  public List<RegisterOrderResponse> getOrdersByInvoiceKey(Integer invoiceKey) {
    return ordersByBulkInvoiceKey.get(invoiceKey);
  }
}

package ru.allrecipes.merchant.bank.alfabank;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ru.bpc.phoenix.web.api.merchant.soap.MerchantService;
import ru.bpc.phoenix.web.api.merchant.soap.MerchantServiceImplService;

@Configuration
public class AlfabankWsdlConfiguration {

  /**
   * Jaxb2Marshaller.
   * 
   * @return {@link Jaxb2Marshaller}
   */
  @Bean
  public Jaxb2Marshaller marshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("ru.allrecipes.merchant.bank.alfabank.wsdl");
    return marshaller;
  }

  /**
   * AlfabankClient.
   * 
   * @param marshaller
   *          {@link Jaxb2Marshaller}
   * @return {@link AlfabankClient}
   */
  @Bean
  public AlfabankClient quoteClient(Jaxb2Marshaller marshaller) {
    AlfabankClient client = new AlfabankClient();
    client.setDefaultUri("https://web.rbsuat.com/ab/webservices/merchant-ws");
    client.setMarshaller(marshaller);
    client.setUnmarshaller(marshaller);
    return client;
  }
}

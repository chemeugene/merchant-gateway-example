package ru.allrecipes.merchant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.filter.CharacterEncodingFilter;

import ru.bpc.phoenix.web.api.merchant.soap.MerchantServiceImplService;

@SpringBootApplication
@ComponentScan(basePackages = { "ru.allrecipes", "ru.bpc", "ru.paymentgate" })
public class MerchantApplication {

  public static void main(String[] args) {
    SpringApplication.run(MerchantApplication.class, args);
  }

  @Bean
  public MerchantServiceImplService merchantService() {
    return new MerchantServiceImplService();
  }
  
}

package ru.allrecipes.merchant.system;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.allrecipes.merchant.config.BankConfig;

@Component
@Slf4j
public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {

  private BankConfig config;

  public HeaderHandler(BankConfig config) {
    super();
    this.config = config;
  }

  @Override
  public boolean handleMessage(SOAPMessageContext smc) {
    Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

    if (outboundProperty.booleanValue()) {
      SOAPMessage message = smc.getMessage();
      try {

        SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();

        SOAPElement security = header.addChildElement("Security", "wsse",
            "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");

        SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");

        SOAPElement username = usernameToken.addChildElement("Username", "wsse");
        username.addTextNode(config.getLogin());

        SOAPElement password = usernameToken.addChildElement("Password", "wsse");
        password.setAttribute("Type",
            "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        password.addTextNode(config.getPassword());
        message.writeTo(System.out);

      } catch (Exception e) {
        log.error("Unable to create UsernameToken", e);
      }
    } else {
      try {
        // This handler does nothing with the response from the Web Service so
        // we just print out the SOAP message.
        SOAPMessage message = smc.getMessage();
        message.writeTo(System.out);
      } catch (Exception e) {
        log.warn("Unable to print response");
      }
    }

    return outboundProperty;
  }

  @Override
  public boolean handleFault(SOAPMessageContext context) {
    return true;
  }

  @Override
  public void close(MessageContext context) {
  }

  @Override
  public Set<QName> getHeaders() {
    return null;
  }

}

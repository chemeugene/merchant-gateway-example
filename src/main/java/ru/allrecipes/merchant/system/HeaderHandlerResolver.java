package ru.allrecipes.merchant.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import org.springframework.stereotype.Component;

@Component
public class HeaderHandlerResolver implements HandlerResolver {

  private HeaderHandler handler;

  public HeaderHandlerResolver(HeaderHandler handler) {
    this.handler = handler;
  }

  @Override
  @SuppressWarnings("rawtypes")
  public List<Handler> getHandlerChain(PortInfo portInfo) {
    List<Handler> handlerChain = new ArrayList<Handler>();
    handlerChain.add(handler);
    return handlerChain;
  }

}

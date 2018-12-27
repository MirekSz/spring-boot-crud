
package com.example.democamel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class ResponseProcessor implements Processor {

	@Override
	public void process(final Exchange exchange) throws Exception {
		exchange.getOut().setBody(exchange.getExchangeId());

	}

}

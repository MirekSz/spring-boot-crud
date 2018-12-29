
package org.apache.camel.example.cxf.proxy;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.example.reportincident.InputReportIncident;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Component
public class EnrichBean implements Processor {

	public Document enrich(final Document doc) {
		Node node = doc.getElementsByTagName("incidentId").item(0);
		String incident = node.getTextContent();

		// here we enrich the document by changing the incident id to another value
		// you can of course do a lot more in your use-case
		node.setTextContent("456");
		System.out.println("Incident was " + incident + ", changed to 456");

		return doc;
	}

	@Override
	public void process(final Exchange exchange) throws Exception {
		InputReportIncident body2 = exchange.getIn().getBody(InputReportIncident.class);
		Document body = exchange.getIn().getBody(Document.class);
		Document enrich = enrich(body);
		exchange.getIn().setBody(enrich);
	}
}

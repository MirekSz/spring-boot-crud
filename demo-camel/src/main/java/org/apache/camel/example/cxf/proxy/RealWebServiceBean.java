
package org.apache.camel.example.cxf.proxy;

import javax.xml.ws.Endpoint;

public class RealWebServiceBean {

	// should be the same address as we have in our route
	private String url;

	private final ReportIncidentEndpointService service = new ReportIncidentEndpointService();
	private Endpoint endpoint;

	public void start() throws Exception {
		System.out.println("Starting real web service...");
		endpoint = Endpoint.publish(url, service);
		System.out.println("Started real web service at: " + url);
	}

	public void stop() throws Exception {
		if (endpoint != null) {
			System.out.println("Stopping real webservice...");
			endpoint.stop();
			System.out.println("Stopped real webservice.");
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}
}

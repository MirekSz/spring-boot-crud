
package org.apache.camel.example.cxf.proxy;

import javax.xml.ws.Endpoint;

import org.apache.camel.example.reportincident.InputReportIncident;
import org.apache.camel.example.reportincident.OutputReportIncident;
import org.apache.camel.example.reportincident.ReportIncidentEndpoint;

public class ReportIncidentEndpointServiceRunner implements ReportIncidentEndpoint {

	@Override
	public OutputReportIncident reportIncident(final InputReportIncident in) {
		// just log and return a fixed response
		System.out.println("\n\n\nInvoked real web service: id=" + in.getIncidentId() + " by " + in.getGivenName() + " "
				+ in.getFamilyName() + "\n\n\n" + in.getPhone());

		OutputReportIncident out = new OutputReportIncident();
		out.setCode("OK;" + in.getIncidentId() + " " + in.getPhone());
		return out;
	}

	public static void main(final String[] args) {
		Endpoint.publish("http://localhost:6070/incident", new ReportIncidentEndpointServiceRunner());
	}

}

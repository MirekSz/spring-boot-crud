
package org.apache.camel.example.cxf.proxy;

import org.apache.camel.example.reportincident.InputReportIncident;
import org.apache.camel.example.reportincident.OutputReportIncident;
import org.apache.camel.example.reportincident.ReportIncidentEndpoint;
import org.springframework.stereotype.Component;

@Component
public class ReportIncidentEndpointService implements ReportIncidentEndpoint {

	@Override
	public OutputReportIncident reportIncident(final InputReportIncident in) {
		// just log and return a fixed response
		System.out.println("\n\n\nInvoked real web service: id=" + in.getIncidentId() + " by " + in.getGivenName() + " "
				+ in.getFamilyName() + "\n\n\n");

		OutputReportIncident out = new OutputReportIncident();
		out.setCode("OK;" + in.getIncidentId());
		return out;
	}

}

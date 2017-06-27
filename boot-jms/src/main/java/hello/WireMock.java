package hello;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.util.List;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class WireMock {
	public static void main(String[] args) throws InterruptedException {
		WireMockServer wireMockServer = new WireMockServer(
				wireMockConfig().fileSource(new ClasspathFileSource("wiremock")).port(8089)); // No-args
		// no
		// HTTPS
		wireMockServer.start();

		// Do some stuff
		List<StubMapping> stubMappings = wireMockServer.getStubMappings();
		System.out.println(stubMappings.get(0).getRequest().getUrl());
		System.out.println(stubMappings.get(0).getResponse().getBody());

		// Finish doing stuff
		Thread.sleep(100000);
		wireMockServer.stop();

	}
}

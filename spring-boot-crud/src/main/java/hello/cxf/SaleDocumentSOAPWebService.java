package hello.cxf;

import hello.model.SaleDocument;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.stereotype.Component;

@Component
@WebService
public class SaleDocumentSOAPWebService {
	@WebMethod
	public SaleDocument findById(Long id) {
		SaleDocument saleDocument = new SaleDocument();
		saleDocument.setNumber("SOAP " + id);

		return saleDocument;
	}
}

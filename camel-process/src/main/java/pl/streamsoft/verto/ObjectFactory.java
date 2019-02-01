
package pl.streamsoft.verto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pl.streamsoft.verto package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Get_QNAME = new QName("http://verto.streamsoft.pl", "get");
    private final static QName _GetResponse_QNAME = new QName("http://verto.streamsoft.pl", "getResponse");
    private final static QName _WebServiceException_QNAME = new QName("http://verto.streamsoft.pl", "WebServiceException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.streamsoft.verto
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Get }
     * 
     */
    public Get createGet() {
        return new Get();
    }

    /**
     * Create an instance of {@link GetResponse }
     * 
     */
    public GetResponse createGetResponse() {
        return new GetResponse();
    }

    /**
     * Create an instance of {@link WebServiceException }
     * 
     */
    public WebServiceException createWebServiceException() {
        return new WebServiceException();
    }

    /**
     * Create an instance of {@link GoodWO }
     * 
     */
    public GoodWO createGoodWO() {
        return new GoodWO();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Get }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Get }{@code >}
     */
    @XmlElementDecl(namespace = "http://verto.streamsoft.pl", name = "get")
    public JAXBElement<Get> createGet(Get value) {
        return new JAXBElement<Get>(_Get_QNAME, Get.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://verto.streamsoft.pl", name = "getResponse")
    public JAXBElement<GetResponse> createGetResponse(GetResponse value) {
        return new JAXBElement<GetResponse>(_GetResponse_QNAME, GetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WebServiceException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WebServiceException }{@code >}
     */
    @XmlElementDecl(namespace = "http://verto.streamsoft.pl", name = "WebServiceException")
    public JAXBElement<WebServiceException> createWebServiceException(WebServiceException value) {
        return new JAXBElement<WebServiceException>(_WebServiceException_QNAME, WebServiceException.class, null, value);
    }

}

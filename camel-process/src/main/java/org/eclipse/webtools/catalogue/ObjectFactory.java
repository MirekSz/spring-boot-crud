
package org.eclipse.webtools.catalogue;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.eclipse.webtools.catalogue package. 
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

    private final static QName _Get_QNAME = new QName("http://www.eclipse.org/webtools/Catalogue", "get");
    private final static QName _Publication_QNAME = new QName("http://www.eclipse.org/webtools/Catalogue", "Publication");
    private final static QName _BookResponse_QNAME = new QName("http://www.eclipse.org/webtools/Catalogue", "bookResponse");
    private final static QName _Magazine_QNAME = new QName("http://www.eclipse.org/webtools/Catalogue", "Magazine");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.eclipse.webtools.catalogue
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
     * Create an instance of {@link PublicationType }
     * 
     */
    public PublicationType createPublicationType() {
        return new PublicationType();
    }

    /**
     * Create an instance of {@link BookType }
     * 
     */
    public BookType createBookType() {
        return new BookType();
    }

    /**
     * Create an instance of {@link MagazineType }
     * 
     */
    public MagazineType createMagazineType() {
        return new MagazineType();
    }

    /**
     * Create an instance of {@link Catalogue }
     * 
     */
    public Catalogue createCatalogue() {
        return new Catalogue();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Get }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Get }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.eclipse.org/webtools/Catalogue", name = "get")
    public JAXBElement<Get> createGet(Get value) {
        return new JAXBElement<Get>(_Get_QNAME, Get.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PublicationType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PublicationType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.eclipse.org/webtools/Catalogue", name = "Publication")
    public JAXBElement<PublicationType> createPublication(PublicationType value) {
        return new JAXBElement<PublicationType>(_Publication_QNAME, PublicationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BookType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BookType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.eclipse.org/webtools/Catalogue", name = "bookResponse", substitutionHeadNamespace = "http://www.eclipse.org/webtools/Catalogue", substitutionHeadName = "Publication")
    public JAXBElement<BookType> createBookResponse(BookType value) {
        return new JAXBElement<BookType>(_BookResponse_QNAME, BookType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MagazineType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MagazineType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.eclipse.org/webtools/Catalogue", name = "Magazine", substitutionHeadNamespace = "http://www.eclipse.org/webtools/Catalogue", substitutionHeadName = "Publication")
    public JAXBElement<MagazineType> createMagazine(MagazineType value) {
        return new JAXBElement<MagazineType>(_Magazine_QNAME, MagazineType.class, null, value);
    }

}

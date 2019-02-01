
package org.eclipse.webtools.catalogue;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.eclipse.org/webtools/Catalogue}Publication" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "publication"
})
@XmlRootElement(name = "Catalogue")
public class Catalogue {

    @XmlElementRef(name = "Publication", namespace = "http://www.eclipse.org/webtools/Catalogue", type = JAXBElement.class)
    protected List<JAXBElement<? extends PublicationType>> publication;

    /**
     * Gets the value of the publication property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the publication property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPublication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link BookType }{@code >}
     * {@link JAXBElement }{@code <}{@link MagazineType }{@code >}
     * {@link JAXBElement }{@code <}{@link PublicationType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends PublicationType>> getPublication() {
        if (publication == null) {
            publication = new ArrayList<JAXBElement<? extends PublicationType>>();
        }
        return this.publication;
    }

}

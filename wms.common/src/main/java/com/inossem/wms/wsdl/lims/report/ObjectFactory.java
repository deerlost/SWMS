
package com.inossem.wms.wsdl.lims.report;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inossem.wms.wsdl.lims.report package. 
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

    private final static QName _String_QNAME = new QName("http://tempuri.org/", "string");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inossem.wms.wsdl.lims.report
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LIMSQADATABATCHResponse }
     * 
     */
    public LIMSQADATABATCHResponse createLIMSQADATABATCHResponse() {
        return new LIMSQADATABATCHResponse();
    }

    /**
     * Create an instance of {@link LIMSQADATA3Response }
     * 
     */
    public LIMSQADATA3Response createLIMSQADATA3Response() {
        return new LIMSQADATA3Response();
    }

    /**
     * Create an instance of {@link LIMSQADATAGUIGEResponse }
     * 
     */
    public LIMSQADATAGUIGEResponse createLIMSQADATAGUIGEResponse() {
        return new LIMSQADATAGUIGEResponse();
    }

    /**
     * Create an instance of {@link PROLIMSQUERYCOM }
     * 
     */
    public PROLIMSQUERYCOM createPROLIMSQUERYCOM() {
        return new PROLIMSQUERYCOM();
    }

    /**
     * Create an instance of {@link LIMSQADATAGUIGE }
     * 
     */
    public LIMSQADATAGUIGE createLIMSQADATAGUIGE() {
        return new LIMSQADATAGUIGE();
    }

    /**
     * Create an instance of {@link LIMSQADATABATCH }
     * 
     */
    public LIMSQADATABATCH createLIMSQADATABATCH() {
        return new LIMSQADATABATCH();
    }

    /**
     * Create an instance of {@link LIMSQADATAID }
     * 
     */
    public LIMSQADATAID createLIMSQADATAID() {
        return new LIMSQADATAID();
    }

    /**
     * Create an instance of {@link PROLIMSQUERYCOMResponse }
     * 
     */
    public PROLIMSQUERYCOMResponse createPROLIMSQUERYCOMResponse() {
        return new PROLIMSQUERYCOMResponse();
    }

    /**
     * Create an instance of {@link LIMSQADATAIDResponse }
     * 
     */
    public LIMSQADATAIDResponse createLIMSQADATAIDResponse() {
        return new LIMSQADATAIDResponse();
    }

    /**
     * Create an instance of {@link LIMSQADATA3 }
     * 
     */
    public LIMSQADATA3 createLIMSQADATA3() {
        return new LIMSQADATA3();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

}
